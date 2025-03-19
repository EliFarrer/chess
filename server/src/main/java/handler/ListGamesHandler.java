package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import model.GameMetaData;
import result.ErrorResult;
import service.GameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class ListGamesHandler {
    GameService service;
    Gson serializer;
    public ListGamesHandler(DataAccess dao) {
        service = new GameService(dao);
        serializer = new Gson();
    }

    // var jsonObject = new JsonObject();
    // jsonObject.add("games", gson.toJsonTree(games));
    // res.type too?
    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            ArrayList<GameMetaData> listGamesRes = service.listGames(authToken);
            res.status(200);
            var jsonObject = new JsonObject();
            jsonObject.add("games", serializer.toJsonTree(listGamesRes));
            return jsonObject;
        } catch (UnauthorizedException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
