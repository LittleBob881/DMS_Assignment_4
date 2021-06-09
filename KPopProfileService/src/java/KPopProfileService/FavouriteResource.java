/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    private FavouriteBean favouriteBean;
    
    public FavouriteResource()
    {}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{username}")
    public String getFavourites(@PathParam("username") String userName) {
        List<Band> favouriteBandsList = favouriteBean.getFavouriteBands(userName);
        
        //parse into json
        JsonObjectBuilder builder = Json.createObjectBuilder();
        ArrayList<JsonObject> bandObjects = new ArrayList<>();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        
        //add each band object into a JSON array
        for(Band band : favouriteBandsList)
        {
            builder.add("name", band.getName());
            builder.add("generation", band.getGeneration());
            builder.add("year", band.getYear());
            builder.add("fandomName", band.getFandomName());
          
            arrayBuilder.add(builder.build());
        }
        
        //build json array as object
        builder.add("favouriteBands", arrayBuilder.build());
        
        //return whole JsonObject
        JsonObject favouriteBandsJSON = builder.build();
        
        return favouriteBandsJSON.toString();
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
        System.out.println("Band Name: " + bandName);
        String userName = st.nextToken();
        System.out.println("User Name: " + userName);
        
        favouriteBean.addFavouriteBand(bandName, userName);
    }
}
