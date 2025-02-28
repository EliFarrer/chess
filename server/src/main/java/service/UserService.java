package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

import java.util.Objects;

public class UserService {
    private final UserDAO dataAccess;

    public UserService(UserDAO dao) {
        this.dataAccess = Objects.requireNonNullElseGet(dao, () -> new MemoryUserDAO(null, null));
    }

    public LoginResult register(RegisterRequest req) throws ServiceException {
        try {
            // check to see if the user is already registered
            UserData testUserData = dataAccess.getUser(req.username());
            if (testUserData != null) {
                throw new ServiceException("the user is already registered");
            }

            // 400 error, the data is bad
            if (req.username().isEmpty() || req.password().isEmpty() || req.email().isEmpty()) {
                throw new ServiceException("bad data");
            }

            // create the new user
            UserData userData = new UserData(req.username(), req.password(), req.email());
            dataAccess.createUser(userData);
            // get the authentication
            AuthData authData = dataAccess.createAuth(userData.username());
            // return the username and authToken.
            return new LoginResult(authData.username(), authData.authToken());
        } catch (DataAccessException e) {
            // this is the case where the database is broken.
            throw new ServiceException("Data access exception: " + e);
        }
    }
    public LoginResult login(LoginRequest req) {
        try {
            UserData userData = dataAccess.getUser(req.username());

            // if the user is not registered
            if (userData == null) {
                throw new ServiceException("this user is not registered");
            }

            // the user is registered, so create auth and return LoginResult
            AuthData authData = dataAccess.createAuth(userData.username());
            return new LoginResult(authData.username(), authData.authToken());
        } catch (DataAccessException e) {
            throw new ServiceException("Data access exception: " + e);
        }
    }
    public void logout() {}
}
