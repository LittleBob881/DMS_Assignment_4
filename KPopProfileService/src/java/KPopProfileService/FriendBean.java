/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * This Singleton EJB Bean is used to add or get friends of a user
 */
@Singleton
public class FriendBean {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    MessageSender messageSender = new MessageSender();

    public boolean addFriend(String username1, String username2) {
        JsonObject friendJSON = Json.createObjectBuilder()
                .add("numVariables", 3)
                .add("method", "addFriend")
                .add("username1", username1)                
                .add("username2", username2)
                .build();

        System.out.println("Sending  messages");
        String response = messageSender.sendMessage(friendJSON.toString());
        System.out.println("Sending completed");

        return Boolean.parseBoolean(response);
    }
}
