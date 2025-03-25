package ui;

public class PostLoginClient implements Client {
    public final int port;

    public PostLoginClient(int port) {
        this.port = port;
    }

    public String help() {
        return "";
    }

    public String evaluate(String line) {
        return line;
    }
}
