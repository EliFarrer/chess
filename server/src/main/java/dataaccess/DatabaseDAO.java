package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DatabaseDAO implements DataAccess {
    String[] databases = {"user", "game", "auth"};
    public DatabaseDAO(Map<String, UserData> userMap, Map<String, String> authMap, Map<Integer, GameData> gameMap) {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() throws DataAccessException {
        var clearTableTemplate = "drop table ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement clearTableStatement = conn.prepareStatement(clearTableTemplate)) {
                for (var db : databases) {
                    var stmt = clearTableStatement;
                    stmt.setString(1, db);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        // add a user
    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public boolean isUserDatabaseEmpty() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM user")) {
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {   // if we are past the last row, then it is empty
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData createAuth(String userName) throws DataAccessException {
        return null;
    }

    public boolean isAuthDataMapEmpty() throws DataAccessException {
        return false;
    }

    public boolean isNotAuthorized(String authToken) throws DataAccessException {
        return false;
    }

    public void removeAuth(String authToken) throws DataAccessException {
        //
    }

    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    public void createGame(int gameID, String gameName) throws DataAccessException {
        //
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public boolean gameNotAuthorized(int gameID) throws DataAccessException {
        return false;
    }

    public boolean colorNotAvailable(int gameID, ChessGame.TeamColor requestedColor) throws DataAccessException {
        return false;
    }

    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

    }

    public ArrayList<GameMetaData> listGames() throws DataAccessException {
        return null;
    }
}
