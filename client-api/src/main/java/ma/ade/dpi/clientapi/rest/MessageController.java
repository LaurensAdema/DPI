package ma.ade.dpi.clientapi.rest;

import ma.ade.dpi.domain.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @RequestMapping(value = "/rest/message", method = RequestMethod.GET)
    public void getMessages() {
        // get messages
    }

    @RequestMapping(value = "/rest/message", method = RequestMethod.POST)
    public void postMessage(Message message) {
        // send message
    }
}