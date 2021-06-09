/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.Queue;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * Description. 
 */
public class MessageSender {
    private Connection conn;
    private Session session;
    private MessageProducer producer;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/KPopProfileQueue")
    private static Queue queue;

    public MessageSender() {
        try {
            // obtain a connection to the JMS provider
            conn = connectionFactory.createConnection();
            // obtain an untransacted context for producing messages
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // obtain a producer of messages to send to the queue
            producer = session.createProducer((Destination) queue);
        } catch (JMSException e) {
            System.err.println("Unable to open connection: " + e);
        }
    }

    public void send(String JSONFaveBand) { 
        try {
            TextMessage message = session.createTextMessage();
            message.setText(JSONFaveBand);
            System.out.println("Sending JSON String: " + message);
            producer.send(message);
        } catch (JMSException e) {
            System.err.println("Unable to send JSON String: " + e);
        }
    }

    public void closeConnection() {
        try {
            if (session != null) {
                session.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (JMSException e) {
            System.err.println("Unable to close connection: " + e);
        }
    }
}
