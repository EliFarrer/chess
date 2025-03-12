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
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    public AuthData createAuth(String userName) throws DataAccessException {
        return null;
    }

    public boolean isNotAuthorized(String authToken) throws DataAccessException {
        return false;
    }

    public void removeAuth(String authToken) throws DataAccessException {
        //
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
