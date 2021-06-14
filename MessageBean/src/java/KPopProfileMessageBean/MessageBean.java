/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileMessageBean;

import KPopProfileEntities.Band;
import KPopProfileEntities.FavouriteBand;
import KPopProfileEntities.UserProfile;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

/**
 *
 * Receives messages on queue from KPopProfileService. Sends back a response
 * message.
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
    private List<UserProfile> usernameList;

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

    //populate username list from database
    @PostConstruct
    public void initialiseUsernameList() {
        if (entityManager != null) {
            usernameList = entityManager
                    .createQuery("Select u from UserProfile u", UserProfile.class)
                    .getResultList();
        }
    }

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String messageString = ((TextMessage) message).getText();
                System.out.println("Message Recieved: " + messageString);
                JsonArray json = convertStringToJson(messageString);

                boolean success;
                switch (json.getString(0)) {
                    case "addFaveBand":
                        success = addFaveBand(json);
                        sendResponse(message, String.valueOf(success));
                        break;
                    case "removeFaveBand":
                        success = removeFaveBand(json);
                        sendResponse(message, String.valueOf(success));
                        break;
                    case "login":
                        success = login(json);
                        sendResponse(message, String.valueOf(success));
                        break;
                    case "getAllBands":
                        String allBands = getAllBands();
                        sendResponse(message, allBands);
                        break;
                    default:
                        System.out.println("Method did not match expected methods. ");
                        sendResponse(message, "false");
                }
            } else {
                System.out.println("MessageBean received non-text message: " + message);
            }

        } catch (JMSException e) {
            System.err.println("Exception with incoming message: " + e);
        }
    }

    private JsonArray convertStringToJson(String jsonString) {
        StringTokenizer st = new StringTokenizer(jsonString, "\"{:,");
        st.nextToken();

        int numVariables = Integer.parseInt(st.nextToken());
        String[] keys = new String[numVariables];
        String[] values = new String[numVariables];
        for (int i = 0; i < numVariables; i++) {
            keys[i] = st.nextToken();
            values[i] = st.nextToken();
        }

        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (int i = 0; i < numVariables; i++) {
            builder.add(i, values[i]);
        }

        JsonArray json = builder.build();

        System.out.println("Convert Method JSON String: " + json);

        return json;
    }

    private void sendResponse(Message message, String success) {
        try {
            TextMessage response = this.session.createTextMessage();
            response.setText(success);
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            System.out.println("Sending back response");
            messageProducer.send(message.getJMSReplyTo(), response);
            System.out.println("Sending back response complete");

        } catch (JMSException ex) {
            System.out.println("Could not create reply message. " + ex);
        }
    }

    //add favourite band of user to database and commit transaction
    private boolean addFaveBand(JsonArray bandJson) {
        String username = bandJson.getString(1);
        String bandName = bandJson.getString(2);

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

    private boolean removeFaveBand(JsonArray bandJson)
    {
        String username = bandJson.getString(1);
        String bandName = bandJson.getString(2);
        
        if(entityManager != null)
        {
            try {
                userTransaction.begin();
                Query query = entityManager.createQuery("DELETE from FavouriteBand f WHERE "
                        +"f.username = :username AND f.bandName = :bandName", FavouriteBand.class);
                query.setParameter("username", username);
                query.setParameter("bandName", bandName).executeUpdate();
                
                entityManager.flush();
                userTransaction.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    //add user to database if not already existing and commit transaction
    private boolean login(JsonArray loginJson) {
        String username = loginJson.getString(1);
        boolean userExists = false;

        //check if username exists
        for (UserProfile user : usernameList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                userExists = true;
                logger.info("User exists!");
            }

        }

        //username does not exist, then create a record with the username
        if (!userExists) {
            //persist in UserProfile object
            UserProfile newUser = new UserProfile();
            newUser.setUsername(username);

            //commit transaction to "kpop_users" database
            try {
                userTransaction.begin();
                entityManager.persist(newUser);
                entityManager.flush();
                userTransaction.commit();
                usernameList.add(newUser);
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                System.out.println("Error in connecting to database");
                return false;
            }
        }

        return true;
    }
    
    private String getAllBands() {
        List<Band> allBandsList = null;
        
        if (entityManager != null) {
            allBandsList = entityManager
                    .createQuery("Select b from Band b", Band.class)
                    .getResultList();
        }

        //parse into json
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        //add each band object into a JSON array
        for(Band band : allBandsList)
        {
            builder.add("name", band.getName());
            builder.add("generation", band.getGeneration());
            builder.add("year", band.getYear());
            builder.add("fandomName", band.getFandomName());
          
            arrayBuilder.add(builder.build());
        }
        
        //build json array as object
        builder.add("bands", arrayBuilder.build());
        
        //return whole JsonObject
        JsonObject bandsJSON = builder.build();
        System.out.println("All Band List JSON: " + bandsJSON);
        return bandsJSON.toString();
    }
}
