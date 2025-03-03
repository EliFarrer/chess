package model;

// same as GameData, but doesn't contain the actual game
public record GameMetaData(int gameId, String whiteUsername, String blackUsername, String gameName) {
    public GameMetaData(GameData gameData) {
        this(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
    }
}
