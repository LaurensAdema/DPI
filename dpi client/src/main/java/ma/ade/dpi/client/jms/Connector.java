package ma.ade.dpi.client.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.ejb.Stateless;
import javax.jms.*;
import java.io.Serializable;

@Stateless
public class Connector implements Serializable {
    private ConnectionFactory connectionFactory;

    public Connector() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
}
