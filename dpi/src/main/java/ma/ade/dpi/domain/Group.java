package ma.ade.dpi.domain;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<User> teachers;
    private List<User> users;

    public Group(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
        this.users = new ArrayList<>();
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
}
