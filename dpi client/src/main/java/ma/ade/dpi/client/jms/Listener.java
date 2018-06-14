package ma.ade.dpi.client.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener implements MessageListener {
    private Receiver receiver;
    private IMessageHandler handler;

    public Listener(IMessageHandler handler) {
        this.handler = handler;
    }

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("received " + textMessage.getText());
            handler.handleMessage(textMessage.getText());
            if ("END".equals(textMessage.getText())) {
                receiver.latchCountDown();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void setAsyncReceiveQueueClientExample(Receiver receiver) {
        this.receiver = receiver;
    }
}