package handler;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import result.ErrorResult;
import service.ClearService;
import spark.*;


public class ClearHandler implements Route {
    ClearService service;
    Gson serializer;
    public ClearHandler(DataAccess dao) {
        service = new ClearService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            service.clearGame();
            return "";
        } catch (DataAccessException e) {
            res.status(e.getStatusCode());
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
