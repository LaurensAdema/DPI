package ma.ade.dpi.client.bean;

import ma.ade.dpi.client.domain.Group;
import ma.ade.dpi.client.service.GroupService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.github.adminfaces.template.util.Assert.has;
import static ma.ade.dpi.client.util.Utils.addDetailMessage;


@Named
@ViewScoped
public class GroupFormMB implements Serializable {


    private Integer id;
    private Group group;


    @Inject
    GroupService groupService;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            group = groupService.findById(id);
        } else {
            group = new Group();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


    public void remove() throws IOException {
        if (has(group) && has(group.getId())) {
            groupService.deleteGroup(group);
            addDetailMessage("Group " + group.getName()
                    + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("group-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (group.getId() == 0) {
            groupService.createGroup(group);
            msg = "Group " + group.getName() + " created successfully";
        } else {
            groupService.updateGroup(group);
            msg = "Group " + group.getName() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        group = new Group();
        id = null;
    }

    public boolean isNew() {
        return group == null || group.getId() == 0;
    }


}