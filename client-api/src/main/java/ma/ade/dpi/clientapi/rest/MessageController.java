package ma.ade.dpi.clientapi.rest;

import ma.ade.dpi.domain.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MessageController {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange broker;

    @RequestMapping(value = "/rest/message", method = RequestMethod.GET)
    public void getMessages() {
        // get messages
    }

    @RequestMapping(value = "/rest/message", method = RequestMethod.POST)
    public void postMessage(@RequestBody Message message) {
        template.convertAndSend(broker.getName(), "broker", message);
    }
}