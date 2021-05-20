/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * Description. 
 */
@Stateless
public class FavouriteBean {
    
    public List<Band> getFavouriteBands(String userName) {
        List<Band> bands = new ArrayList<>();
        
        /*
        Connect to database and get the list of bands for the user name that is passed in. 
        */
        
        return bands;
    }

    public void addFavouriteBand(String bandName, String userName) {
        /*
        Connect to database and look up the band and validate is a band in our db. 
            Then add a new map between the user and the band
        */
    }
}
