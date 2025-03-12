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
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

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
    }

    /*
    User DAO
     */
    public void createUser(UserData userData) throws DataAccessException {
        DatabaseManager.createTables();
        UserData secureUserData = new UserData(userData.username(), BCrypt.hashpw(userData.password(), BCrypt.gensalt()), userData.email());
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("insert into user (username, userData) values (?, ?)")) {
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
                    rs.next();
                    var json = rs.getString("userData");
                    var builder = new GsonBuilder();
                    Gson gson = builder.create();
                    return gson.fromJson(json, UserData.class);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUserDatabaseEmpty() throws DataAccessException {
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM userData")) {
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

    /*
    Auth DAO
     */

    public AuthData createAuth(String username) throws DataAccessException {
        DatabaseManager.createTables();
        String id = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("insert into auth (id, username) values (?, ?)")) {
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    /*
    Game DAO
     */
    public void createGame(int gameID, String gameName) throws DataAccessException {
        DatabaseManager.createTables();

        //
    }

    public GameData getGame(int gameID) throws DataAccessException {
        DatabaseManager.createTables();

        return null;
    }

    public boolean gameNotAuthorized(int gameID) throws DataAccessException {
        DatabaseManager.createTables();

        return false;
    }

    public boolean colorNotAvailable(int gameID, ChessGame.TeamColor requestedColor) throws DataAccessException {
        DatabaseManager.createTables();

        return false;
    }

    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        DatabaseManager.createTables();


    }

    public ArrayList<GameMetaData> listGames() throws DataAccessException {
        DatabaseManager.createTables();

        return null;
    }

    public boolean isGameDatabaseEmpty() throws DataAccessException {
        DatabaseManager.createTables();
        try (var connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM game")) {
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
}
