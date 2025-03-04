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
        this.dataAccess = Objects.requireNonNullElseGet(dao, () -> new MemoryUserDAO(null, null, null));
    }

    public LoginResult register(RegisterRequest req) throws BadRequestException, DataAccessException, AlreadyTakenException {
        try {
            if (req.username() == null || req.password() == null || req.email() == null) {
                throw new BadRequestException("Error: bad request");
            }
            // 400 error, the data is bad
            if (req.username().isEmpty() || req.password().isEmpty() || req.email().isEmpty()) {
                throw new BadRequestException("Error: bad request");
            }

            // check to see if the user is already registered
            UserData testUserData = dataAccess.getUser(req.username());
            if (testUserData != null) {
                throw new AlreadyTakenException("Error: the user is already registered");
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
            throw new DataAccessException(e.getMessage());
        }
    }
    public LoginResult login(LoginRequest req) throws BadRequestException, DataAccessException, UnauthorizedException {
        try {
            UserData userData = dataAccess.getUser(req.username());
            // if the user is not registered
            if (userData == null) {
                throw new UnauthorizedException("Error: this user is not registered");
            }

            // if bad password
            if (!Objects.equals(req.password(), userData.password())) {
                throw new UnauthorizedException("Error: incorrect password");
            }

            // the user is registered, so create auth and return LoginResult
            AuthData authData = dataAccess.createAuth(userData.username());
            return new LoginResult(authData.username(), authData.authToken());
        } catch (DataAccessException e) {   // 500 error
            throw new DataAccessException(e.getMessage());
        }
    }
    public void logout(String authToken) throws BadRequestException, DataAccessException{
        try {
            if (dataAccess.isNotAuthorized(authToken))  {   // if we don't have access
                throw new UnauthorizedException("Error: unauthorized auth data");
            }

            dataAccess.removeAuth(authToken);
        } catch (DataAccessException e) {    // 500 error
            throw new DataAccessException(e.getMessage());
        }
    }
}
