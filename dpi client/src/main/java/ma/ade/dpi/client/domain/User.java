package ma.ade.dpi.client.domain;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {

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
        User other = (User) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public boolean hasName() {
        return name != null && !"".equals(name.trim());
    }

    @Override
    public int compareTo(User o) {
        int cmp;
        if (id > o.getId())
            cmp = +1;
        else if (id < o.getId())
            cmp = -1;
        else
            cmp = 0;
        return cmp;
    }
}
