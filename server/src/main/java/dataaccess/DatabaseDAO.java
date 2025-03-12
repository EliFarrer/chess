package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;


public class DatabaseDAO implements DataAccess {
    String[] databases = {"user", "game", "auth"};
    public DatabaseDAO() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Clear
     */
    public void clear() throws DataAccessException {
        DatabaseManager.createTables();
        for (var db : databases) {
            var clearTableTemplate = "drop table " + db;
            try (var conn = DatabaseManager.getConnection()) {
                try (PreparedStatement clearTableStatement = conn.prepareStatement(clearTableTemplate)) {
                    clearTableStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        if (!isUserDatabaseEmpty() || !isAuthDatabaseEmpty() || !isGameDatabaseEmpty()) {
            throw new DataAccessException("Error: The database did not clear properly");
        }
    }

    /*
    User DAO
     */
    public void createUser(UserData userData) throws DataAccessException {
        DatabaseManager.createTables();
        UserData secureUserData = new UserData(userData.username(), BCrypt.hashpw(userData.password(), BCrypt.gensalt()), userData.email());
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO user (username, userData) VALUES (?, ?)")) {
                statement.setString(1, secureUserData.username());
                // make a UserData class and convert it to json
                var json = new Gson().toJson(secureUserData);
                statement.setString(2, json);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            String select = "SELECT username, userData FROM user WHERE username = ?";
            try (var statement = conn.prepareStatement(select)) {
                statement.setString(1, username);
                try (var rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Error: The user does not exist");
                    }
                    var json = rs.getString("userData");
                    var gson = new GsonBuilder().create();
                    return gson.fromJson(json, UserData.class);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean userExists(String username) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            String select = "SELECT username, userData FROM user WHERE username = ?";
            try (var statement = conn.prepareStatement(select)) {
                statement.setString(1, username);
                try (var rs = statement.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isUserDatabaseEmpty() throws DataAccessException {
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM user")) {
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {   // if we are past the last row, then it is empty
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /*
    Auth DAO
     */

    public AuthData createAuth(String username) throws DataAccessException {
        DatabaseManager.createTables();
        String id = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO auth (id, username) VALUES (?, ?)")) {
                statement.setString(1, id);
                statement.setString(2, username);
                statement.executeUpdate();
            }
            return new AuthData(username, id);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public String getUsername(String authToken) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username FROM auth WHERE id = ?")) {
                statement.setString(1, authToken);
                try (var rs = statement.executeQuery()) {
                    rs.next();
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isNotAuthorized(String authToken) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username FROM auth WHERE id = ?")) {
                statement.setString(1, authToken);
                try (var rs = statement.executeQuery()) {
                    // if there are not any usernames that match the auth, then rs.next() will return false. So we return true
                    return !rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void removeAuth(String authToken) throws DataAccessException {
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM auth WHERE id = ?")) {
                stmt.setString(1, authToken);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isAuthDatabaseEmpty() throws DataAccessException {
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM auth")) {
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {   // if we are past the last row, then it is empty
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /*
    Game DAO
     */
    public void createGame(int gameID, String gameName) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO game (gameID, gameData) VALUES (?, ?)")) {
                statement.setInt(1, gameID);

                GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
                var json = new Gson().toJson(gameData);
                statement.setString(2, json);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            String select = "SELECT gameData FROM game WHERE gameID = ?";
            try (var statement = conn.prepareStatement(select)) {
                statement.setInt(1, gameID);
                try (var rs = statement.executeQuery()) {
                    if (!rs.next()) {   // if false, return null
                        return null;
                    }
                    // if not, parse
                    var json = rs.getString("gameData");
                    var gson = new GsonBuilder().create();
                    return gson.fromJson(json, GameData.class);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean gameNotAuthorized(int gameID) throws DataAccessException {
        DatabaseManager.createTables();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameID FROM game WHERE gameID = ?")) {
                statement.setInt(1, gameID);
                try (var rs = statement.executeQuery()) {
                    // if there are not any usernames that match the auth, then rs.next() will return false. So we return true
                    return !rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        DatabaseManager.createTables();
        if (gameData == null) {
            throw new DataAccessException("Error: no gameData provided");
        }
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM game WHERE gameID = ?")) {
                stmt.setInt(1, gameID);
                stmt.executeUpdate();
            }
            try (var statement = connection.prepareStatement("INSERT INTO game (gameID, gameData) VALUES (?, ?)")) {
                statement.setInt(1, gameID);

                var json = new Gson().toJson(gameData);
                statement.setString(2, json);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean colorNotAvailable(int gameID, ChessGame.TeamColor requestedColor) throws DataAccessException {
        DatabaseManager.createTables();
        if (requestedColor == null) {
            throw new DataAccessException("Error: no gameData provided");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gameData FROM game WHERE gameID = ?")) {
                statement.setInt(1, gameID);
                try (var rs = statement.executeQuery()) {
                    // check to make sure our data exists
                    if (!rs.next()) {
                        throw new DataAccessException("Error: game data doesn't exist");
                    }
                    // parse our game data
                    var json = rs.getString("gameData");
                    var gson = new GsonBuilder().create();
                    GameData gameData = gson.fromJson(json, GameData.class);

                    // check to see if the color is available
                    if (requestedColor == ChessGame.TeamColor.WHITE) {
                        return !Objects.equals(gameData.whiteUsername(), null);
                    } else {
                        return !Objects.equals(gameData.blackUsername(), null);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ArrayList<GameMetaData> listGames() throws DataAccessException {
        ArrayList<GameMetaData> games = new ArrayList<>();
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT gameData FROM game")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        var json = rs.getString("gameData");
                        var gson = new GsonBuilder().create();
                        GameMetaData gameMetaData = gson.fromJson(json, GameMetaData.class);
                        games.add(gameMetaData);
                    }
                }
            }
            return games;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isGameDatabaseEmpty() throws DataAccessException {
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT gameID FROM game")) {
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {   // if we are past the last row, then it is empty
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
