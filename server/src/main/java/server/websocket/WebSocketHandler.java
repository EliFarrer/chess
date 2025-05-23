package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import websocket.messages.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    String[] letterArray = {"", "a", "b", "c", "d", "e", "f", "g", "h"};
    GameConnectionManager connections = new GameConnectionManager();
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
            case MAKE_MOVE -> makeMove(session, command.getAuthToken(), command.getGameID(), ((MakeMoveCommand) command).getMove());
            case LEAVE -> leave(session, command.getAuthToken(), command.getGameID());
            case RESIGN -> resign(session, command.getAuthToken(), command.getGameID());
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
        connections.add(gameID, session, rootClientAuth);

        // handle if they joined as a player or observer
        String status;
        if (Objects.equals(user, game.whiteUsername())) {
            status = "white";
        } else if (Objects.equals(user, game.blackUsername())) {
            status = "black";
        } else {
            status = "an observer";
        }

        MoveResponse moveData = new MoveResponse(game.game(), null);
        var updateGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, new Gson().toJson(moveData));
        var message = String.format("%s joined the game as %s", user, status);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        // broadcast the game to the user who just joined
        connections.broadcastTo(gameID, rootClientAuth, updateGame);
        // notify the user joined for everyone currently in the game.
        connections.broadcast(gameID, rootClientAuth, notification);
    }

    private void leave(Session session, String rootClientAuth, Integer gameID) throws IOException, DataAccessException {
        connections.remove(gameID, rootClientAuth);
        String user;
        try {
            user = dataAccess.getUsername(rootClientAuth);
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not authenticated");
            session.getRemote().sendString(message.toString());
            return;
        }

        GameData gameData = dataAccess.getGame(gameID);
        ChessGame game = gameData.game();
        if (game == null) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad gameID");
            session.getRemote().sendString(message.toString());
            return;
        }

        // weakness here: if one user is registered for the same game, then it will leave the white one from the black one
        GameData newGameData;
        ChessGame.TeamColor playerColor = user.equals(gameData.whiteUsername()) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            newGameData = new GameData(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else {
            newGameData = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        }
        dataAccess.updateGame(gameID, newGameData);

        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s left the game", user));
        connections.broadcast(gameID, rootClientAuth, message);

    }

    private void makeMove(Session session, String rootClientAuth, Integer gameID, ChessMove move) throws IOException, DataAccessException {
        GameData gameData = dataAccess.getGame(gameID);
        ChessGame game = gameData.game();
        if (game == null) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Bad gameID");
            session.getRemote().sendString(message.toString());
            return;
        }


        String user;
        String opponentUser;
        try {
            user = dataAccess.getUsername(rootClientAuth);
            opponentUser = Objects.equals(gameData.whiteUsername(), user) ? gameData.blackUsername() : gameData.whiteUsername();
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not authenticated");
            session.getRemote().sendString(message.toString());
            return;
        }

        // check it is the right player making the move
        ChessGame.TeamColor turnColor = game.getTeamTurn();
        ChessGame.TeamColor playerColor = Objects.equals(user, gameData.whiteUsername()) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        if (playerColor != turnColor) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not your turn");
            session.getRemote().sendString(message.toString());
            return;
        }

        try {
            if (game.getBoard().spotEmpty(move.getStartPosition())) {
                ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: no piece is there");
                session.getRemote().sendString(message.toString());
                return;
            }
            game.makeMove(move);
            dataAccess.updateGame(gameID, gameData);
        } catch (InvalidMoveException e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
            session.getRemote().sendString(message.toString());
            return;
        }

        MoveResponse moveData = new MoveResponse(game, move);
        LoadGameMessage newGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, new Gson().toJson(moveData));

        String startString = letterArray[move.getStartPosition().getColumn()] + move.getStartPosition().getRow();
        String endString = letterArray[move.getEndPosition().getColumn()] + move.getEndPosition().getRow();
        String moveString = String.format("%s moved from %s to %s", user, startString, endString);
        NotificationMessage moveMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, moveString);
        // broadcast the game to everyone
        connections.broadcast(gameID, "", newGameMessage);
        // broadcast the notification to everyone but the user
        connections.broadcast(gameID, rootClientAuth, moveMessage);


        // check if the new team is in check, checkmate, or stalemate
        ChessGame.TeamColor opponentColor;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            opponentColor = ChessGame.TeamColor.BLACK;
        } else {
            opponentColor = ChessGame.TeamColor.WHITE;
        }
        if (game.isInCheckmate(opponentColor)) {
            String format = String.format("%s is in checkmate", opponentUser);
            NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, format);
            connections.broadcast(gameID, "", message);
        } else {
            if (game.isInCheck(opponentColor)) {
                String format = String.format("%s is in check", opponentUser);
                NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, format);
                connections.broadcast(gameID, "", message);
            }
            if (game.isInStalemate(opponentColor)) {
                String format = String.format("%s is in stalemate \n game ended", opponentUser);
                NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, format);
                connections.broadcast(gameID, "", message);

                game.setGameInPlay(false);
            }
        }

    }

    private void resign(Session session, String rootClientAuth, Integer gameID) throws IOException, DataAccessException {
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


        GameData gameData;
        ChessGame game;
        try {
            gameData = dataAccess.getGame(gameID);
            game = gameData.game();

            // if the game is not in play anymore
            if (!game.isGameInPlay()) {
                ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game over");
                session.getRemote().sendString(message.toString());
                return;
            }

            game.setGameInPlay(false);
            dataAccess.updateGame(gameID, gameData);
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: bad GameID");
            session.getRemote().sendString(message.toString());
            return;
        }

        // if it is an observer trying to resign
        if (!user.equals(gameData.whiteUsername()) && !user.equals(gameData.blackUsername())) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: observers can't resign. Leave instead.");
            session.getRemote().sendString(message.toString());
            return;
        }

        dataAccess.updateGame(gameID, gameData);

        String message = String.format("%s resigned from the game", user);
        connections.broadcast(gameID, "", new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
    }
}
