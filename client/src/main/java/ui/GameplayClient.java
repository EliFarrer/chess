package ui;

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
                "help" for this menu
                """;    }

    public State getNewState() {
        return state;
    }

    public String evaluate(String line, State state) {
        return line;
    }
}
