package dataaccess;

import model.UserData;

import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    public Map<String, UserData> data;

    public void clearAllEntries() {
        data.clear();
    }

    public void createUser(UserData userData) {
        data.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return data.get(username);
    }

}
