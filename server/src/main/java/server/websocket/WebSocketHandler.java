package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DatabaseDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {
    ConnectionManager connections = new ConnectionManager();
    DataAccess dataAccess;

    public WebSocketHandler(DataAccess dataAccess) {
        // pass the dataAccess class in. That way we can determine things like usernames.
        this.dataAccess = dataAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command.getAuthToken());
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(Session session, String visitorAuthToken) {
        // add a new connection to our connection manager
        connections.add(session, visitorAuthToken);
        // handle if they joined as a player or observer
//        connections.broadcast(visitorAuthToken)
    }

    private void makeMove() {}

    private void leave() {}

    private void resign() {}
}
