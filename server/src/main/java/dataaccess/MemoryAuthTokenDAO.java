package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthTokenDAO implements AuthTokenDAO {
    public Map<String, String> authDataMap; // maps from authToken to username

    public MemoryAuthTokenDAO(Map<String, String> authMap) {
        this.authDataMap = Objects.requireNonNullElseGet(authMap, HashMap::new);
    }

    public AuthData createAuth(String userName) {
        String id = UUID.randomUUID().toString();
        authDataMap.put(id, userName);
        return new AuthData(userName, id);
    }

    public boolean isAuthDataMapEmpty() {
        return authDataMap.isEmpty();
    }

    public boolean isAuthorized(String authToken) throws DataAccessException {
        // if the authToken is in the database, we return true because it is authorized
        return (authDataMap.get(authToken) != null);
    }

    public void removeAuth(String authToken) throws DataAccessException {
        authDataMap.remove(authToken);
    }
}
