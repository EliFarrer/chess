package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import result.ErrorResult;
import result.ListGamesResult;
import service.GameService;
import service.BadRequestException;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    GameService service;
    Gson serializer;
    public ListGamesHandler(MemoryUserDAO dao) {
        service = new GameService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
           ListGamesResult LGRes = service.listGames(authToken);
            res.status(200);
            return serializer.toJson(LGRes);
        } catch (UnauthorizedException e) {
            res.status(401);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
