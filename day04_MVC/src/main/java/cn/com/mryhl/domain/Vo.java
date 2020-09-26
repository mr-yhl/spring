package cn.com.mryhl.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vo  {
    private List<User> users = new ArrayList<User>();
    private Map<String,String> map = new HashMap<String, String>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Vo{" +
                "users=" + users +
                ", map=" + map +
                '}';
    }
}
