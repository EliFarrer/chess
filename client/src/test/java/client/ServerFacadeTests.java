package client;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import server.ResponseException;
import server.Server;
import ui.ServerFacade;
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
        Assertions.assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest(null, "password", "p1@email.com")));
    }
    @Test
    void registerNegativeBadDataEmptyString() {
        Assertions.assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest("", "password", "p1@email.com")));
    }
    @Test
    void registerNegativeAlreadyTaken() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest("user", "password", "p1@email.com")));
    }

    @Test
    void logoutPositive() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }
    @Test
    void logoutNegativeUnauthorized() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    void loginPositive() {
        // register new user
        String username = "user";
        String password = "password";
        facade.register(new RegisterRequest(username, password, "p1@email.com"));

        // log them out
        facade.logout();

        Assertions.assertDoesNotThrow(() -> facade.login(new LoginRequest(username, password)));
    }
    @Test
    void loginNegativeUnauthorized() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("user", "password")));
    }

    @Test
    void createGamePositive() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertDoesNotThrow(() -> {
            CreateGameResult id = facade.createGame(new CreateGameRequest("myGame"));
            Assertions.assertNotNull(id.gameID());
        });
    }
    @Test
    void createGameNegativeUnauthorized() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(new CreateGameRequest("myGame")));
    }
    @Test
    void createGameNegativeBadDataNull() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(null));
    }
    @Test
    void createGameNegativeBadDataEmptyString() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(new CreateGameRequest("")));
    }

    @Test
    void listGamesPositive() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        String game1Name = "game1";
        String game2Name = "game2";
        String game3Name = "game3";
        facade.createGame(new CreateGameRequest(game1Name));
        facade.createGame(new CreateGameRequest(game2Name));
        facade.createGame(new CreateGameRequest(game3Name));
        ArrayList<GameData> games = facade.listGames().games();
        Assertions.assertEquals(3, games.size());
    }
    @Test
    void listGamesNegativeUnauthorized() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        String game1Name = "game1";
        String game2Name = "game2";
        String game3Name = "game3";
        facade.createGame(new CreateGameRequest(game1Name));
        facade.createGame(new CreateGameRequest(game2Name));
        facade.createGame(new CreateGameRequest(game3Name));
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames().games());
    }

    @Test
    void joinGamePositive() {
        String username = "user";
        String gameName = "myGame";
        facade.register(new RegisterRequest(username, "password", "p1@email.com"));
        CreateGameResult gameID = facade.createGame(new CreateGameRequest(gameName));
        Assertions.assertDoesNotThrow(() -> facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID.gameID())));
        ArrayList<GameData> games = facade.listGames().games();
        Assertions.assertEquals(new GameData(gameID.gameID(), username, null, gameName, new ChessGame()), games.getFirst());
    }
    @Test
    void joinGameNegativeUnauthorized() {
        facade.register(new RegisterRequest("user", "password", "p1@email.com"));
        CreateGameResult gameID = facade.createGame(new CreateGameRequest("myGame"));
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID.gameID())));
    }
    @Test
    void joinGameNegativeAlreadyTaken() {
        facade.register(new RegisterRequest("user1", "password", "p1@email.com"));
        facade.register(new RegisterRequest("user2", "password", "p2@email.com"));
        CreateGameResult gameID = facade.createGame(new CreateGameRequest("myGame"));
        facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID.gameID()));
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID.gameID())));
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
