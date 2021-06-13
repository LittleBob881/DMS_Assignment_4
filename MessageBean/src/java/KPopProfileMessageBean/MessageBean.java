/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileMessageBean;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.json.Json;
import javax.json.JsonObject;
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

    public MessageBean() {
    }

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String messageString = ((TextMessage) message).getText();
                System.out.println("Message Recieved: " + messageString);
                JsonObject json = convertStringToJson(messageString);
                
                switch(json.getString("method")) {
                    case "addFaveBand":
                        addFaveBand(json);
                        break;
                        
                    default:
                        System.out.println("Method did not match expected methods. ");
                }
                

                /*
                Connect to database and look up the band and validate is a band in our db. 
                Then add a new map between the user and the band
                 */
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
        
        //TODO: Connect to db and add band o.o
        if(entityManager != null)
        {
            try {
                userTransaction.begin();
                entityManager.persist(newFav);
                entityManager.flush();
                userTransaction.commit();
                logger.info("Transaction done! "+username+" -> "+bandName);
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(MessageBean.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }   
        
        return true;
    }
}
