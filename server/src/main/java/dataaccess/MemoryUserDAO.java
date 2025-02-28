package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    public Map<String, UserData> data;

    public void clearAllEntries() throws DataAccessException {
        data.clear();
    }

    public void createUser(UserData userData) throws DataAccessException {
        data.put(userData.username(), userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        return data.get(username);
    }
}
