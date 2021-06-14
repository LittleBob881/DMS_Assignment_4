/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * Receives message responses from message bean. 
 */
public class MessageReceiver  implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String messageText;
        System.out.println("Message recieved: " + message);
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                messageText = textMessage.getText();
                System.out.println("Message on client. messageText = " + messageText);
            }
        } catch (JMSException e) {
            System.out.println("Could not get response message. " + e);
        }
    }
    
}
