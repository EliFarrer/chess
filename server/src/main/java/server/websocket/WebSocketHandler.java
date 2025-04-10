package server.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.xml.crypto.Data;
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
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(message, JsonElement.class).getAsJsonObject();
        String type = json.get("commandType").getAsString();

        UserGameCommand command;
        if (type.equalsIgnoreCase("make_move")) {
            command = gson.fromJson(message, MakeMoveCommand.class);
        } else {
            command = gson.fromJson(message, UserGameCommand.class);
        }
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command.getAuthToken(), command.getGameID());
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave(command.getAuthToken());
            case RESIGN -> resign();
        }
    }

    private void connect(Session session, String visitorAuthToken, Integer gameID) throws IOException, DataAccessException {
        if (visitorAuthToken == null || visitorAuthToken.isEmpty()) {
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not authenticated");
            session.getRemote().sendString(message.toString());
            return;
        }
        // handle if they joined as a player or observer
        String user = dataAccess.getUsername(visitorAuthToken);
        GameData game = dataAccess.getGame(gameID);
        if (game == null) {
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad gameID");
            session.getRemote().sendString(message.toString());
            return;
        }

        // add a new connection to our connection manager
        connections.add(session, visitorAuthToken);
        String status;
        boolean isObserver = false;
        if (Objects.equals(user, game.whiteUsername())) {
            status = "white";
        } else if (Objects.equals(user, game.blackUsername())) {
            status = "black";
        } else {
            status = "an observer";
            isObserver = true;
        }

        var message = String.format("%s joined the game as %s", user, status);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        // notify the user joined for everyone currently in the game.
        connections.broadcast(visitorAuthToken, notification);
        var updateGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "printed the game");

        // if it is an observer joining, the game will broadcast to them
        // if it is someone joining the game, it will broadcast to everyone but them.
        if (isObserver) {
            connections.broadcastTo(visitorAuthToken, updateGame);
        } else {
            connections.broadcast(visitorAuthToken, updateGame);
        }
    }

    private void makeMove() {

    }

    private void leave(String authToken) throws IOException, DataAccessException {
        connections.remove(authToken);
        String user = dataAccess.getUsername(authToken);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s left the game", user));
        connections.broadcast(authToken, message);
    }

    private void resign() {}
}
