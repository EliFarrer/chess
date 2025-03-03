package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import model.UserData;
import java.util.ArrayList;

public interface UserDAO {
    // in here are all the declarations of the methods we need to run eventually like clear and add and stuff like that
    void clear() throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    AuthData createAuth(String userName) throws DataAccessException;

    boolean isAuthDataMapEmpty() throws DataAccessException;

    boolean isNotAuthorized(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

    void createGame(int gameID, String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    boolean gameNotAuthorized(int gameID) throws DataAccessException;

    boolean colorNotAvailable(int gameID, ChessGame.TeamColor requestedColor) throws DataAccessException;

    void updateGame(int gameID, GameData gameData) throws DataAccessException;

    ArrayList<GameMetaData> listGames() throws DataAccessException;
    }