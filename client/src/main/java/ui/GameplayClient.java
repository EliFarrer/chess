package ui;

public class GameplayClient implements Client {
    public final int port;

    public GameplayClient(int port) {
        this.port = port;
    }

    public String help() {
        return "";
    }

    public String evaluate(String line) {
        return line;
    }
}
