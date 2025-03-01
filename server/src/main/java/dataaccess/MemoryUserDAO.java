package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.*;


public class MemoryUserDAO implements UserDAO {
    public Map<String, UserData> userDataMap;
    public Map<String, String> authDataMap; // maps from authToken to username
    public Map<Integer, GameData> gameDataMap; // maps from authToken to username


    public MemoryUserDAO(Map<String, UserData> userMap, Map<String, String> authMap, Map<Integer, GameData> gameMap) {
        this.userDataMap = Objects.requireNonNullElseGet(userMap, HashMap::new);
        this.authDataMap = Objects.requireNonNullElseGet(authMap, HashMap::new);
        this.gameDataMap = Objects.requireNonNullElseGet(gameMap, HashMap::new);
    }

    public void clear() throws DataAccessException {
        userDataMap.clear();
        authDataMap.clear();
        gameDataMap.clear();
    }

    public void createUser(UserData userData) throws DataAccessException {
        userDataMap.put(userData.username(), userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        return userDataMap.get(username);
    }

    public AuthData createAuth(String userName) {
        String id = UUID.randomUUID().toString();
        authDataMap.put(id, userName);
        return new AuthData(userName, id);
    }

    public boolean isAuthDataMapEmpty() {
        return authDataMap.isEmpty();
    }

    public boolean isNotAuthorized(String authToken) throws DataAccessException {
        // if the authToken is in the database, we return true because it is authorized
        return (authDataMap.get(authToken) == null);
    }

    public void removeAuth(String authToken) throws DataAccessException {
        authDataMap.remove(authToken);
    }

    public void createGame(int gameID, String gameName) {
        GameData gameData = new GameData(gameID, "", "", gameName, new ChessGame());
        gameDataMap.put(gameID, gameData);
    }

    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }


}
