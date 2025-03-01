package service;
import chess.ChessGame;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.CreateGameRequest;
import request.JoinGameRequest;

import java.util.ArrayList;
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
        int gameID = 1234;

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

        JoinGameRequest req = new JoinGameRequest("32", ChessGame.TeamColor.WHITE, gameID);
        service.joinGame(req);
        // we assume that if it didn't throw an error it worked correctly.
    }
    @Test
    public void testJoin_Negative_badData() {
        int gameID = 1234;

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

        JoinGameRequest req = new JoinGameRequest("32",null, gameID);
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("bad data", ex.getMessage());
    }
    @Test
    public void testJoin_Negative_unauthorizedAuthData() {
        String expectedAuthToken = "32";
        String testAuthToken = "31";
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(gameID,"", "johnny","mychessgame", new ChessGame());
        gameMap.put(gameID, gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(testAuthToken, ChessGame.TeamColor.WHITE, gameID);
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("unauthorized auth data", ex.getMessage());
    }
    @Test
    public void testJoin_Negative_unauthorizedGameID() {
        int expectedGameID = 1234;
        int testGameID = 4221;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(expectedGameID,"", "johnny","mychessgame", new ChessGame());
        gameMap.put(expectedGameID, gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest("32", ChessGame.TeamColor.WHITE, testGameID);
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("unauthorized game id", ex.getMessage());
    }
    @Test
    public void testJoin_Negative_alreadyTaken() {
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(gameID,"eli", "","mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest("32", ChessGame.TeamColor.WHITE, gameID);
        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.joinGame(req));
        Assertions.assertEquals("color already taken", ex.getMessage());
    }
    @Test

    public void testList_Positive() {
        String authToken = "32";
        int gameID1 = 1234;
        int gameID2 = 1244;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", authToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData1 = new GameData(gameID1, "eli", "johnny", "mychessgame", new ChessGame());
        GameData gameData2 = new GameData(gameID2, "johnny", "eli", "mychessgame13", new ChessGame());

        ArrayList<GameMetaData> expectedGames = new ArrayList<>();
        expectedGames.add(new GameMetaData(gameData1));
        expectedGames.add(new GameMetaData(gameData2));

        gameMap.put(gameID1, gameData1);
        gameMap.put(gameID2, gameData2);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        ArrayList<GameMetaData> actualGames = service.listGames(authToken);
        Assertions.assertEquals(expectedGames, actualGames);
    }
    @Test
    public void testList_Negative_unauthorizedAuthData() {
        String expectedAuthToken = "32";
        String testAuthToken = "31";
        int gameID1 = 1234;
        int gameID2 = 1244;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData1 = new GameData(gameID1, "eli", "johnny", "mychessgame", new ChessGame());
        GameData gameData2 = new GameData(gameID2, "johnny", "eli", "mychessgame13", new ChessGame());

        gameMap.put(gameID1, gameData1);
        gameMap.put(gameID2, gameData2);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        Exception ex = Assertions.assertThrows(ServiceException.class, () -> service.listGames(testAuthToken));
        Assertions.assertEquals("unauthorized auth data", ex.getMessage());
    }
}
