package HTTPHandler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import request.RegisterRequest;
import service.ServiceException;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    UserService service;
    Gson serializer;
    public RegisterHandler(MemoryUserDAO dao) {
        service = new UserService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            RegisterRequest regReq = serializer.fromJson(req.body(), RegisterRequest.class);
            service.register(regReq);
            res.status(200);
            return serializer.toJson(regReq);
        } catch (DataAccessException e) {
            res.status(400);
            return serializer.toJson(e.getMessage());
        } catch (ServiceException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        }

    }
}
