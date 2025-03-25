package ui;

import server.ServerFacade;

public class PreLoginClient implements Client {
    public final int port;
    ServerFacade server;

    public PreLoginClient(int port) {
        this.port = port;
        this.server = new ServerFacade(port);
    }

    public String help() {
        return "";
    }

    public String evaluate(String line) {
        return line;
    }
}
