package ma.ade.dpi.client.service;

import ma.ade.dpi.client.domain.Group;
import ma.ade.dpi.client.domain.User;
import ma.ade.dpi.client.model.Filter;
import org.primefaces.model.SortOrder;

import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
@ApplicationScoped
public class GroupService implements Serializable {
    Collection<Group> allGroups = new ArrayList<>();
    int curId = 0;

    public Group createGroup(String name) {
        curId++;
        Group group = new Group(curId, name);
        allGroups.add(group);
        return group;
    }

    public Group createGroup(Group group) {
        curId++;
        group.setId(curId);
        allGroups.add(group);
        return group;
    }

    public Group updateGroup(Group group) {
        Group cachedUser = findById(group.getId());
        cachedUser.setName(group.getName());
        return cachedUser;
    }

    public boolean deleteGroup(Group currentGroup) {
        return allGroups.removeIf(x -> x.getId() == currentGroup.getId());
    }

    public Group findById(Integer id) {
        return allGroups.stream().filter(x -> x.getId() == id).findFirst().get();
    }

    public Collection<Group> getAll() {
        return allGroups;
    }

    public List<Group> paginate(Filter<Group> filter) {
        List<Group> pagedGroups = new ArrayList<>();
        if (has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedGroups = allGroups.stream().
                    sorted((c1, c2) -> {
                        if (filter.getSortOrder().isAscending()) {
                            return c1.compareTo(c2);
                        } else {
                            return c2.compareTo(c1);
                        }
                    })
                    .collect(Collectors.toList());
        }

        int page = filter.getFirst() + filter.getPageSize();
        if (filter.getParams().isEmpty()) {
            pagedGroups = pagedGroups.subList(filter.getFirst(), page > allGroups.size() ? allGroups.size() : page);
            return pagedGroups;
        }

        List<Predicate<Group>> predicates = configFilter(filter);

        List<Group> pagedList = getAll().stream().filter(predicates
                .stream().reduce(Predicate::or).orElse(t -> true))
                .collect(Collectors.toList());

        if (page < pagedList.size()) {
            pagedList = pagedList.subList(filter.getFirst(), page);
        }

        if (has(filter.getSortField())) {
            pagedList = pagedList.stream().
                    sorted((c1, c2) -> {
                        boolean asc = SortOrder.ASCENDING.equals(filter.getSortOrder());
                        if (asc) {
                            return c1.compareTo(c2);
                        } else {
                            return c2.compareTo(c1);
                        }
                    })
                    .collect(Collectors.toList());
        }
        return pagedList;
    }

    public long count(Filter<Group> filter) {
        return getAll().stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }

    private List<Predicate<Group>> configFilter(Filter<Group> filter) {
        List<Predicate<Group>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<Group> idPredicate = (Group c) -> Integer.toString(c.getId()).equals(filter.getParam("id").toString());
            predicates.add(idPredicate);
        }

        if (has(filter.getEntity())) {
            Group filterEntity = filter.getEntity();

            if (has(filterEntity.getName())) {
                Predicate<Group> namePredicate = (Group c) -> c.getName().toLowerCase().contains(filterEntity.getName().toLowerCase());
                predicates.add(namePredicate);
            }
        }
        return predicates;
    }

    public void joinGroupAsStudent(Group selectedGroup, User user) {
        findById(selectedGroup.getId()).addUserAsStudent(user);
    }

    public void joinGroupAsTeacher(Group selectedGroup, User user) {
        findById(selectedGroup.getId()).addAsTeacher(user);
    }

    public void leaveGroupAsStudent(Group selectedGroup, User user) {
        findById(selectedGroup.getId()).removeUserAsStudent(user);
    }

    public void leaveGroupAsTeacher(Group selectedGroup, User user) {
        findById(selectedGroup.getId()).removeAsTeacher(user);
    }
}
