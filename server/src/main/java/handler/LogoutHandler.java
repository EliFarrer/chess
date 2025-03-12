package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import result.ErrorResult;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    UserService service;
    Gson serializer;
    public LogoutHandler(DataAccess dao) {
        service = new UserService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            service.logout(req.headers("Authorization"));
            res.status(200);
            return "";
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(401);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
