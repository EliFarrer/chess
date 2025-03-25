package ui;

import server.ServerFacade;
import java.util.Arrays;
import static ui.EscapeSequences.*;

public class PreLoginClient implements Client {
    public final int port;
    ServerFacade server;

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

    public String evaluate(String line) {
        var commands = line.toLowerCase().split(" ");
        var command = (commands.length > 0) ? commands[0] : "help";
        var parameters = Arrays.copyOfRange(commands, 1, command.length());

        return switch (command) {
            case "quit" -> "quit";
            case "login" -> login(parameters);
            case "register" -> register(parameters);
            default -> help();
        };
    }

    public String login(String[] params) {
        return "login";
    }

    public String register(String[] params) {
        return "register";
    }

}
