package service;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


public class ClearServiceTests {
    @Test
    public void testClear_Positive() {
        String authToken = "32";
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", authToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(gameID, "eli", "johnny", "mychessgame", new ChessGame());

        gameMap.put(gameID, gameData);
        MemoryUserDAO dao = new MemoryUserDAO(null, authMap, gameMap);
        ClearService service = new ClearService(dao);

        Assertions.assertDoesNotThrow(service::clearGame);
        Assertions.assertTrue(dao.isEmpty());

    }

    // don't need a negative testClear test case
}