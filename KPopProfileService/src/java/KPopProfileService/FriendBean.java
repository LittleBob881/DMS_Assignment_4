/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * This Singleton EJB Bean is used to add or get friends of a user
 */
@Singleton
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class FriendBean {

    @PersistenceContext(unitName = "KPopProfileServicePU")
    private EntityManager entityManager;
        
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
    
   
        public List<String> getFriends(String userName) {
        List<String> friends = new ArrayList<>();
        List<Friend> friendEntities;

        //Connect to database and friends for username
        if (entityManager != null) {
            Query query = entityManager.createQuery("Select f from Friend f WHERE f.username1 = :username "
                    +"OR f.username2 = :username",  Friend.class);
                            
            query.setParameter("username", userName);

            friendEntities = query.getResultList();
            
            //check if user has friends
            if (!friendEntities.isEmpty()) {
                for (Friend friend : friendEntities) {
                    if(friend.getUsername1().equalsIgnoreCase(userName))
                        friends.add(friend.getUsername2());
                    
                    else if(friend.getUsername2().equalsIgnoreCase(userName))
                        friends.add(friend.getUsername1());
                }
            }
        }
           
        return friends;
    }
}
