package ui;

import server.ResponseException;
import server.ServerFacade;
import java.util.Arrays;

public class PreLoginClient implements Client {
    public final int port;
    ServerFacade server;
    State state = State.PRE_LOGIN;

    public PreLoginClient(int port) {
        this.port = port;
        this.server = new ServerFacade(port);
    }

    public String help() {
        return """
                "quit" to exit the game
                "login <username> <password>" to login
                "register <username> <password> <email>" to register
                """;
    }

    public State getNewState() {
        return state;
    }

    public String evaluate(String line, State state) {
        try {
            var commands = line.toLowerCase().split(" ");
            var command = (commands.length > 0) ? commands[0] : "help";
            var parameters = Arrays.copyOfRange(commands, 1, command.length());

            return switch (command) {
                case "quit" -> "quit";
                case "login" -> login(parameters);
                case "register" -> register(parameters);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

    }

    public String login(String[] params) {
        state = State.POST_LOGIN;
        return "login";
    }

    public String register(String[] params) {
        state = State.POST_LOGIN;
        return "register";
    }

}
