package ui;

import server.ServerFacade;

import java.util.Arrays;

public class GameplayClient implements Client {
    public ServerFacade server;
    public State state = State.GAMEPLAY;

    public GameplayClient(ServerFacade server) {
        this.server = server;
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
