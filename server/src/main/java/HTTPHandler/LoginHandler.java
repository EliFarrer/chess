package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import request.LoginRequest;
import result.LoginResult;
import service.ServiceException;
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
        } catch (ServiceException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        }

    }
}
