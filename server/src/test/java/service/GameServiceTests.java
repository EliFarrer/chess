package service;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.CreateGameRequest;

import java.util.HashMap;

public class GameServiceTests {
    @Test
    public void testCreate_Positive() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map, null);
        GameService service = new GameService(dao);

        CreateGameRequest req = new CreateGameRequest("32", "gameName");
        Integer gameID = service.createGame(req);
        Assertions.assertNotNull(gameID);
    }
    @Test
    public void testCreate_Negative_badData() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map, null);
        GameService service = new GameService(dao);

        CreateGameRequest req = new CreateGameRequest("", "gameName");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.createGame(req));
        Assertions.assertEquals("bad data", ex.getMessage());
    }
    @Test
    public void testCreate_Negative_unauthorized() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryUserDAO dao = new MemoryUserDAO(null, map, null);
        GameService service = new GameService(dao);

        CreateGameRequest req = new CreateGameRequest("33", "gameName");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.createGame(req));
        Assertions.assertEquals("unauthorized auth data", ex.getMessage());
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
