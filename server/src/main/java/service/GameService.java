package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;

import java.util.Objects;

public class GameService {
    private final UserDAO dataAccess;
    public GameService(MemoryUserDAO dao) {
        this.dataAccess = Objects.requireNonNullElseGet(dao, () -> new MemoryUserDAO(null, null, null));
    }

//    public ListGamesResult listGames(String authToken) {}

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException, BadRequestException, UnauthorizedException {
        try {
            if (req.authToken().isEmpty() || req.gameName().isEmpty()) {
                throw new BadRequestException("Error: bad request");
            }
            if (dataAccess.isNotAuthorized(req.authToken())) {
                throw new UnauthorizedException("Error: unauthorized auth data");
            }
            int gameID = req.gameName().hashCode();
            dataAccess.createGame(gameID, req.gameName());
            return new CreateGameResult(gameID);
        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void joinGame(String authToken, JoinGameRequest req) throws DataAccessException, BadRequestException {
        try {
            if (req.gameID() == null || req.playerColor() == null) {
                throw new BadRequestException("Error: bad request");
            }
            if (dataAccess.isNotAuthorized(authToken)) {
                throw new UnauthorizedException("Error: unauthorized auth data");
            }
            if (dataAccess.gameNotAuthorized(req.gameID())) {
                throw new UnauthorizedException("Error: unauthorized game id");
            }

            GameData gameData = dataAccess.getGame(req.gameID());
            String username = dataAccess.getUsername(authToken);
            GameData newGameData;
            if (req.playerColor() == ChessGame.TeamColor.WHITE) {
                if (dataAccess.colorNotAvailable(req.gameID(), ChessGame.TeamColor.WHITE)) {
                    throw new AlreadyTakenException("Error: color already taken");
                }
                newGameData = new GameData(req.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                if (dataAccess.colorNotAvailable(req.gameID(), ChessGame.TeamColor.BLACK)) {
                    throw new AlreadyTakenException("Error: color already taken");
                }
                newGameData = new GameData(req.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            }
            dataAccess.updateGame(req.gameID(), newGameData);

        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException, UnauthorizedException {
        try {
            if (dataAccess.isNotAuthorized(authToken)) {
                throw new UnauthorizedException("Error: unauthorized auth data");
            }
            return new ListGamesResult(dataAccess.listGames());
        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
