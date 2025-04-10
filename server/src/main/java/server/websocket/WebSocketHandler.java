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
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
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
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID());
            case LEAVE -> leave(command.getAuthToken());
            case RESIGN -> resign(command.getAuthToken(), command.getGameID());
        }
    }

    private void connect(Session session, String rootClientAuth, Integer gameID) throws IOException, DataAccessException {
        if (rootClientAuth == null || rootClientAuth.isEmpty()) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not authenticated");
            session.getRemote().sendString(message.toString());
            return;
        }

        String user;
        try {
            user = dataAccess.getUsername(rootClientAuth);
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not authenticated");
            session.getRemote().sendString(message.toString());
            return;
        }
        GameData game = dataAccess.getGame(gameID);
        if (game == null) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad gameID");
            session.getRemote().sendString(message.toString());
            return;
        }

        // add a new connection to our connection manager
        connections.add(session, rootClientAuth);

        // handle if they joined as a player or observer
        String status;
        if (Objects.equals(user, game.whiteUsername())) {
            status = "white";
        } else if (Objects.equals(user, game.blackUsername())) {
            status = "black";
        } else {
            status = "an observer";
        }

        var updateGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, "printed the game");
        var message = String.format("%s joined the game as %s", user, status);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        // broadcast the game to the user who just joined
        connections.broadcastTo(rootClientAuth, updateGame);
        // notify the user joined for everyone currently in the game.
        connections.broadcast(rootClientAuth, notification);
    }

    private void leave(String authToken) throws IOException, DataAccessException {
        connections.remove(authToken);
        String user = dataAccess.getUsername(authToken);
        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s left the game", user));
        connections.broadcast(authToken, message);
    }

    private void makeMove(String authToken, Integer gameID) throws IOException, DataAccessException {
        LoadGameMessage newGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, "new game");
        NotificationMessage moveMessage = new NotificationMessage(ServerMessage.ServerMessageType.LOAD_GAME, "moved from here to there");
        connections.broadcast(authToken, newGameMessage);
        connections.broadcast(authToken, moveMessage);
    }

    private void resign(String authToken, Integer gameID) {}
}
