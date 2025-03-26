package service;
import chess.ChessGame;
import dataaccess.MemoryDAO;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;

import java.util.ArrayList;
import java.util.HashMap;

public class GameServiceTests {
    @Test
    public void testCreatePositive() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryDAO dao = new MemoryDAO(null, map, null);
        GameService service = new GameService(dao);

        CreateGameRequest req = new CreateGameRequest("gameName");
        Assertions.assertDoesNotThrow(() -> {
            CreateGameResult gameID = service.createGame("32", req);
            Assertions.assertNotNull(gameID);
        });
    }
    @Test
    public void testCreateNegativeBadData() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryDAO dao = new MemoryDAO(null, map, null);
        GameService service = new GameService(dao);

        CreateGameRequest req = new CreateGameRequest("gameName");
        Exception ex = Assertions.assertThrows(BadRequestException.class, () -> service.createGame("", req));
        Assertions.assertEquals("Error: bad request", ex.getMessage());
    }
    @Test
    public void testCreateNegativeUnauthorized() {
        HashMap<String, String> map = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        map.put(authData.authToken(), authData.username());
        MemoryDAO dao = new MemoryDAO(null, map, null);
        GameService service = new GameService(dao);

        CreateGameRequest req = new CreateGameRequest("gameName");
        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.createGame("33", req));
        Assertions.assertEquals("Error: unauthorized auth data", ex.getMessage());
    }

    @Test
    public void testJoinPositive() {
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,null, "johnny","mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);

        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);

        Assertions.assertDoesNotThrow(() -> service.joinGame("32", req)
        );
        // we assume that if it didn't throw an error it worked correctly.
    }
    @Test
    public void testJoinNegativeBadData() {
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(1234,null, "johnny","mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);

        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(null, gameID);
        Exception ex = Assertions.assertThrows(BadRequestException.class, () -> service.joinGame("32", req));
        Assertions.assertEquals("Error: bad request", ex.getMessage());
    }
    @Test
    public void testJoinNegativeUnauthorizedAuthData() {
        String expectedAuthToken = "32";
        String testAuthToken = "31";
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", expectedAuthToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(gameID,null, "johnny","mychessgame", new ChessGame());
        gameMap.put(gameID, gameData);
        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.joinGame(testAuthToken, req));
        Assertions.assertEquals("Error: unauthorized auth data", ex.getMessage());
    }
    @Test
    public void testJoinNegativeUnauthorizedGameID() {
        int expectedGameID = 1234;
        int testGameID = 4221;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(expectedGameID,null, "johnny","mychessgame", new ChessGame());
        gameMap.put(expectedGameID, gameData);
        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(ChessGame.TeamColor.WHITE, testGameID);
        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.joinGame("32", req));
        Assertions.assertEquals("Error: unauthorized game id", ex.getMessage());
    }
    @Test
    public void testJoinNegativeAlreadyTaken() {
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", "32");
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(gameID,"eli", null,"mychessgame", new ChessGame());
        gameMap.put(gameData.gameID(), gameData);
        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        JoinGameRequest req = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        Exception ex = Assertions.assertThrows(AlreadyTakenException.class, () -> service.joinGame("32", req));
        Assertions.assertEquals("Error: color already taken", ex.getMessage());
    }
    @Test

    public void testListPositive() {
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

        ArrayList<GameData> expectedGames = new ArrayList<>();
        expectedGames.add(gameData1);
        expectedGames.add(gameData2);

        gameMap.put(gameID1, gameData1);
        gameMap.put(gameID2, gameData2);
        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        Assertions.assertDoesNotThrow(() -> {
            ArrayList<GameData> actualGames = service.listGames(authToken);
            Assertions.assertEquals(expectedGames, actualGames);
        });

    }
    @Test
    public void testListNegativeUnauthorizedAuthData() {
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
        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        GameService service = new GameService(dao);

        Exception ex = Assertions.assertThrows(UnauthorizedException.class, () -> service.listGames(testAuthToken));
        Assertions.assertEquals("Error: unauthorized auth data", ex.getMessage());
    }

}
