/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileMessageBean;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 *
 * Description.
 */
@MessageDriven(activationConfig
        = {
            @ActivationConfigProperty(propertyName = "destinationLookup",
                    propertyValue = "jms/KPopProfileQueue")
            ,
        @ActivationConfigProperty(propertyName = "destinationType",
                    propertyValue = "javax.jms.Queue")
        })
public class MessageBean {

    public MessageBean() {
    }

    public void onMessage(Message message) {
        try {
            /*
                Connect to database and look up the band and validate is a band in our db. 
                Then add a new map between the user and the band
             */
            if (message instanceof TextMessage) {
                System.out.println("MessageBean received text message: "
                        + ((TextMessage) message).getText());
            } else {
                System.out.println("MessageBean received non-text message: " + message);
            }
        } catch (JMSException e) {
            System.err.println("Exception with incoming message: " + e);
        }
    }
}
