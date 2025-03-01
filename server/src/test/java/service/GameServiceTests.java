package service;
import chess.ChessGame;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.CreateGameRequest;
import request.JoinGameRequest;

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
        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,"", "johnny","mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);

        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest("32", "WHITE", "gameName");
        service.joinGame(req);
        // we assume that if it didn't throw an error it worked correctly.
    }
    @Test
    public void testJoin_Negative_badData() {
        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,"", "johnny","mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);

        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest("32","", "gameName");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("bad data", ex.getMessage());
    }
    @Test
    public void testJoin_Negative_unauthorizedAuthData() {
        String expectedAuthToken = "32";
        String testAuthToken = "31";

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,"", "johnny","mychessgame", new ChessGame());
        gameMap.put(9248, gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(testAuthToken, "WHITE", "gameName");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("unauthorized auth data", ex.getMessage());
    }
    @Test
    public void testJoin_Negative_unauthorizedGameID() {
        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,"", "johnny","mychessgame", new ChessGame());
        gameMap.put(9248, gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest("32", "WHITE", "gameName");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("unauthorized game id", ex.getMessage());
    }
    @Test
    public void testJoin_Negative_alreadyTaken() {
        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,"eli", "","mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest("32", "WHITE", "gameName");
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("color already taken", ex.getMessage());
    }
    @Test
    public void testList_Positive() {

    }
    @Test
    public void testList_Negative() {

    }
}
