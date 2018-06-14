package ma.ade.dpi.client.bean;

import ma.ade.dpi.client.domain.Group;
import ma.ade.dpi.client.domain.User;
import ma.ade.dpi.client.jms.Sender;
import ma.ade.dpi.client.model.Filter;
import ma.ade.dpi.client.service.UserService;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.jms.JMSException;
import java.util.Map;

import static ma.ade.dpi.client.util.Utils.addDetailMessage;


@Named
@ViewScoped
public class UserListMB implements Serializable {

    @Inject
    UserService userService;

    Integer id;

    LazyDataModel<User> users;

    Filter<User> filter = new Filter<>(new User());

    List<User> selectedUsers; //users selected in checkbox column

    List<User> filteredValue;// datatable filteredValue attribute (column filters)

    @PostConstruct
    public void initDataModel() {
        users = new LazyDataModel<User>() {
            @Override
            public List<User> load(int first, int pageSize,
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
                List<User> list = userService.paginate(filter);
                setRowCount((int) userService.count(filter));
                return list;
            }

            @Override
            public int getRowCount() {
                return super.getRowCount();
            }

            @Override
            public User getRowData(String key) {
                return userService.findById(new Integer(key));
            }
        };
    }

    public void clear() {
        filter = new Filter<User>(new User());
    }

    public void findUserById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide User ID to load");
        }
        selectedUsers.add(userService.findById(id));
    }

    public void delete() {
        int numUsers = 0;
        for (User selectedUser : selectedUsers) {
            numUsers++;
            userService.deleteUser(selectedUser);
        }
        selectedUsers.clear();
        addDetailMessage(numUsers + " users deleted successfully!");
    }

    public void sendMessage() {
        int numUsers = 0;
        for (User selectedUser : selectedUsers) {
            numUsers++;
            try {
                Sender sender = new Sender(selectedUser.getName());
                sender.sendMessage("Test User " + numUsers);
                sender.shutdown();
            } catch (JMSException e) {
                Messages.create("Danger").detail(e.getMessage()).add();
            }
        }
        selectedUsers.clear();
        addDetailMessage(numUsers + " users were send messages to");
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public List<User> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<User> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public LazyDataModel<User> getUsers() {
        return users;
    }

    public void setUsers(LazyDataModel<User> users) {
        this.users = users;
    }

    public Filter<User> getFilter() {
        return filter;
    }

    public void setFilter(Filter<User> filter) {
        this.filter = filter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}