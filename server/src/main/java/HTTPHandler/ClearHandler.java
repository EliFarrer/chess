package HTTPHandler;
import spark.*;


public class ClearHandler implements Route {
    public Object handle(Request req, Response res) {
        service.clearGame();
        res.status(200);
        return "";
    }

}
