package ma.ade.dpi.client.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable, Comparable<Group> {
    private int id;
    private String name;
    private List<User> teachers;
    private List<User> users;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
        this.teachers = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public Group() {
        this.teachers = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<User> teachers) {
        this.teachers = teachers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public boolean hasName() {
        return name != null && !"".equals(name.trim());
    }

    @Override
    public int compareTo(Group o) {
        int cmp;
        if (id > o.getId())
            cmp = +1;
        else if (id < o.getId())
            cmp = -1;
        else
            cmp = 0;
        return cmp;
    }

    public void addAsTeacher(User user) {
        teachers.add(user);
    }

    public void addUserAsStudent(User user) {
        users.add(user);
    }

    public void removeAsTeacher(User user) {
        teachers.removeIf(x -> x.getId() == user.getId());
    }

    public void removeUserAsStudent(User user) {
        users.removeIf(x -> x.getId() == user.getId());
    }
}
