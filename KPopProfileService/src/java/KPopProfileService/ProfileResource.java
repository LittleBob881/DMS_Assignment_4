/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * Description. 
 */
@Named 
@Path("/profile")
public class ProfileResource {
    
    @EJB 
    ProfileBean profileBean;
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void login(MultivaluedMap<String, String> formParams) {
        String userName = formParams.getFirst("userName");
        profileBean.login(userName);
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
