package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseDAO;
import dataaccess.MemoryDAO;
import dataaccess.DataAccess;
import model.GameData;
import model.GameMetaData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class GameService {
    private final DataAccess dataAccess;
    public GameService(DataAccess dao) {
        this.dataAccess = Objects.requireNonNullElseGet(dao, DatabaseDAO::new);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest req)
            throws DataAccessException, BadRequestException, UnauthorizedException {
        try {
            if (Objects.equals(authToken, "") || Objects.equals(req.gameName(), "")) {
                throw new BadRequestException("Error: bad request");
            }
            if (dataAccess.isNotAuthorized(authToken)) {
                throw new UnauthorizedException("Error: unauthorized auth data");
            }
            UUID uuid = UUID.randomUUID();
            int gameID = Math.abs((int) uuid.getMostSignificantBits());
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
            } else if (req.playerColor() == ChessGame.TeamColor.BLACK){
                if (dataAccess.colorNotAvailable(req.gameID(), ChessGame.TeamColor.BLACK)) {
                    throw new AlreadyTakenException("Error: color already taken");
                }
                newGameData = new GameData(req.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            } else {
                GameData game = dataAccess.getGame(req.gameID());
                if (game.whiteUsername() == null) {
                    newGameData = new GameData(req.gameID(), null, username, gameData.gameName(), gameData.game());
                } else {
                    newGameData = new GameData(req.gameID(), username, null, gameData.gameName(), gameData.game());
                }
            }
            dataAccess.updateGame(req.gameID(), newGameData);

        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException {
        try {
            if (dataAccess.isNotAuthorized(authToken)) {
                throw new UnauthorizedException("Error: unauthorized auth data");
            }
            return dataAccess.listGames();
        } catch(DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
