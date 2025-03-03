package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import result.ErrorResult;
import request.LoginRequest;
import result.LoginResult;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    UserService service;
    Gson serializer;
    public LoginHandler(MemoryUserDAO dao) {
        service = new UserService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            LoginRequest logReq = serializer.fromJson(req.body(), LoginRequest.class);
            LoginResult logRes = service.login(logReq);
            res.status(200);
            return serializer.toJson(logRes);
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(401);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
