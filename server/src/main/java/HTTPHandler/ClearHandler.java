package HTTPHandler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import result.ErrorResult;
import service.ClearService;
import spark.*;


public class ClearHandler implements Route {
    ClearService service;
    Gson serializer;
    public ClearHandler(MemoryUserDAO dao) {
        service = new ClearService(dao);
        serializer = new Gson();
    }

    public Object handle(Request req, Response res) {
        try {
            service.clearGame();
            return "";
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(new ErrorResult(e.getMessage()));
        }
    }
}
