package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import request.CreateGameRequest;
import service.GameService;
import service.ServiceException;
import spark.Request;
import spark.Response;


public class CreateGameHandler {
    GameService service;
    Gson serializer;
    public CreateGameHandler(MemoryUserDAO dao) {
        service = new GameService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            CreateGameRequest CGreq = new CreateGameRequest(authToken, req.body());
            Integer CGRes = service.createGame(CGreq);
            res.status(200);
            return serializer.toJson(CGRes);
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        } catch (ServiceException e) {
            res.status(400);
            return serializer.toJson(e.getMessage());
        }
    }
}
