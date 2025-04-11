package ui.client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import server.ResponseException;
import ui.BoardPrinter;
import ui.ServerFacade;
import ui.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class Client {
    BoardPrinter boardPrinter = new BoardPrinter();
    public String authToken;
    public Integer gameID;
    public State state;
    abstract String help();

//    abstract String evaluate(String line, State state);

    protected GameData getGame(ServerFacade server, int gameID) {
        ArrayList<GameData> games = server.listGames().games();
        for (var game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    protected String getBoardString(ServerFacade server, Integer gameID, ChessGame.TeamColor perspective, Collection<ChessMove> potentialPositions, ChessPosition position) {
        var game = Objects.requireNonNull(getGame(server, gameID)).game();
        if (game == null) {
            throw new ResponseException(400, "Error: game doesn't exist");
        }

        boolean whitePerspective = perspective == ChessGame.TeamColor.WHITE;

        return boardPrinter.getBoardString(game.getBoard().board, whitePerspective, potentialPositions, position);
    }
}
