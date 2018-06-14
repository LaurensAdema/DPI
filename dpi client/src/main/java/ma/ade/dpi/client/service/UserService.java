package ma.ade.dpi.client.service;

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
public class UserService implements Serializable {
    Collection<User> allUsers = new ArrayList<>();
    int curId = 0;

    public User createUser(String name) {
        curId++;
        User user = new User(curId, name);
        allUsers.add(user);
        return user;
    }

    public User createUser(User user) {
        curId++;
        user.setId(curId);
        allUsers.add(user);
        return user;
    }

    public User updateUser(User user) {
        User cachedUser = findById(user.getId());
        cachedUser.setName(user.getName());
        return cachedUser;
    }

    public boolean deleteUser(User currentUser) {
        return allUsers.removeIf(x -> x.getId() == currentUser.getId());
    }

    public User findById(Integer id) {
        return allUsers.stream().filter(x -> x.getId() == id).findFirst().get();
    }

    public Collection<User> getAll() {
        return allUsers;
    }

    public List<User> paginate(Filter<User> filter) {
        List<User> pagedUsers = new ArrayList<>();
        if (has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
            pagedUsers = allUsers.stream().
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
            pagedUsers = pagedUsers.subList(filter.getFirst(), page > allUsers.size() ? allUsers.size() : page);
            return pagedUsers;
        }

        List<Predicate<User>> predicates = configFilter(filter);

        List<User> pagedList = getAll().stream().filter(predicates
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

    public long count(Filter<User> filter) {
        return getAll().stream()
                .filter(configFilter(filter).stream()
                        .reduce(Predicate::or).orElse(t -> true))
                .count();
    }

    private List<Predicate<User>> configFilter(Filter<User> filter) {
        List<Predicate<User>> predicates = new ArrayList<>();
        if (filter.hasParam("id")) {
            Predicate<User> idPredicate = (User c) -> Integer.toString(c.getId()).equals(filter.getParam("id").toString());
            predicates.add(idPredicate);
        }

        if (has(filter.getEntity())) {
            User filterEntity = filter.getEntity();

            if (has(filterEntity.getName())) {
                Predicate<User> namePredicate = (User c) -> c.getName().toLowerCase().contains(filterEntity.getName().toLowerCase());
                predicates.add(namePredicate);
            }
        }
        return predicates;
    }
}
