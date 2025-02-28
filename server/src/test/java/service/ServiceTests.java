package service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.Objects;

public class ServiceTests {
    @Test
    public void testClear_Positive() {
//        MemoryUserDAO dao = new MemoryUserDAO();
//        UserData data = new UserData("eli", "ile", "eli@ile.com");
//        dao.createUser(data);
        ClearService service = new ClearService(null);
        ClearResult rtrn = service.clearGame();
        assert(rtrn.message().isEmpty());
    }

    // don't need a negative testClear test case

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
    @Test
    public void testCreate_Positive() {

    }
    @Test
    public void testCreate_Negative() {

    }
    @Test
    public void testJoin_Positive() {

    }
    @Test
    public void testJoin_Negative() {

    }
    @Test
    public void testList_Positive() {

    }
    @Test
    public void testList_Negative() {

    }

}