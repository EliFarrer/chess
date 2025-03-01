package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;

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
            if (dataAccess.isNotAuthorized(req.authToken())) {
                throw new ServiceException("unauthorized auth data");
            }
            int gameID = req.gameName().hashCode();
            dataAccess.createGame(gameID, req.gameName());
            return gameID;
        } catch(DataAccessException e) {
            throw new ServiceException("Data access exception (creating game): " + e);
        }
    }

    public void joinGame(JoinGameRequest req) {
        try {
            if (req.gameID().isEmpty() || req.playerColor().isEmpty()) {
                throw new ServiceException("bad data");
            }
            if (dataAccess.isNotAuthorized(req.authToken())) {
                throw new ServiceException("unauthorized auth data");
            }
//            if (dataAccess.gameNotAuthorized(req.gameID())) {
//                throw new ServiceException("unauthorized game id");
//            }
//            GameData gameData = dataAccess.getGame(req.gameID());
//            ChessGame.TeamColor requestedColor = req.playerColor().equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
//            if (dataAccess.colorNotAvailable(requestedColor)) {
//                throw new ServiceException("color already taken");
//            }


        } catch(DataAccessException e) {
            throw new ServiceException("Data access exception (joining game): " + e);
        }
    }
}
