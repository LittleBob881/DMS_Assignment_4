/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * Description. 
 */
@Named 
@Path("/favourites")
public class FavouriteResource {
    
    @EJB
    FavouriteBean favouriteBean;
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getFavourites(String userNameJSON) {
        StringTokenizer st = new StringTokenizer(userNameJSON, "\"");
        String userName = st.nextToken();
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("[ ");
        List<Band> allbands = favouriteBean.getFavouriteBands(userName);
        for (int i = 0; i < allbands.size(); i++) {
            buffer.append(allbands.get(i).getJSONString());
            if (i != allbands.size() - 1) {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
    
    @POST
    @Path("/favourite")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addFavouriteBand(MultivaluedMap<String, String> formParams) {
        String bandName = formParams.getFirst("bandName");
        String userName = formParams.getFirst("userName");
        favouriteBean.addFavouriteBand(bandName, userName);
    }
    
    @POST
    @Path("/favourite")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addFavouriteBand(String bandNameJSON) {
        StringTokenizer st = new StringTokenizer(bandNameJSON, "\"");
        String bandName = st.nextToken();
        //CHANGE TO TAKE IN TWO PARAMS
        favouriteBean.addFavouriteBand(bandName, userName);
    }
}
