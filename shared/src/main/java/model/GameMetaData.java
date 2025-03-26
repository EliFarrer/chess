package model;

// same as GameData, but doesn't contain the actual game
public record GameMetaData(int gameID, String whiteUsername, String blackUsername, String gameName) {
    public GameMetaData(GameData gameData) {
        this(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
    }

    @Override
    public String toString() {
        String white = whiteUsername == null ? "?" : whiteUsername;
        String black = blackUsername == null ? "?" : blackUsername;
        return "Game " + gameName +
                ", white=" + white +
                ", black=" + black  +
                ", id=" + gameID;
    }
}
