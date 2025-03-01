package model;

import chess.ChessGame;

// same as GameData, but doesn't contain the actual game
public record GameMetaData(int gameId, String whiteUsername, String blackUsername, String gameName) {
}
