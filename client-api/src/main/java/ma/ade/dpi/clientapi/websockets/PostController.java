package ma.ade.dpi.clientapi.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ade.dpi.domain.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class PostController {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange broker;

    @MessageMapping("/post")
    public void handle(String messageString) {
        try {
            Message message = new ObjectMapper().readValue(messageString, Message.class);
            template.convertAndSend(broker.getName(), message.getDestination(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
