package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.GameMetaData;
import service.GameService;
import service.ServiceException;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

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
            ArrayList<GameMetaData> LGRes = service.listGames(authToken);
            res.status(200);
            return serializer.toJson(LGRes);
        } catch (ServiceException e) {
            res.status(400);
            return serializer.toJson(e.getMessage());
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        }

    }
}
