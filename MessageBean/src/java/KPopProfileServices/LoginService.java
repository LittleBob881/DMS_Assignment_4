/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileServices;

import KPopProfileEntities.UserProfile;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * Service to add user to database if not already existing and commit transaction.
 */
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class LoginService {
    
    private Logger logger = Logger.getLogger(this.getClass().getName());

    //for commits to database
    @Resource
    private UserTransaction userTransaction;
    @PersistenceContext(unitName = "MessageBeanPU")
    private EntityManager entityManager;
    private List<UserProfile> usernameList;
    
    public boolean login(JsonArray loginJson) {
        String username = loginJson.getString(1);
        
        boolean userExists = false;

        if (entityManager != null && username != null) {
            usernameList = entityManager
                    .createQuery("Select u from UserProfile u", UserProfile.class)
                    .getResultList();

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
        } else {
            return false;
        }
    }
}
