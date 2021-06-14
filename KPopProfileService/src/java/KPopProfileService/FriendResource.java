/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
}
