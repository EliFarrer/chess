package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class ClearDatabaseTests {
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
    public void testClearPositive() {
        // we are expecting that every time we call one of these, the database has tables
//        String authToken = "32";
//        int gameID = 1234;
//
//        // authorization map
//        HashMap<String, String> authMap = new HashMap<>();
//        AuthData authData = new AuthData("eli", authToken);
//        authMap.put(authData.authToken(), authData.username());
//
//        // gameMap
//        HashMap<Integer, GameData> gameMap = new HashMap<>();
//        GameData gameData = new GameData(gameID, "eli", "johnny", "mychessgame", new ChessGame());
//
//        gameMap.put(gameID, gameData);
//        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
//        ClearService service = new ClearService(dao);
//
//        Assertions.assertDoesNotThrow(service::clearGame);
//        Assertions.assertDoesNotThrow(dao::isEmpty);


        // have a database that is not empty
        // assert it is not empty
        // call clear
        // assert is it now clear





    }

    // don't need a negative testClear test case

//    public void addToDatabase()
}
