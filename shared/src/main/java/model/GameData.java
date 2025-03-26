package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
//    public GameData(GameMetaData gameMetaData) {
//        this(gameMetaData.gameID(), gameMetaData.whiteUsername(), gameMetaData.blackUsername(), gameMetaData.gameName(), new ChessGame());
//    }

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

        return gameID == gameData.gameID && Objects.equals(game, gameData.game) && Objects.equals(gameName, gameData.gameName) && Objects.equals(whiteUsername, gameData.whiteUsername) && Objects.equals(blackUsername, gameData.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
