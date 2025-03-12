package service;
import dataaccess.MemoryDAO;
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
    public void testRegisterPositive() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");

        Assertions.assertDoesNotThrow(() -> {
            LoginResult res = service.register(req);
            Assertions.assertEquals(req.username(), res.username());
            Assertions.assertNotNull(res.authToken());
        });
    }
    @Test
    public void testRegisterNegativeUserAlreadyRegistered() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");

        // register a user
        Assertions.assertDoesNotThrow(() -> service.register(req));

        Exception ex = Assertions.assertThrows(AlreadyTakenException.class, () -> service.register(req));
        Assertions.assertEquals("Error: the user is already registered", ex.getMessage());
    }
    @Test
    public void testRegisterNegativeBadData() {
        UserService service = new UserService(null);
        RegisterRequest req = new RegisterRequest("", "ile", "eli@ile.com");
        Exception ex = Assertions.assertThrows(BadRequestException.class, () -> service.register(req));
        Assertions.assertEquals("Error: bad request", ex.getMessage());
    }

    @Test
    public void testLoginPositive() {
        HashMap<String, UserData> map = new HashMap<>();
        UserData userData = new UserData("eli", "ile", "eli@ile.com");
        map.put(userData.username(), userData);
        MemoryDAO dao = new MemoryDAO(map, null, null);
        UserService service = new UserService(dao);

        LoginRequest req = new LoginRequest(userData.username(), userData.password());

        Assertions.assertDoesNotThrow(() -> {
            LoginResult res = service.login(req);
            Assertions.assertEquals(req.username(), res.username());
            Assertions.assertNotNull(res.authToken());
        });
    }
    @Test
    public void testLoginNegativeUnauthorized() {
        UserData userData = new UserData("eli", "ile", "eli@ile.com");
        UserService service = new UserService(null);

        LoginRequest req = new LoginRequest(userData.username(), userData.password());
        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.login(req));
        Assertions.assertEquals("Error: this user is not registered", ex.getMessage());
    }

    @Test
    public void testLogoutPositive() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryDAO dao = new MemoryDAO(null, map, null);
        UserService service = new UserService(dao);

        Assertions.assertDoesNotThrow(() -> {
            service.logout(authData.authToken());
            Assertions.assertTrue(dao.isAuthDataMapEmpty());
        });
    }
    @Test
    public void testLogoutNegativeUnauthorized() {
        String expectedAuthToken = "32";
        String testAuthToken = "33";
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        map.put(expectedAuthToken, authData.username());
        MemoryDAO dao = new MemoryDAO(null, map, null);
        UserService service = new UserService(dao);

        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.logout(testAuthToken));
        Assertions.assertEquals("Error: unauthorized auth data", ex.getMessage());
    }
}
