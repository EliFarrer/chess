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

        Assertions.assertDoesNotThrow(() -> {
            LoginResult res = service.register(req);
            Assertions.assertEquals(req.username(), res.username());
            Assertions.assertNotNull(res.authToken());
        });
    }
    @Test
    public void testRegister_Negative_userAlreadyRegistered() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");

        // register a user
        Assertions.assertDoesNotThrow(() -> service.register(req));

        Exception ex = Assertions.assertThrows(AlreadyTakenException.class, () -> service.register(req));
        Assertions.assertEquals("Error: the user is already registered", ex.getMessage());
    }
    @Test
    public void testRegister_Negative_badData() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("", "ile", "eli@ile.com");
        Exception ex = Assertions.assertThrows(BadRequestException.class, () -> service.register(req));
        Assertions.assertEquals("Error: bad request", ex.getMessage());
    }

    @Test
    public void testLogin_Positive() {
        HashMap<String, UserData> map = new HashMap<>();
        UserData userData = new UserData("eli", "ile", "eli@ile.com");
        map.put(userData.username(), userData);
        MemoryUserDAO dao = new MemoryUserDAO(map, null, null);
        UserService service = new UserService(dao);

        LoginRequest req = new LoginRequest(userData.username(), userData.password());

        Assertions.assertDoesNotThrow(() -> {
            LoginResult res = service.login(req);
            Assertions.assertEquals(req.username(), res.username());
            Assertions.assertNotNull(res.authToken());
        });
    }
    @Test
    public void testLogin_Negative_unauthorized() {
        UserData userData = new UserData("eli", "ile", "eli@ile.com");
        UserService service = new UserService(null);

        LoginRequest req = new LoginRequest(userData.username(), userData.password());
        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.login(req));
        Assertions.assertEquals("Error: this user is not registered", ex.getMessage());
    }

    @Test
    public void testLogout_Positive() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map, null);
        UserService service = new UserService(dao);

        Assertions.assertDoesNotThrow(() -> {
            service.logout(authData.authToken());
            Assertions.assertTrue(dao.isAuthDataMapEmpty());
        });
    }
    @Test
    public void testLogout_Negative_unauthorized() {
        String expectedAuthToken = "32";
        String testAuthToken = "33";
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        map.put(expectedAuthToken, authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map, null);
        UserService service = new UserService(dao);

        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.logout(testAuthToken));
        Assertions.assertEquals("Error: unauthorized auth data", ex.getMessage());
    }
}
