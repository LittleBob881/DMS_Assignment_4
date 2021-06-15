/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 */
@Named  //for dependency injection EJB
@Path("/friends")
public class FriendResource {
    
       private Logger logger = Logger.getLogger(this.getClass().getName());

    @EJB
    private FriendBean friendBean;

    public FriendResource() {
    }
    
    @POST
    @Path("addfriend")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public boolean addFriend(MultivaluedMap<String, String> formParams) {

        String username1 = formParams.getFirst("username1");
        String username2 = formParams.getFirst("username2");
        
        boolean success = friendBean.addFriend(username1, username2);

        return success;
    }
    
    @GET
    @Path("friend/{username}")
    public String getFriends(@PathParam("username") String userName) {
        List<String> userFriendsList = friendBean.getFriends(userName);
        
        //parse into json
        JsonObjectBuilder builder = Json.createObjectBuilder();
        ArrayList<JsonObject> bandObjects = new ArrayList<>();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        //add each band object into a JSON array
        for (String name : userFriendsList) {
            builder.add("username", name);
            
            arrayBuilder.add(builder.build());
        }

        //build json array as object
        builder.add("friends", arrayBuilder.build());

        //return whole JsonObject
        JsonObject friendsJSON = builder.build();

        return friendsJSON.toString();
    }
}
