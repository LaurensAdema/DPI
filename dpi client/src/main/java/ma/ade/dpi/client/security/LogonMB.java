package ma.ade.dpi.client.security;

import com.github.adminfaces.template.session.AdminSession;
import ma.ade.dpi.client.domain.User;
import ma.ade.dpi.client.jms.IMessageHandler;
import ma.ade.dpi.client.jms.Receiver;
import ma.ade.dpi.client.service.UserService;
import ma.ade.dpi.client.util.Utils;
import org.omnifaces.util.Faces;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.github.adminfaces.template.config.AdminConfig;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import javax.jms.JMSException;

/**
 * Created by rmpestano on 12/20/14.
 * <p>
 * This is just a login example.
 * <p>
 * AdminSession uses isLoggedIn to determine if user must be redirect to login page or not.
 * By default AdminSession isLoggedIn always resolves to true so it will not try to redirect user.
 * <p>
 * If you already have your authorization mechanism which controls when user must be redirect to initial page or logon
 * you can skip this class.
 */
@Named
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable, IMessageHandler {

    private User currentUser;
    private String name;
    private String password;
    private boolean remember;
    @Inject
    private AdminConfig adminConfig;
    @Inject
    private UserService userService;
    private Map<String,Receiver> receivers;
    private List<String> messages;

    public LogonMB() {
        receivers = new HashMap<>();
        messages = new ArrayList<>();
    }

    public void login() throws IOException {
        currentUser = userService.createUser(name);
        Utils.addDetailMessage("Logged in successfully as <b>" + name + "</b>");
        Faces.getExternalContext().getFlash().setKeepMessages(true);
        receiveMessages(name);

        Faces.redirect(adminConfig.getIndexPage());
    }

    public void receiveMessages(String queue) {
        try {
            Receiver receiver = new Receiver();
            receiver.receiveMessages(queue, this);
            receivers.put(queue, receiver);
        } catch (JMSException | InterruptedException e) {
            Messages.create("Danger").detail(e.getMessage()).add();
        }
    }

    public void stopListening(String queue) {
        Receiver receiver = receivers.get(queue);
        try {
            receiver.stopListening();
        } catch (JMSException e) {
            Messages.create("Danger").detail(e.getMessage()).add();
        }
        receivers.remove(queue, receiver);
    }

    public void doLogout() throws IOException {
        stopListening(currentUser.getName());
        userService.deleteUser(currentUser);
        currentUser = null;
    }

    @Override
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void handleMessage(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        //List<String> messages = new ArrayList<>(this.messages);
        //this.messages.clear();
        return messages;
    }
}
