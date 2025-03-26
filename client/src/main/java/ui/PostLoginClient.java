package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient implements Client {
    public final int port;
    public State state = State.POST_LOGIN;
    ServerFacade server;

    public PostLoginClient(ServerFacade server) {
        this.server = server;
    }

    public String help() {
        return """
                "logout" to logout
                "create <game name>" to create a new game
                "list" to list all the games
                "play <color> <game id>" to join a game with a specific color
                "observe <game id>" to observe a game being played
                """;    }

    public State getNewState() {
        return state;
    }

    public String evaluate(String line, State state) {
        var commands = line.toLowerCase().split(" ");
        var command = (commands.length > 0) ? commands[0] : "help";
        var parameters = Arrays.copyOfRange(commands, 1, command.length());

        return switch (command) {
            case "quit" -> "Bye!";
            case "logout" -> logout();
            case "create" -> createGame(parameters);
            case "list" -> listGames();
            case "play" -> joinGame(parameters);
            case "observe" -> observeGame(parameters);
            default -> help();
        };
    }

    private String observeGame(String[] parameters) {
        if (parameters.length != 1) {
            throw new ResponseException(400, "Error: Expected observe <game id>");
        }
        state = State.GAMEPLAY;
        return "observing game " + parameters[0];
    }

    private String joinGame(String[] parameters) {
        if (parameters.length != 2) {
            throw new ResponseException(400, "Error: Expected play <color> <game id>");
        }
        var color = parameters[0].equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

        server.joinGame(new JoinGameRequest(color, Integer.parseInt(parameters[1])));
        state = State.GAMEPLAY;
        return "joined game " + parameters[1] + " for color " + parameters[0];
    }

    private String listGames() {
        var games = server.listGames().games();
        var gson = new Gson();
        var res = new StringBuilder();
        for (var game : games) {
            res.append(gson.toJson(game)).append('\n');
        }
        state = State.POST_LOGIN;
        return res.toString();
    }

    private String createGame(String[] parameters) {
        if (parameters.length != 1) {
            throw new ResponseException(400, "Error: Expected create <game name>");
        }
        var res = server.createGame(new CreateGameRequest(parameters[0]));
        state = State.POST_LOGIN;
        return "Created game: " + res.gameID();
    }

    private String logout() {
        server.logout();
        state = State.PRE_LOGIN;
        return "logged out, thank you!";
    }
}
