package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.GameMetaData;
import request.CreateGameRequest;
import request.JoinGameRequest;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private final UserDAO dataAccess;
    public GameService(MemoryUserDAO dao) {
        this.dataAccess = Objects.requireNonNullElseGet(dao, () -> new MemoryUserDAO(null, null, null));
    }

//    public ListGamesResult listGames(String authToken) {}

    public Integer createGame(CreateGameRequest req) throws DataAccessException, ServiceException{
        try {
            if (req.authToken().isEmpty() || req.gameName().isEmpty()) {
                throw new ServiceException("bad data");
            }
            if (dataAccess.isNotAuthorized(req.authToken())) {
                throw new ServiceException("unauthorized auth data");
            }
            int gameID = req.gameName().hashCode();
            dataAccess.createGame(gameID, req.gameName());
            return gameID;
        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void joinGame(String authToken, JoinGameRequest req) throws DataAccessException, ServiceException {
        try {
            if (req.gameID() == null || req.playerColor() == null) {
                throw new ServiceException("bad data");
            }
            if (dataAccess.isNotAuthorized(authToken)) {
                throw new ServiceException("unauthorized auth data");
            }
            if (dataAccess.gameNotAuthorized(req.gameID())) {
                throw new ServiceException("unauthorized game id");
            }

            GameData gameData = dataAccess.getGame(req.gameID());
            String username = dataAccess.getUsername(authToken);
            GameData newGameData;
            if (req.playerColor() == ChessGame.TeamColor.WHITE) {
                if (dataAccess.colorNotAvailable(req.gameID(), ChessGame.TeamColor.WHITE)) {
                    throw new ServiceException("color already taken");
                }
                newGameData = new GameData(req.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                if (dataAccess.colorNotAvailable(req.gameID(), ChessGame.TeamColor.BLACK)) {
                    throw new ServiceException("color already taken");
                }
                newGameData = new GameData(req.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            }
            dataAccess.updateGame(req.gameID(), newGameData);

        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ArrayList<GameMetaData> listGames(String authToken) throws DataAccessException, ServiceException{
        try {
            if (dataAccess.isNotAuthorized(authToken)) {
                throw new ServiceException("unauthorized auth data");
            }
            return dataAccess.listGames();
        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
