package dataaccess;

import model.AuthData;

public interface AuthTokenDAO {
    AuthData createAuth(String userName);

    boolean isAuthDataMapEmpty() throws DataAccessException;

    boolean isAuthorized(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;
}
