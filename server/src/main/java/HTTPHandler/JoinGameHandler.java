package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import request.JoinGameRequest;
import service.GameService;
import service.ServiceException;
import spark.Request;
import spark.Response;


public class JoinGameHandler {
    GameService service;
    Gson serializer;
    public JoinGameHandler(MemoryUserDAO dao) {
        service = new GameService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            JoinGameRequest JGReq = serializer.fromJson(req.body(), JoinGameRequest.class);
            res.status(200);

            service.joinGame(authToken, JGReq);
            res.status(200);
            return "";
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        } catch (ServiceException e) {
            res.status(400);
            return serializer.toJson(e.getMessage());
        }
    }
}
