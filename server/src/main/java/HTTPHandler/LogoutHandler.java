package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import request.RegisterRequest;
import service.ServiceException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    UserService service;
    Gson serializer;
    public LogoutHandler(MemoryUserDAO dao) {
        service = new UserService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            service.logout(req.headers("Authorization"));
            res.status(200);
            return "";
        } catch (ServiceException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        }

    }
}
