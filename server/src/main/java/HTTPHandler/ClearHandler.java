package HTTPHandler;
import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import service.ClearService;
import service.ServiceException;
import spark.*;

import java.util.Map;


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
        } catch (ServiceException e) {
            res.status(500);
            return serializer.toJson(e.getMessage());
        }

    }

}
