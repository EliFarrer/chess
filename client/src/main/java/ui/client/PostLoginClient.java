package ui.client;

import chess.ChessGame;
import request.CreateGameRequest;
import request.JoinGameRequest;
import server.ResponseException;
import ui.ServerFacade;
import ui.State;
import ui.websocket.WebSocketFacade;

import java.awt.*;
import java.util.*;

public class PostLoginClient extends Client {
    ChessGame.TeamColor perspective;
    private State state = State.POST_LOGIN;
    ServerFacade server;
    WebSocketFacade ws;
    LinkedHashMap<Integer, Integer> gameNumberToGameID = new LinkedHashMap<>();
    Integer gameCount = 0;
    Integer currentGameID = null;
    String authToken = null;

    public PostLoginClient(ServerFacade server, WebSocketFacade ws) {
        this.server = server;
        this.ws = ws;
    }

    public String help() {
        this.state = State.POST_LOGIN;
        return """
                "logout" to logout
                "create <game name>" to create a new game
                "list" to list all the games
                "play <BLACK|WHITE> <game id>" to join a game with a specific color
                "observe <game id>" to observe a game being played
                "help" for this menu
                """;    }

    public String evaluate(String line, String authToken) {
        this.authToken = authToken;
        try {
            var commands = line.toLowerCase().split(" ");
            var command = (commands.length > 0) ? commands[0] : "help";
            if (commands.length == 0) { return help(); }
            var parameters = Arrays.copyOfRange(commands, 1, commands.length);

            return switch (command) {
                case "quit" -> "Bye!";
                case "logout" -> logout();
                case "create" -> createGame(parameters);
                case "list" -> listGames();
                case "play" -> joinGame(parameters);
                case "observe" -> observeGame(parameters);
                default -> help();
            };
        } catch (ResponseException ex) {
            this.state = State.POST_LOGIN;
            return ex.getMessage();
        }
    }

    private String observeGame(String[] parameters) {
        if (parameters.length != 1) {
            throw new ResponseException(400, "Error: Expected observe <game id>");
        }
        Integer gameNumber = Integer.parseInt(parameters[0]);

        updateGameCount();
        if (gameNumber > gameCount | gameNumber <= 0) {
            throw new ResponseException(400, String.format("Error: Game %d does not exist", gameNumber));
        }
        Integer gameID = gameNumberToGameID.get(gameNumber);
        this.state = State.GAMEPLAY;
        this.currentGameID = gameID;
        this.perspective = ChessGame.TeamColor.WHITE;
        ws.connect(authToken, gameID);
        return "";//getBoardString(server, gameID, ChessGame.TeamColor.WHITE);
    }

    private String joinGame(String[] parameters) {
        if (parameters.length != 2) {
            throw new ResponseException(400, "Error: Expected join <color> <game id>");
        }

        String gameName = parameters[0];
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(parameters[1]);

        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Error: Expected a number");
        }

        ChessGame.TeamColor color;
        if (gameName.equalsIgnoreCase("white")) {
            color = ChessGame.TeamColor.WHITE;
            this.perspective = ChessGame.TeamColor.WHITE;
        } else if (gameName.equalsIgnoreCase("black")) {
            color = ChessGame.TeamColor.BLACK;
            this.perspective = ChessGame.TeamColor.BLACK;
        } else {
            throw new ResponseException(400, "Error: Expected join <BLACK|WHITE> <game id>");
        }

        updateGameCount();
        Integer gameID = gameNumberToGameID.get(gameNumber);
        if (gameNumber > gameCount | gameNumber <= 0) {
            throw new ResponseException(400, String.format("Error: Game %d does not exist", gameNumber));
        }

        server.joinGame(new JoinGameRequest(color, gameID));
        ws.connect(authToken, gameID);
        this.state = State.GAMEPLAY;
        this.currentGameID = gameID;
        return "";// this.getBoardString(server, gameID, color);
    }

    private String listGames() {
        var games = server.listGames().games();
        var res = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            res.append(i + 1).append(") ").append(games.get(i).toString()).append('\n');
        }
        this.state = State.POST_LOGIN;
        if (res.toString().isEmpty()) {
            return "No games...";
        }
        return res.toString();
    }

    private String createGame(String[] parameters) {
        if (parameters.length != 1) {
            throw new ResponseException(400, "Error: Expected create <game name>");
        }
        String gameName = parameters[0];

        var res = server.createGame(new CreateGameRequest(gameName));

        gameNumberToGameID.put(++gameCount, res.gameID());

        this.state = State.POST_LOGIN;
        return "Created game: " + gameName;
    }

    private String logout() {
        server.logout();
        this.state = State.PRE_LOGIN;
        this.authToken = null;
        return "logged out, thank you!";
    }

    private void updateGameCount() {
        var games = server.listGames().games();
        gameCount = games.size();
        for (int i = 1; i <= gameCount; i++) {
            gameNumberToGameID.put(i, games.get(i-1).gameID());
        }
    }

    public State getNewState() { return state; }

    public String getAuthToken() { return this.authToken; }

    public Integer getCurrentGameID() { return this.currentGameID; }

    public ChessGame.TeamColor getPerspective() { return this.perspective; }
}
