package ma.ade.dpi.clientapi.messaging;

import ma.ade.dpi.domain.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Sender {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange broker;

    public void send(Message message) {
        template.convertAndSend(broker.getName(), "broker", message);
    }
}
