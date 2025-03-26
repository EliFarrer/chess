package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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
    public CreateGameHandler(DataAccess dao) {
        service = new GameService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            CreateGameRequest createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
            CreateGameResult createGameResponse = service.createGame(authToken, createGameRequest);
            res.status(200);
            return serializer.toJson(createGameResponse);
        } catch (DataAccessException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (BadRequestException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
