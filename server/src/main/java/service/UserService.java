package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class UserService {
    private final UserDAO dataAccess;

    public UserService() {
        this.dataAccess = new MemoryUserDAO();
    }

    public RegisterResult register(RegisterRequest req) throws ServiceException {
        try {
            // check to see if the user is already registered
            UserData testUserData = dataAccess.getUser(req.userName());
            if (testUserData != null) {
                throw new ServiceException("the user is already registered");
            }

            // 400 error, the data is bad
            if (req.userName().isEmpty() || req.password().isEmpty() || req.email().isEmpty()) {
                throw new ServiceException("bad data");
            }

            // create the new user
            UserData userData = new UserData(req.userName(), req.password(), req.email());
            dataAccess.createUser(userData);
            // get the authentication
            AuthData authData = dataAccess.createAuth(userData.userName());
            // return the username and authToken.
            return new RegisterResult(authData.username(), authData.authToken());
        } catch (DataAccessException e) {
            // this is the case where the database is broken.
            throw new ServiceException("Data access exception: " + e);
        }
    }
    public void login() {}
    public void logout() {}
}
