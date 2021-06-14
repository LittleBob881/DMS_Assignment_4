/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileMessageBean;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

/**
 *
 * Description.
 */
@MessageDriven(activationConfig
        = {
            @ActivationConfigProperty(propertyName = "destinationLookup",
                    propertyValue = "jms/KPopProfileQueue")
            ,
        @ActivationConfigProperty(propertyName = "destinationType",
                    propertyValue = "javax.jms.Queue")
        })
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class MessageBean {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    //for commits to database
    @Resource
    private UserTransaction userTransaction;
    @PersistenceContext(unitName = "MessageBeanPU")
    private EntityManager entityManager;

    //for sending reply messages
    private Connection conn;
    private Session session;
    private static ConnectionFactory connectionFactory;
    private MessageProducer messageProducer;

    public MessageBean() {
        try {
            Context ctx = new InitialContext();
            connectionFactory = (ConnectionFactory) ctx.lookup("jms/ConnectionFactory");
            conn = connectionFactory.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            messageProducer = session.createProducer(null);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        } catch (NamingException ex) {
            System.out.println("Could not create connection factory. " + ex);
        } catch (JMSException ex) {
            System.out.println("Could not make connection with connection factory. " + ex);
        }

    }

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String messageString = ((TextMessage) message).getText();
                System.out.println("Message Recieved: " + messageString);
                JsonObject json = convertStringToJson(messageString);

                switch (json.getString("method")) {
                    case "addFaveBand":
                        boolean success = addFaveBand(json);
                        sendResponse(message, success);
                        break;
                    default:
                        System.out.println("Method did not match expected methods. ");
                }
            } else {
                System.out.println("MessageBean received non-text message: " + message);
            }

        } catch (JMSException e) {
            System.err.println("Exception with incoming message: " + e);
        }
    }

    private JsonObject convertStringToJson(String jsonString) {
        StringTokenizer st = new StringTokenizer(jsonString, "\"{:,");
        st.nextToken();
        String method = st.nextToken();
        st.nextToken();
        String userName = st.nextToken();
        st.nextToken();
        String bandName = st.nextToken();

        JsonObject json = Json.createObjectBuilder()
                .add("method", method)
                .add("userName", userName)
                .add("bandName", bandName)
                .build();

        System.out.println("Convert Method. Method: " + method + ". User Name: "
                + userName + ". Band Name: " + bandName);

        return json;
    }

    //add favourite band of user to database and commit transaction
    private boolean addFaveBand(JsonObject bandJson) {
        String username = bandJson.getString("userName");
        String bandName = bandJson.getString("bandName");

        FavouriteBand newFav = new FavouriteBand();
        newFav.setBandName(bandName);
        newFav.setUsername(username);

        if (entityManager != null) {
            try {
                userTransaction.begin();
                entityManager.persist(newFav);
                entityManager.flush();
                userTransaction.commit();
                logger.info("Transaction done! " + username + " -> " + bandName);
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        return true;
    }

    private void sendResponse(Message message, boolean success) {
        try {
            TextMessage response = this.session.createTextMessage();
            response.setText(String.valueOf(success));
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            System.out.println("Sending back response");
            messageProducer.send(message.getJMSReplyTo(), response);
            System.out.println("Sending back response complete");

        } catch (JMSException ex) {
            System.out.println("Could not create reply message. " + ex);
        }
    }

}
