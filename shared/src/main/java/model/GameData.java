package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData(GameMetaData gameMetaData) {
        this(gameMetaData.gameID(), gameMetaData.whiteUsername(), gameMetaData.blackUsername(), gameMetaData.gameName(), new ChessGame());
    }
}
