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

import javax.xml.crypto.Data;
import java.sql.SQLException;
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
    public void testCreateGameNegativeBadData() {
        // look up something that doesn't exist....
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> {
            db.isGameDatabaseEmpty();
            db.createGame(id, null);
        });
        Assertions.assertThrows(DataAccessException.class, () -> db.createGame(id, "myChessGame"));
    }

    @Test
    public void testCreateGameNegativeDuplicate() {
        // look up something that doesn't exist....
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> {
            db.isGameDatabaseEmpty();
            db.createGame(id, "myChessGame");
        });
        Assertions.assertThrows(DataAccessException.class, () -> db.createGame(id, "myChessGame"));
    }

    @Test
    public void testGetGamePositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(id, "myChessGame");
            Assertions.assertNotNull(db.getGame(id));
        });
    }
    @Test
    public void testGetGameNegativeBadData() {
        // bad input
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> Assertions.assertNull(db.getGame(id)));
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
        // game not created
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(db.gameNotAuthorized(id)));
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
    public void testUpdateGameNegativeBadData() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        String gameName = "myChessGame";
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(id, gameName);
        });
        Assertions.assertThrows(DataAccessException.class, () -> db.updateGame(id, null));
    }

    @Test
    public void testColorNotAvailablePositive() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        String gameName = "myChessGame";
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(id, gameName);
            db.updateGame(id, new GameData(id, null, "eli", gameName, new ChessGame()));
            Assertions.assertFalse(db.colorNotAvailable(id, ChessGame.TeamColor.WHITE));
            Assertions.assertTrue(db.colorNotAvailable(id, ChessGame.TeamColor.BLACK));
        });
    }

    @Test
    public void testColorNotAvailableNegativeBadData() {
        DatabaseDAO db = new DatabaseDAO();
        int id = 1;
        String gameName = "myChessGame";
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(id, gameName);
            db.updateGame(id, new GameData(id, null, "eli", gameName, new ChessGame()));
        });
        Assertions.assertThrows(DataAccessException.class, () -> db.colorNotAvailable(id, null));
    }

    @Test
    public void testListGamesPositive() {
        DatabaseDAO db = new DatabaseDAO();
        ArrayList<GameMetaData> expectedGames = new ArrayList<>();
        expectedGames.add(new GameMetaData(1, "eli", "ile", "game1"));
        expectedGames.add(new GameMetaData(2, "peter", "peter", "game2"));
        expectedGames.add(new GameMetaData(3, "john", "john", "game3"));
        Assertions.assertDoesNotThrow(() -> {
            db.createGame(1, "game1");
            db.updateGame(1, new GameData(expectedGames.get(0)));
            db.createGame(2, "game2");
            db.updateGame(2, new GameData(expectedGames.get(1)));
            db.createGame(3, "game3");
            db.updateGame(3, new GameData(expectedGames.get(2)));
            ArrayList<GameMetaData> actualGames = db.listGames();
            Assertions.assertEquals(expectedGames, actualGames);
        });
    }
    @Test
    public void testListGamesNegative() {
        // no games created
        DatabaseDAO db = new DatabaseDAO();
        ArrayList<GameMetaData> expectedGames = new ArrayList<>();
        Assertions.assertDoesNotThrow(db::listGames);
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
