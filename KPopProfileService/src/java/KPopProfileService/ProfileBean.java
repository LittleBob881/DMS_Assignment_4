/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * This Stateful EJB Bean is used to create or update user details, such as their
 * username or kpop favourite
 */

@Singleton
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class ProfileBean {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    MessageSender messageSender = new MessageSender();
    
    //for commits to database
    @Resource private UserTransaction userTransaction;
    
    @PersistenceContext (unitName = "KPopProfileServicePU")
    private EntityManager entityManager;
    private List<UserProfile> usernameList;
    
    //populate username list from database
    @PostConstruct
    public void initialiseUsernameList()
    {        
        if(entityManager != null)
        {
            usernameList  = entityManager
                    .createQuery("Select u from UserProfile u", UserProfile.class)
                    .getResultList();
        }     
    }
    
    public boolean login(String username) {
        JsonObject loginJSON = Json.createObjectBuilder()
                .add("numVariables", 2)
                .add("method", "login")
                .add("userName", username)
                .build();

        System.out.println("Sending  messages");
        String response = messageSender.sendMessage(loginJSON.toString());
        System.out.println("Sending completed");
        
        return Boolean.parseBoolean(response);
    }
}
