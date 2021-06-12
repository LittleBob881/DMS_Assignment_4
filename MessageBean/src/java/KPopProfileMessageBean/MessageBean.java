/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileMessageBean;

import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.json.Json;
import javax.json.JsonObject;

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
public class MessageBean {

    //for commits to database
//    @Resource
//    private UserTransaction userTransaction;
//    @PersistenceContext(unitName = "MessageBeanPU")
//    private EntityManager entityManager;

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

    private void addFaveBand(JsonObject bandJson) {
        //TODO: Connect to db and add band o.o
    }
}
