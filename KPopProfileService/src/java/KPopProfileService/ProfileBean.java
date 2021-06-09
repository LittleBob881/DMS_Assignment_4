/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * This Stateful EJB Bean is used to create or update user details, such as their
 * username or kpop favourite
 */

@Stateful
public class ProfileBean {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    //for commits to database
    @Resource private UserTransaction userTransaction;
    @PersistenceContext (unitName = "KPopProfileServicePU")
    private EntityManager entityManager;
    private List<UserProfile> usernameList;
    
    //populate username list from database
    @PostConstruct
    public void initialiseRecipeList()
    {        
        if(entityManager != null)
        {
            usernameList  = entityManager
                    .createQuery("Select u from UserProfile u", UserProfile.class)
                    .getResultList();
        }     
    }
    
    public void login(String userName) {
        
        boolean userExists = false;
       
        if(entityManager != null)
        {
            //check if username exists
            for(UserProfile user : usernameList)
            {
                if(user.getUsername().equalsIgnoreCase(userName))
                    userExists = true;
            }
            
            //username does not exist, then create a record with username
           if(!userExists)
           {
                try {
                    //persist in UserProfile object
                    UserProfile newUser = new UserProfile();
                    newUser.setUsername(userName);
                    
                    //commit transaction to "kpop_users" database
                    userTransaction.begin();
                    entityManager.persist(newUser);
                    entityManager.flush();
                    userTransaction.commit();
                } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                    Logger.getLogger(ProfileBean.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
     }     
    }
}
