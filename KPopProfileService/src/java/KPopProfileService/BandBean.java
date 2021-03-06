/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.json.Json;
import javax.json.JsonObject;
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
 * The EJB logic to get or add favourite bands for a user.
 *
 */
@Stateless
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class BandBean {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    MessageSender messageSender = new MessageSender();

    //for commits to database    
    @Resource
    private UserTransaction userTransaction;

    @PersistenceContext(unitName = "KPopProfileServicePU")
    private EntityManager entityManager;
    
    private List<Band> bandList;
    
    @PostConstruct
    public void initialiseUsernameList() {
        if (entityManager != null) {
            bandList = entityManager
                    .createQuery("Select b from Band b", Band.class)
                    .getResultList();
        }
    }

    public List<Band> getFavouriteBands(String userName) {
        List<FavouriteBand> favouriteBands;
        List<Band> favouriteBandDetails = new ArrayList<>();

        //Connect to database and get the list of bands for the user name that is passed in. 
        if (entityManager != null) {
            Query query = entityManager.createQuery("Select b from FavouriteBand b WHERE b.username = :username", FavouriteBand.class);
            query.setParameter("username", userName);

            favouriteBands = query.getResultList();

            //check if user has favourite bands, then return list of specific band details
            if (!favouriteBands.isEmpty()) {

                //iterate through list of all bands in database
                for (Band band : bandList) {
                    //iterate through user's favouritee bands, check against all band list
                    for (FavouriteBand fav : favouriteBands) {
                        if (fav.getBandName().equalsIgnoreCase(band.getName())) {
                            favouriteBandDetails.add(band);
                        }
                    }
                }
            }
        }
        return favouriteBandDetails;
    }

    public String getAllBandsJSON() {
        JsonObject faveBandJSON = Json.createObjectBuilder()
                .add("numVariables", 1)
                .add("method", "getAllBands")
                .build();

        System.out.println("Sending  messages");
        String response = messageSender.sendMessage(faveBandJSON.toString());
        System.out.println("Sending completed");

        return response;
    }

    public boolean addFavouriteBand(String bandName, String userName) {
        JsonObject faveBandJSON = Json.createObjectBuilder()
                .add("numVariables", 3)
                .add("method", "addFaveBand")
                .add("userName", userName)
                .add("bandName", bandName)
                .build();

        System.out.println("Sending  messages");
        String response = messageSender.sendMessage(faveBandJSON.toString());
        System.out.println("Sending completed");

        return Boolean.parseBoolean(response);
    }
    
       public boolean removeFavouriteBand(String bandName, String userName) {
        JsonObject faveBandJSON = Json.createObjectBuilder()
                .add("numVariables", 3)
                .add("method", "removeFaveBand")
                .add("userName", userName)
                .add("bandName", bandName)
                .build();

        System.out.println("Sending messages");
        String response = messageSender.sendMessage(faveBandJSON.toString());
        System.out.println("Sending completed");

        return Boolean.parseBoolean(response);
    }
}
