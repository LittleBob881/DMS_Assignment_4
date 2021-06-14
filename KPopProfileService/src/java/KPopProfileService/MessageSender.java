/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.util.UUID;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * 
 */
public class MessageSender {

    private Connection conn;
    private Session session;
    private MessageProducer producer;
    private static ConnectionFactory connectionFactory;
    private static Queue queue;

    public MessageSender() {
        try {
            Context ctx = new InitialContext();
            connectionFactory = (ConnectionFactory) ctx.lookup("jms/ConnectionFactory");
            queue = (Queue) ctx.lookup("jms/KPopProfileQueue");
        } catch (NamingException ex) {
            System.out.println("Could not create connection factory. Try removing the glassfish/domains/domain1/imq/instances/imqbroker/lock file and restarting the server. " + ex);
        }

        try {
            // obtain a connection to the JMS provider
            conn = connectionFactory.createConnection();
            // obtain an untransacted context for producing messages
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // obtain a producer of messages to send to the queue
            producer = session.createProducer(queue);
        } catch (JMSException e) {
            System.err.println("Unable to open connection: " + e);
        }
    }

    public void send(String JSONString) {
        try {
            TextMessage message = session.createTextMessage();
            message.setText(JSONString);
            System.out.println("Sending JSON String: " + message);
            producer.send(message);
        } catch (JMSException e) {
            System.err.println("Unable to send JSON String: " + e);
        }
    }

    public void sendForResponse(String JSONString) {
        try {
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            responseConsumer.setMessageListener(new MessageReceiver());
            conn.start();

            TextMessage message = session.createTextMessage();
            message.setText(JSONString);
            message.setJMSReplyTo(tempDest);

            System.out.println("Setting reply destination to: " + tempDest.toString());
            UUID uuid = UUID.randomUUID();
            message.setJMSCorrelationID(uuid.toString());

            producer.send(message);
        } catch (JMSException ex) {
            System.out.println("Could not send message. " + ex);
        }
    }
}
