package ui;

import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import server.ResponseException;

import java.util.Arrays;

public class PreLoginClient implements Client {
    ServerFacade server;
    State state = State.PRE_LOGIN;

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }

    public String help() {
        this.state = State.PRE_LOGIN;
        return """
                "quit" to exit the game
                "login <username> <password>" to login
                "register <username> <password> <email>" to register
                "help" for this menu
                """;
    }

    public State getNewState() {
        return state;
    }
// every time we call one of these methods, we need to update the state
    public String evaluate(String line, State state) {
        try {
            var commands = line.toLowerCase().split(" ");
            var command = (commands.length > 0) ? commands[0] : "help";
            if (commands.length == 0) { return help(); }
            var parameters = Arrays.copyOfRange(commands, 1, commands.length);

            return switch (command) {
                case "quit" -> "quit";
                case "login" -> login(parameters);
                case "register" -> register(parameters);
                default -> help();
            };
        } catch (ResponseException ex) {
            this.state = State.PRE_LOGIN;
            return ex.getMessage();
        }

    }

    // we don't care about the auth tokens here, the ServerFacade handles that
    public String login(String[] params) {
        if (params.length != 2) {
            throw new ResponseException(400, "Error: Expected login <username> <password>");
        }
        LoginRequest req = new LoginRequest(params[0], params[1]);
        LoginResult res = server.login(req);
        if (res == null) {
            throw new ResponseException(400, "Error: Did not get a result");
        }
        state = State.POST_LOGIN;
        return "logged in as " + res.username();
    }

    public String register(String[] params) {
        if (params.length != 3) {
            throw new ResponseException(400, "Error: Expected register <username> <password> <email>");
        }
        RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);
        LoginResult res = server.register(req);
        state = State.POST_LOGIN;
        return "registered as " + res.username();
    }

}
