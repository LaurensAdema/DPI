package ma.ade.dpi.client.bean;

import com.github.adminfaces.template.exception.BusinessException;
import ma.ade.dpi.client.domain.Group;
import ma.ade.dpi.client.jms.Connector;
import ma.ade.dpi.client.jms.Sender;
import ma.ade.dpi.client.model.Filter;
import ma.ade.dpi.client.security.LogonMB;
import ma.ade.dpi.client.service.GroupService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static ma.ade.dpi.client.util.Utils.addDetailMessage;


@Named
@ViewScoped
public class GroupListMB implements Serializable {

    @Inject
    GroupService groupService;
    @Inject
    LogonMB logonMB;

    Integer id;

    LazyDataModel<Group> groups;

    Filter<Group> filter = new Filter<>(new Group());

    List<Group> selectedGroups; //groups selected in checkbox column

    List<Group> filteredValue;// datatable filteredValue attribute (column filters)

    @PostConstruct
    public void initDataModel() {
        groups = new LazyDataModel<Group>() {
            @Override
            public List<Group> load(int first, int pageSize,
                                   String sortField, SortOrder sortOrder,
                                   Map<String, Object> filters) {
                ma.ade.dpi.client.model.SortOrder order = null;
                if (sortOrder != null) {
                    order = sortOrder.equals(SortOrder.ASCENDING) ? ma.ade.dpi.client.model.SortOrder.ASCENDING
                            : sortOrder.equals(SortOrder.DESCENDING) ? ma.ade.dpi.client.model.SortOrder.DESCENDING
                            : ma.ade.dpi.client.model.SortOrder.UNSORTED;
                }
                filter.setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<Group> list = groupService.paginate(filter);
                setRowCount((int) groupService.count(filter));
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public Group getRowData(String key) {
                return groupService.findById(new Integer(key));
            }
        };
    }

    public void clear() {
        filter = new Filter<Group>(new Group());
    }

    public void findGroupById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Group ID to load");
        }
        selectedGroups.add(groupService.findById(id));
    }

    public void delete() {
        int numGroups = 0;
        for (Group selectedGroup : selectedGroups) {
            numGroups++;
            groupService.deleteGroup(selectedGroup);
        }
        selectedGroups.clear();
        addDetailMessage(numGroups + " groups deleted successfully!");
    }

    public void joinAsStudent() {
        int numGroups = 0;
        for (Group selectedGroup : selectedGroups) {
            numGroups++;
            groupService.joinGroupAsStudent(selectedGroup, logonMB.getCurrentUser());
            logonMB.receiveMessages(selectedGroup.getName());
        }
        selectedGroups.clear();
        addDetailMessage(numGroups + " groups joined as user succesfully!");
    }

    public void joinAsTeacher() {
        int numGroups = 0;
        for (Group selectedGroup : selectedGroups) {
            numGroups++;
            groupService.joinGroupAsTeacher(selectedGroup, logonMB.getCurrentUser());
            logonMB.receiveMessages(selectedGroup.getName());
        }
        selectedGroups.clear();
        addDetailMessage(numGroups + " groups joined as teacher succesfully!");
    }

    public void leaveAsStudent() {
        int numGroups = 0;
        for (Group selectedGroup : selectedGroups) {
            numGroups++;
            groupService.leaveGroupAsStudent(selectedGroup, logonMB.getCurrentUser());
            logonMB.stopListening(selectedGroup.getName());
        }
        selectedGroups.clear();
        addDetailMessage(numGroups + " groups left as user succesfully!");
    }

    public void leaveAsTeacher() {
        int numGroups = 0;
        for (Group selectedGroup : selectedGroups) {
            numGroups++;
            groupService.leaveGroupAsTeacher(selectedGroup, logonMB.getCurrentUser());
            logonMB.stopListening(selectedGroup.getName());
        }
        selectedGroups.clear();
        addDetailMessage(numGroups + " groups left as teacher succesfully!");
    }

    public void sendMessage() {
        int numGroups = 0;
        for (Group selectedGroup : selectedGroups) {
            numGroups++;
            try {
                Sender sender = new Sender(selectedGroup.getName());
                sender.sendMessage("Test Group " + numGroups);
                sender.shutdown();
            } catch (JMSException e) {
                Messages.create("Danger").detail(e.getMessage()).add();
            }
        }
        selectedGroups.clear();
        addDetailMessage(numGroups + " groups send messages ");
    }

    public List<Group> getSelectedGroups() {
        return selectedGroups;
    }

    public List<Group> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Group> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public void setSelectedGroups(List<Group> selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    public LazyDataModel<Group> getGroups() {
        return groups;
    }

    public void setGroups(LazyDataModel<Group> groups) {
        this.groups = groups;
    }

    public Filter<Group> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Group> filter) {
        this.filter = filter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}