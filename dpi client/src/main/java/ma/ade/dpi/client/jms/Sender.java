package ma.ade.dpi.client.jms;

import javax.inject.Inject;
import javax.jms.*;

public class Sender {
    @Inject
    Connector connector;

    ConnectionFactory connectionFactory;
    Connection connection;
    Session session;
    Queue queue;
    MessageProducer producer;

    public Sender(String queueString) throws JMSException {
        connectionFactory = connector.getConnectionFactory();
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue(queueString);
        producer = session.createProducer(queue);
    }

    public void sendMessage(String message) throws JMSException {
        producer.send(session.createTextMessage(message));
    }

    public void shutdown() throws JMSException {
        session.close();
        connection.close();
    }
}
