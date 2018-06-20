package ma.ade.dpi.domain;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private User sender;
    private Date date;
    private String message;
    private String destination;

    public Message(User sender, Date date, String message, String destination) {
        this.sender = sender;
        this.date = date;
        this.message = message;
        this.destination = destination;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
