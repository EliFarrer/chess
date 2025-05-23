package server;
import dataaccess.DataAccess;
import dataaccess.DatabaseDAO;
import handler.*;
import server.websocket.WebSocketHandler;
import spark.*;


public class Server {
    // not sure what to do with service here. I wma going to have multiple of them...
    private final DataAccess dao;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.dao = new DatabaseDAO();
        this.webSocketHandler = new WebSocketHandler(dao);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("/web");

        Spark.webSocket("/ws", webSocketHandler);
        // Register your endpoints and handle exceptions here.
        registerEndpoints();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.notFound("<html><body>Sorry error 404 page not found</body></html>");
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void registerEndpoints() {
        Spark.delete("/db", this::handleClearGame);
        Spark.post("/user", this::handleRegisterUser);
        Spark.post("/session", this::handleLoginUser);
        Spark.delete("/session", this::handleLogoutUser);
        Spark.get("/game", this::handleListGames);
        Spark.post("/game", this::handleCreateGame);
        Spark.put("/game", this::handleJoinGame);
    }

    private Object handleClearGame(Request req, Response res) {
        ClearHandler handler = new ClearHandler(dao);
        return handler.handle(req, res);
    }
    private Object handleRegisterUser(Request req, Response res) {
        RegisterHandler handler = new RegisterHandler(dao);
        return handler.handle(req, res);
    }
    private Object handleLoginUser(Request req, Response res) {
        LoginHandler handler = new LoginHandler(dao);
        return handler.handle(req, res);
    }
    private Object handleLogoutUser(Request req, Response res) {
        LogoutHandler handler = new LogoutHandler(dao);
        return handler.handle(req, res);
    }
    private Object handleListGames(Request req, Response res) {
        ListGamesHandler handler = new ListGamesHandler(dao);
        return handler.handle(req, res);
    }
    private Object handleCreateGame(Request req, Response res) {
        CreateGameHandler handler = new CreateGameHandler(dao);
        return handler.handle(req, res);
    }
    private Object handleJoinGame(Request req, Response res) {
        JoinGameHandler handler = new JoinGameHandler(dao);
        return handler.handle(req, res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
