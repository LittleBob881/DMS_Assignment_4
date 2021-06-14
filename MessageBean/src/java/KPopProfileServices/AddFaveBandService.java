/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileServices;

import KPopProfileEntities.FavouriteBand;
import KPopProfileMessageBean.MessageBean;
import java.util.logging.Level;
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
 * Service to add favourite band of user to database and commit transaction. 
 */
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class AddFaveBandService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    //for commits to database
    @Resource
    private UserTransaction userTransaction;
    @PersistenceContext(unitName = "MessageBeanPU")
    private EntityManager entityManager;

    public boolean addFaveBand(JsonArray bandJson) {
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
}
