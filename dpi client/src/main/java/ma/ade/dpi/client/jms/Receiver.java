package ma.ade.dpi.client.jms;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

public class Receiver {

    @Inject
    private Connector connector;

    private Connection connection;
    private Session session;
    private CountDownLatch latch = new CountDownLatch(1);
    private Listener listener;

    public Receiver() throws JMSException {
        this.connection = connector.getConnectionFactory().createConnection();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void receiveMessages(String queueString, IMessageHandler messageHandler) throws JMSException, InterruptedException {
        try {
            Queue queue = session.createQueue(queueString);
            MessageConsumer consumer = session.createConsumer(queue);
            listener = new Listener(messageHandler);
            consumer.setMessageListener(listener);
            listener.setAsyncReceiveQueueClientExample(this);

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