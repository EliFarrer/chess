package ui.client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import server.ResponseException;
import ui.BoardPrinter;
import ui.ServerFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class Client {
    BoardPrinter boardPrinter = new BoardPrinter();
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

    protected String getBoardString(ServerFacade server, Integer gameID, ChessGame.TeamColor view, Collection<ChessMove> ends, ChessPosition start) {
        var game = Objects.requireNonNull(getGame(server, gameID)).game();
        if (game == null) {
            throw new ResponseException(400, "Error: game doesn't exist");
        }

        boolean whitePerspective = view == ChessGame.TeamColor.WHITE;

        return boardPrinter.getBoardString(game.getBoard().board, whitePerspective, ends, start);
    }
}
