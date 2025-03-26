package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import result.ErrorResult;
import request.RegisterRequest;
import result.LoginResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    UserService service;
    Gson serializer;
    public RegisterHandler(DataAccess dao) {
        service = new UserService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            RegisterRequest regReq = serializer.fromJson(req.body(), RegisterRequest.class);
            LoginResult regRes = service.register(regReq);
            res.status(200);
            return serializer.toJson(regRes);
        } catch (DataAccessException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (BadRequestException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        } catch (AlreadyTakenException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }

    }
}
