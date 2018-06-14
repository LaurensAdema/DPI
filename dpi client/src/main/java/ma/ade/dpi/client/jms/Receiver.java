package ma.ade.dpi.client.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.CountDownLatch;

import javax.jms.*;

public class Receiver {
    private Connection connection;
    private Session session;
    private CountDownLatch latch = new CountDownLatch(1);
    private Listener listener;

    public Receiver() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        this.connection = connectionFactory.createConnection();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void receiveMessages(String queueString, IMessageHandler messageHandler) throws JMSException, InterruptedException {
        try {
            Queue queue = session.createQueue(queueString);
            MessageConsumer consumer = session.createConsumer(queue);
            listener = new Listener(messageHandler);
            consumer.setMessageListener(listener);
            listener.setReceiver(this);

            connection.start();
            latch.await();
        } finally {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void stopListening() throws JMSException {
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public void latchCountDown() {
        latch.countDown();
    }
}