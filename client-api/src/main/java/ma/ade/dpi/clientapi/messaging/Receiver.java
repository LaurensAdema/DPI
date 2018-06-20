package ma.ade.dpi.clientapi.messaging;

import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ade.dpi.domain.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private SimpMessagingTemplate template;
    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    public Receiver(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RabbitHandler
    public void receiveMessage(Message message) {
        try {
            this.template.convertAndSend(message.getDestination(), new ObjectMapper().writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}