package ui;

import java.util.Arrays;

public class PostLoginClient implements Client {
    public final int port;

    public PostLoginClient(int port) {
        this.port = port;
    }

    public String help() {
        return """
                "logout" to logout
                "create <game name>" to create a new game
                "list" to list all the games
                "play <game id> <color>" to join a game with a specific color
                "observe <game id>" to observe a game being played
                """;    }

    public String evaluate(String line) {
        var commands = line.toLowerCase().split(" ");
        var command = (commands.length > 0) ? commands[0] : "help";
        var parameters = Arrays.copyOfRange(commands, 1, command.length());

        return switch (command) {
            case "logout" -> logout(parameters);
            case "create" -> createGame(parameters);
            case "list" -> listGames(parameters);
            case "play" -> joinGame(parameters);
            case "observe" -> observeGame(parameters);
            default -> "help";
        };
    }

    private String observeGame(String[] parameters) {
        return "observing";
    }

    private String joinGame(String[] parameters) {
        return "join game";
    }

    private String listGames(String[] parameters) {
        return "list games";
    }

    private String createGame(String[] parameters) {
        return "create game";
    }

    private String logout(String[] parameters) {
        return "logout";
    }
}
