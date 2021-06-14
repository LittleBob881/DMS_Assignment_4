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
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * This class sends messages from the RESTful service to a message bean which
 * access the database and sends back a response message. The send message
 * method will wait for the response message before continuing as the response
 * it needed for the HTTP requests made.
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
            System.out.println("Could not create connection factory. Try removing "
                    + "the glassfish/domains/domain1/imq/instances/imqbroker/lock file and restarting the server. " + ex);
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

    public String sendMessage(String JSONString) {
        String response = null;
        try {
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            conn.start();

            TextMessage message = session.createTextMessage();
            message.setText(JSONString);
            message.setJMSReplyTo(tempDest);

            UUID uuid = UUID.randomUUID();
            message.setJMSCorrelationID(uuid.toString());

            producer.send(message);

            Message responseMessage = responseConsumer.receive();
            response = responseMessage(responseMessage);
            System.out.println("Response: " + response);
        } catch (JMSException ex) {
            System.out.println("Could not send message. " + ex);
        }

        return response;
    }

    private String responseMessage(Message responseMessage) {
        String messageText = null;
        System.out.println("Message recieved on consumer");
        try {
            if (responseMessage instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) responseMessage;
                messageText = textMessage.getText();
                System.out.println("Message on client. messageText = " + messageText);

            }
        } catch (JMSException e) {
            System.out.println("Could not get response message. " + e);
        }

        return messageText;
    }
}
