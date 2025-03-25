package ui;

import java.util.Arrays;

public class GameplayClient implements Client {
    public final int port;
    public State state = State.GAMEPLAY;

    public GameplayClient(int port) {
        this.port = port;
    }

    public String help() {
        return """
                "quit" to exit the game
                "login <username> <password>" to login
                "register <username> <password> <email>" to register
                """;    }

    public State getNewState() {
        return state;
    }

    public String evaluate(String line, State state) {
//        var commands = line.toLowerCase().split(" ");
//        var command = (commands.length > 0) ? commands[0] : "help";
//        var parameters = Arrays.copyOfRange(commands, 1, command.length());

        return line;
    }
}
