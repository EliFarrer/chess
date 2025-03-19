package client;

import chess.ChessGame;
import dataaccess.DatabaseManager;
import model.GameData;
import model.GameMetaData;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.LoginResult;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.lang.module.ResolutionException;
import java.util.ArrayList;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        facade.clear();
    }

    @Test
    void clearPositive() {
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    void registerPositive() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertTrue(authData.authToken().length() > 10);
    }
    @Test
    void registerNegativeBadDataNull() {
        Assertions.assertThrows(ResponseException.class, () -> {
            var authData = facade.register(new RegisterRequest(null, "password", "p1@email.com"));
        });
    }
    @Test
    void registerNegativeBadDataEmptyString() {
        Assertions.assertThrows(ResponseException.class, () -> {
            var authData = facade.register(new RegisterRequest("", "password", "p1@email.com"));
        });
    }
    @Test
    void registerNegativeAlreadyTaken() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        });
    }

    @Test
    void logoutPositive() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }
    @Test
    void logoutNegativeUnauthorized() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout("randomString"));
    }

    @Test
    void loginPositive() {
        // register new user
        String username = "user";
        String password = "password";
        var authData = facade.register(new RegisterRequest(username, password, "p1@email.com"));

        // log them out
        facade.logout(authData.authToken());

        Assertions.assertDoesNotThrow(() -> facade.login(new LoginRequest(username, password)));
    }
    @Test
    void loginNegativeUnauthorized() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login(new LoginRequest("user", "password"));
        });
    }

    @Test
    void createGamePositive() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertDoesNotThrow(() -> {
            CreateGameResult id = facade.createGame(authData.authToken(), new CreateGameRequest("myGame"));
            Assertions.assertNotNull(id.gameID());
        });
    }
    @Test
    void createGameNegativeUnauthorized() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(null, new CreateGameRequest("myGame")));
    }
    @Test
    void createGameNegativeBadDataNull() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(authData.authToken(), new CreateGameRequest(null)));
    }
    @Test
    void createGameNegativeBadDataEmptyString() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(authData.authToken(), new CreateGameRequest("")));
    }

    @Test
    void listGamesPositive() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        String game1Name = "game1";
        String game2Name = "game2";
        String game3Name = "game3";
        CreateGameResult game1ID = facade.createGame(authData.authToken(), new CreateGameRequest(game1Name));
        CreateGameResult game2ID = facade.createGame(authData.authToken(), new CreateGameRequest(game2Name));
        CreateGameResult game3ID = facade.createGame(authData.authToken(), new CreateGameRequest(game3Name));
        ArrayList<GameMetaData> games = facade.listGames(authData.authToken()).games();
        Assertions.assertEquals(3, games.size());
        Assertions.assertEquals(new GameMetaData(game1ID.gameID(), null, null, game1Name), games.get(0));
        Assertions.assertEquals(new GameMetaData(game2ID.gameID(), null, null, game2Name), games.get(1));
        Assertions.assertEquals(new GameMetaData(game3ID.gameID(), null, null, game3Name), games.get(2));

    }
    @Test
    void listGamesNegativeUnauthorized() {
        var authData = facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        String game1Name = "game1";
        String game2Name = "game2";
        String game3Name = "game3";
        facade.createGame(authData.authToken(), new CreateGameRequest(game1Name));
        facade.createGame(authData.authToken(), new CreateGameRequest(game2Name));
        facade.createGame(authData.authToken(), new CreateGameRequest(game3Name));
        facade.logout(authData.authToken());
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(authData.authToken()).games());
    }

    @Test
    void joinGamePositive() {

    }
    @Test
    void joinGameNegativeUnauthorized() {

    }
    @Test
    void joinGameNegativeBadDataNull() {

    }
    @Test
    void joinGameNegativeBadDataEmptyString() {

    }
    @Test
    void joinGameNegativeAlreadyTaken() {

    }

    @AfterEach
    void clear() {
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }
}
