package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    // in here are all the declarations of the methods we need to run eventually like clear and add and stuff like that
    void clearAllEntries() throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    AuthData createAuth(String userName);

    boolean isAuthDataMapEmpty() throws DataAccessException;

    boolean isAuthorized(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;
}