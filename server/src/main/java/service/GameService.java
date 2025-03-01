package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import request.CreateGameRequest;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.Objects;

public class GameService {
    private final UserDAO dataAccess;
    public GameService(MemoryUserDAO dao) {
        this.dataAccess = Objects.requireNonNullElseGet(dao, () -> new MemoryUserDAO(null, null, null));
    }

//    public ListGamesResult listGames(String authToken) {}

    public Integer createGame(CreateGameRequest req) {
        try {
            if (req.authToken().isEmpty() || req.gameName().isEmpty()) {
                throw new ServiceException("bad data");
            }
            if (!dataAccess.isAuthorized(req.authToken())) {
                throw new ServiceException("unauthorized auth data");
            }
            int gameID = req.gameName().hashCode();
            dataAccess.createGame(gameID, req.gameName());
            return gameID;
        } catch(DataAccessException e) {
            throw new ServiceException("Data access exception: " + e);
        }
    }

//    public JoinGameResult joinGame(String authToken) {}
}
