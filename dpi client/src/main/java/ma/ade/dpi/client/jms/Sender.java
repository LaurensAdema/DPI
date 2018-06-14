package ma.ade.dpi.client.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.inject.Inject;
import javax.jms.*;

public class Sender {
    Connection connection;
    Session session;
    Queue queue;
    MessageProducer producer;

    public Sender(String queueString) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        this.connection = connectionFactory.createConnection();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.queue = session.createQueue(queueString);
        this.producer = session.createProducer(queue);
    }

    public void sendMessage(String message) throws JMSException {
        producer.send(session.createTextMessage(message));
    }

    public void shutdown() throws JMSException {
        session.close();
        connection.close();
    }
}
