package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    @Override
    public String toString() {
        String white = whiteUsername == null ? "?" : whiteUsername;
        String black = blackUsername == null ? "?" : blackUsername;
        return "Game " + gameName +
                ", white=" + white +
                ", black=" + black  +
                ", id=" + gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        boolean id = gameID == gameData.gameID;
        boolean data = Objects.equals(game, gameData.game);
        boolean name = Objects.equals(gameName, gameData.gameName);
        boolean white = Objects.equals(whiteUsername, gameData.whiteUsername);
        boolean black = Objects.equals(blackUsername, gameData.blackUsername);
        return id && data && name && white && black;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
