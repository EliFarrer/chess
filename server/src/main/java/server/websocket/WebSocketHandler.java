package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    ConnectionManager connections = new ConnectionManager();
    DataAccess dataAccess;

    public WebSocketHandler(DataAccess dataAccess) {
        // pass the dataAccess class in. That way we can determine things like usernames.
        this.dataAccess = dataAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command.getAuthToken(), command.getGameID());
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(Session session, String visitorAuthToken, Integer gameID) throws IOException, DataAccessException {
        // add a new connection to our connection manager
        connections.add(session, visitorAuthToken);
        // handle if they joined as a player or observer
        String user = dataAccess.getUsername(visitorAuthToken);
        GameData game = dataAccess.getGame(gameID);

        String status;
        if (Objects.equals(user, game.whiteUsername())) {
            status = "white";
        } else if (Objects.equals(user, game.blackUsername())) {
            status = "black";
        } else {
            status = "an observer";
        }

        var message = String.format("%s joined the game as %s", user, status);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(visitorAuthToken, notification);
        var updateGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "printed the game");
        connections.broadcast("", updateGame);
    }

    private void makeMove() {

    }

    private void leave() {}

    private void resign() {}
}
