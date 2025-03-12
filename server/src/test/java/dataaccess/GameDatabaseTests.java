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
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var stmt = conn.prepareStatement("insert into game (gameID, gameData) values (")) {
//
//            }
//        } catch (DataAccessException e) {
//            throw new RuntimeException("Error: Adding auth data to database failed");
//        }
        // here I will need to do the translation thing with json....
    }
    @Test
    public void testCreateGameNegative() {

    }
    @Test
    public void testGetGamePositive() {

    }
    @Test
    public void testGetGameNegative() {

    }
    @Test
    public void testGameNotAuthorizedPositive() {

    }
    @Test
    public void testGameNotAuthorizedNegative() {

    }
    @Test
    public void testColorNotAvailablePositive() {

    }
    @Test
    public void testColorNotAvailableNegative() {

    }
    @Test
    public void testUpdateGamePositive() {

    }
    @Test
    public void testUpdateGameNegative() {

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
