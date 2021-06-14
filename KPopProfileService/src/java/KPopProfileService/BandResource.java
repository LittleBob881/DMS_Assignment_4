/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

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
@Path("/bands")
public class BandResource {
    
    @EJB
    private BandBean favouriteBean;
    
    public BandResource()
    {}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllBands()
    {
        List<Band> allBandsList = favouriteBean.getAllBands();

        //parse into json
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        //add each band object into a JSON array
        for(Band band : allBandsList)
        {
            builder.add("name", band.getName());
            builder.add("generation", band.getGeneration());
            builder.add("year", band.getYear());
            builder.add("fandomName", band.getFandomName());
          
            arrayBuilder.add(builder.build());
        }
        
        //build json array as object
        builder.add("bands", arrayBuilder.build());
        
        //return whole JsonObject
        JsonObject bandsJSON = builder.build();
        
        return bandsJSON.toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("favourite/{username}")
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
        builder.add("bands", arrayBuilder.build());
        
        //return whole JsonObject
        JsonObject favouriteBandsJSON = builder.build();
        
        return favouriteBandsJSON.toString();
    }
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addfavourite")
    public boolean addFavouriteBand(String favouriteJson) {
        StringTokenizer st = new StringTokenizer(favouriteJson, "\"");
        String bandName = st.nextToken();
        System.out.println("Band Name: " + bandName);
        st.nextToken();
        String userName = st.nextToken();
        System.out.println("User Name: " + userName);
        
        boolean success = favouriteBean.addFavouriteBand(bandName, userName);
        return success;
    }
    
    @POST
    @Path("addfavourite")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public boolean addFavouriteBand(MultivaluedMap<String, String> formParams) {
        
        String userName = formParams.getFirst("userName");
        String bandName = formParams.getFirst("bandName");
        
        System.out.println("Add Fave: " + userName + " " + bandName);
        
        boolean success = favouriteBean.addFavouriteBand(bandName, userName);
        return success;
    }
}
