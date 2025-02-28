package service;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

import java.util.HashMap;

public class UserServiceTests {
    @Test
    public void testRegister_Positive() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");
        LoginResult res = service.register(req);
        Assertions.assertEquals(req.username(), res.username());
        Assertions.assertNotNull(res.authToken());
    }
    @Test
    public void testRegister_Negative_userAlreadyRegistered() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");
        service.register(req);
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.register(req));
        Assertions.assertEquals("the user is already registered", ex.getMessage());
    }
    @Test
    public void testRegister_Negative_badData() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("", "ile", "eli@ile.com");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.register(req));
        Assertions.assertEquals("bad data", ex.getMessage());
    }

    @Test
    public void testLogin_Positive() {
        HashMap<String, UserData> map = new HashMap<>();
        UserData userData = new UserData("eli", "ile", "eli@ile.com");
        map.put(userData.username(), userData);
        MemoryUserDAO dao = new MemoryUserDAO(map, null);
        UserService service = new UserService(dao);

        LoginRequest req = new LoginRequest(userData.username(), userData.password());
        LoginResult res = service.login(req);
        Assertions.assertEquals(req.username(), res.username());
        Assertions.assertNotNull(res.authToken());
    }
    @Test
    public void testLogin_Negative_unauthorized() {
        UserData userData = new UserData("eli", "ile", "eli@ile.com");
        UserService service = new UserService(null);

        LoginRequest req = new LoginRequest(userData.username(), userData.password());
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.login(req));
        Assertions.assertEquals("this user is not registered", ex.getMessage());
    }

    @Test
    public void testLogout_Positive() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map);
        UserService service = new UserService(dao);

        service.logout(authData.authToken());
        Assertions.assertTrue(dao.isAuthDataMapEmpty());
    }
    @Test
    public void testLogout_Negative_unauthorized() {
        String expectedAuthToken = "32";
        String testAuthToken = "33";
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        map.put(expectedAuthToken, authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map);
        UserService service = new UserService(dao);

        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.logout(testAuthToken));
        Assertions.assertEquals("unauthorized auth data", ex.getMessage());
    }
}
