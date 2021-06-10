/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
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
@Named  //for dependency injection EJB
@Path("/userprofile")
public class ProfileResource {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @EJB 
    private ProfileBean profileBean;
    
    public ProfileResource()
    {}
      
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public boolean login(MultivaluedMap<String, String> formParams) {
        
        String username = formParams.getFirst("username");
        boolean success = profileBean.login(username);

        return success;
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public void login(String userNameJSON) {
        StringTokenizer st = new StringTokenizer(userNameJSON, "\"");
        String userName = st.nextToken();
        profileBean.login(userName);
    }
}
