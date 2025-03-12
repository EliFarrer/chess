package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;

import java.util.ArrayList;
import java.util.HashMap;

public class GameDatabaseTests {
    @BeforeEach
    public void setup() {
        try {
            // clear the tables
            DatabaseManager.dropTables();
            // add the tables
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error: Creating or deleting tables failed.");
        }
    }

    @Test
    public void testCreateGamePositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(db.isGameDatabaseEmpty());
            db.createGame(id, "myChessGame");
            Assertions.assertFalse(db.isGameDatabaseEmpty());
        });
    }

    @Test
    public void testCreateGameNegative() {

    }
    @Test
    public void testGetGamePositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertNull(db.getGame(id));
            db.createGame(id, "myChessGame");
            Assertions.assertNotNull(db.getGame(id));
        });
    }
    @Test
    public void testGetGameNegative() {

    }
    @Test
    public void testGameNotAuthorizedPositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(db.gameNotAuthorized(id));
            db.createGame(id, "myChessGame");
            Assertions.assertFalse(db.gameNotAuthorized(id));
        });
    }

    @Test
    public void testGameNotAuthorizedNegative() {

    }

    @Test
    public void testUpdateGamePositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        String gameName = "myChessGame";
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(id, gameName);
            GameData expectedGameData = new GameData(id, "ile", "eli", gameName, new ChessGame());
            db.updateGame(id, expectedGameData);
            Assertions.assertEquals(expectedGameData, db.getGame(id));
        });
    }
    @Test
    public void testUpdateGameNegative() {

    }

    @Test
    public void testColorNotAvailablePositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        String gameName = "myChessGame";
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(id, gameName);
            db.updateGame(id, new GameData(id, "", "eli", gameName, new ChessGame()));
            Assertions.assertFalse(db.colorNotAvailable(id, ChessGame.TeamColor.WHITE));
            Assertions.assertTrue(db.colorNotAvailable(id, ChessGame.TeamColor.BLACK));
        });
    }

    @Test
    public void testColorNotAvailableNegative() {

    }

    @Test
    public void testListGamesPositive() {


    }
    @Test
    public void testListGamesNegative() {

    }
    @Test
    public void testIsGameDatabaseEmptyPositive() {

    }
    @Test
    public void testIsGameDatabaseEmptyNegative() {

    }

    @AfterAll
    public static void reset() {
        try {
            // clear the tables
            DatabaseManager.dropTables();
            // add the tables
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error: Creating or deleting tables failed.");
        }
    }
}
