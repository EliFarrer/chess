package service;
import chess.ChessGame;
import dataaccess.MemoryDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


public class ClearServiceTests {
    @Test
    public void testClearPositive() {
        String authToken = "32";
        int gameID = 1234;

        // authorization map
        HashMap<String, String> authMap = new HashMap<>();
        AuthData authData = new AuthData("eli", authToken);
        authMap.put(authData.authToken(), authData.username());

        // gameMap
        HashMap<Integer, GameData> gameMap = new HashMap<>();
        GameData gameData = new GameData(gameID, "eli", "johnny", "myChessGame", new ChessGame());

        gameMap.put(gameID, gameData);
        MemoryDAO dao = new MemoryDAO(null, authMap, gameMap);
        ClearService service = new ClearService(dao);

        Assertions.assertDoesNotThrow(service::clearGame);
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(dao.isEmpty()));
    }

    // don't need a negative testClear test case
}