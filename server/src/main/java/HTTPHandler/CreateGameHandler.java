package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import result.CreateGameResult;
import result.ErrorResult;
import request.CreateGameRequest;
import service.GameService;
import service.BadRequestException;
import service.UnauthorizedException;
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
            CreateGameRequest CGreq = serializer.fromJson(req.body(), CreateGameRequest.class);
            CreateGameResult CGRes = service.createGame(authToken, CGreq);
            res.status(200);
            return serializer.toJson(CGRes);
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (BadRequestException e) {
            res.status(400);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(401);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
