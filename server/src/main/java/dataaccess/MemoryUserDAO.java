package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.crypto.Data;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    public Map<String, UserData> data = new HashMap<>();

    public void clearAllEntries() throws DataAccessException {
        data.clear();
    }

    public void createUser(UserData userData) throws DataAccessException {
        data.put(userData.userName(), userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        return data.get(username);
    }

    public AuthData createAuth(String userName) {
        String id = UUID.randomUUID().toString();
        return new AuthData(userName, id);
    }

}
