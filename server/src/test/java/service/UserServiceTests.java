package service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.RegisterRequest;
import result.RegisterResult;

public class UserServiceTests {
    @Test
    public void testRegister_Positive() {
        UserService service = new UserService();
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");
        RegisterResult res = service.register(req);
        Assertions.assertEquals(req.userName(), res.username());
        Assertions.assertNotNull(res.authToken());
    }
    @Test
    public void testRegister_Negative_userAlreadyRegistered() {
        UserService service = new UserService();
        RegisterRequest req = new RegisterRequest("eli", "ile", "eli@ile.com");
        service.register(req);
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.register(req));
        Assertions.assertEquals("the user is already registered", ex.getMessage());
    }

//    public void testRegister_Negative_badData() {}

    @Test
    public void testLogin_Positive() {

    }
    @Test
    public void testLogin_Negative() {

    }
    @Test
    public void testLogout_Positive() {

    }
    @Test
    public void testLogout_Negative() {

    }
}
