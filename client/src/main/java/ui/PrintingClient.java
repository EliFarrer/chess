package ui;

import chess.ChessGame;
import model.GameData;
import server.ResponseException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class PrintingClient {
    BoardPrinter boardPrinter = new BoardPrinter();

    abstract String help();

//    abstract String evaluate(String line, State state);

    private GameData getGame(ServerFacade server, int gameID) {
        ArrayList<GameData> games = server.listGames().games();
        for (var game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    protected String getBoardString(ServerFacade server, Integer gameID, ChessGame.TeamColor perspective) {
        var game = Objects.requireNonNull(getGame(server, gameID)).game();
        if (game == null) {
            throw new ResponseException(400, "Error: game doesn't exist");
        }

        boolean whitePerspective = perspective == ChessGame.TeamColor.WHITE;

        return boardPrinter.getBoardString(game.getBoard().board, whitePerspective);
    }
}
