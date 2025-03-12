package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import model.UserData;
import java.util.*;

public class MemoryDAO implements DataAccess {
    public Map<String, UserData> userDataMap;   // maps from username to userdata
    public Map<String, String> authDataMap; // maps from id to username
    public Map<Integer, GameData> gameDataMap;  // maps from gameID to game data

    public MemoryDAO(Map<String, UserData> userMap, Map<String, String> authMap, Map<Integer, GameData> gameMap) {
        this.userDataMap = Objects.requireNonNullElseGet(userMap, HashMap::new);
        this.authDataMap = Objects.requireNonNullElseGet(authMap, HashMap::new);
        this.gameDataMap = Objects.requireNonNullElseGet(gameMap, HashMap::new);
    }

    public void clear() throws DataAccessException {
        try {
            userDataMap.clear();
            authDataMap.clear();
            gameDataMap.clear();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        try {
            userDataMap.put(userData.username(), userData);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try {
            return userDataMap.get(username);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean userExists(String username) throws DataAccessException {
        try {
            return userDataMap.get(username) != null;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData createAuth(String userName) throws DataAccessException{
        try {
            String id = UUID.randomUUID().toString();
            authDataMap.put(id, userName);
            return new AuthData(userName, id);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isAuthDataMapEmpty() throws DataAccessException {
        try {
            return authDataMap.isEmpty();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isUserDataMapEmpty() throws DataAccessException{
        try {
            return userDataMap.isEmpty();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isGameDataMapEmpty() throws DataAccessException{
        try {
            return gameDataMap.isEmpty();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }    }

    public boolean isEmpty() throws DataAccessException{
        try {
            return (isGameDataMapEmpty() && isUserDataMapEmpty() && isAuthDataMapEmpty());
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }    }

    public boolean isNotAuthorized(String authToken) throws DataAccessException {
        // if the authToken is in the database, we return false because it is unauthorized
        try {
            return (authDataMap.get(authToken) == null);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }    }

    public void removeAuth(String authToken) throws DataAccessException {
        try {
            authDataMap.remove(authToken);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }    }

    public String getUsername(String authToken) throws DataAccessException{
        try {
            return authDataMap.get(authToken);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }    }

    public void createGame(int gameID, String gameName) throws DataAccessException{
        try {
            GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
            gameDataMap.put(gameID, gameData);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return gameDataMap.get(gameID);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }    }

    public boolean gameNotAuthorized(int gameID) throws DataAccessException{
        try {
            return (gameDataMap.get(gameID) == null);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean colorNotAvailable(int gameID, ChessGame.TeamColor requestedColor) throws DataAccessException{
        try {
            GameData gameData = gameDataMap.get(gameID);
            if (requestedColor == ChessGame.TeamColor.WHITE) {
                return gameData.whiteUsername() != null;
            } else {
                return gameData.blackUsername() != null;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        try {
            gameDataMap.remove(gameID);
            gameDataMap.put(gameID, gameData);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ArrayList<GameMetaData> listGames() throws DataAccessException {
        try {
            ArrayList<GameMetaData> games = new ArrayList<>();
            for (Map.Entry<Integer, GameData> entry : gameDataMap.entrySet()) {
                games.add(new GameMetaData(entry.getValue()));
            }
            return games;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
