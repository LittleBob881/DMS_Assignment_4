/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import javax.ejb.Stateless;

/**
 *
 * Description. 
 */
@Stateless
public class ProfileBean {
    
    public void login(String userName) {
        /* 
        We want to connect to the database here and either do nothing if the user exists 
            or make a new user with the name. 
        Might want to return/show something to user about if they are new or returning...? Can discuss.
        */
    }
}
