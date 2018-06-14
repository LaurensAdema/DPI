package ma.ade.dpi.client.bean;

import ma.ade.dpi.client.domain.User;
import ma.ade.dpi.client.service.UserService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static ma.ade.dpi.client.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;


@Named
@ViewScoped
public class UserFormMB implements Serializable {


    private Integer id;
    private User user;


    @Inject
    UserService userService;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            user = userService.findById(id);
        } else {
            user = new User();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void remove() throws IOException {
        if (has(user) && has(user.getId())) {
            userService.deleteUser(user);
            addDetailMessage("User " + user.getName()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("user-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (user.getId() == 0) {
            userService.createUser(user);
            msg = "User " + user.getName() + " created successfully";
        } else {
            userService.updateUser(user);
            msg = "User " + user.getName() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        user = new User();
        id = null;
    }

    public boolean isNew() {
        return user == null || user.getId() == 0;
    }


}