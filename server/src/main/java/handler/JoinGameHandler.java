package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import result.ErrorResult;
import request.JoinGameRequest;
import service.AlreadyTakenException;
import service.GameService;
import service.BadRequestException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;


public class JoinGameHandler {
    GameService service;
    Gson serializer;
    public JoinGameHandler(DataAccess dao) {
        service = new GameService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            JoinGameRequest joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);

            service.joinGame(authToken, joinGameRequest);
            res.status(200);
            return "";
        } catch (DataAccessException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (BadRequestException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (AlreadyTakenException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
