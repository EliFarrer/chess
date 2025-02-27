package server;
import service.ClearService;
import spark.*;

public class Server {
    // not sure what to do with service here. I wma going to have multiple of them...
    private final ClearService service;

    public Server() {
        this.service = new ClearService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.notFound("<html><body>Sorry error 404 page not found</body></html>");
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void registerEndpoints() {
        Spark.post("/user", this::handleRegisterUser);
        Spark.post("/session", this::handleLoginUser);
        Spark.delete("/session", this::handleLogoutUser);
        Spark.get("/game", this::handleListGames);
        Spark.post("/game", this::handleCreateGame);
        Spark.put("/game", this::handleJoinGame);
        Spark.delete("/db", this::handleClearGame);

    }

    private Object handleRegisterUser(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }
    private Object handleLoginUser(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }
    private Object handleLogoutUser(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }
    private Object handleListGames(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }
    private Object handleCreateGame(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }
    private Object handleJoinGame(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }
    private Object handleClearGame(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
