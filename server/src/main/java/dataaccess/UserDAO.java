package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

public interface UserDAO {
    // in here are all the declarations of the methods we need to run eventually like clear and add and stuff like that
    void clear() throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    AuthData createAuth(String userName);

    boolean isAuthDataMapEmpty() throws DataAccessException;

    boolean isNotAuthorized(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken);

    void createGame(int gameID, String gameName) throws DataAccessException;

    GameData getGame(int gameID);

    boolean gameNotAuthorized(int gameID);

    boolean colorNotAvailable(int gameID, ChessGame.TeamColor requestedColor);

    void updateGame(int gameID, GameData gameData);
}