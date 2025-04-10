package ui;

import server.ResponseException;
import ui.client.GameplayClient;
import ui.client.PostLoginClient;
import ui.client.PreLoginClient;
import ui.websocket.ServerMessageHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageHandler {
    State state = State.PRE_LOGIN;
    final int port;
    PreLoginClient preLogin;
    PostLoginClient postLogin;
    GameplayClient gameplay;
    ServerFacade server;
    Integer currentGameID = null;
    String authToken;
    WebSocketFacade ws;

    public Repl(int port) {
        this.port = port;
        server = new ServerFacade(port);
        ws = new WebSocketFacade(port, this);
        preLogin = new PreLoginClient(server);
        postLogin = new PostLoginClient(server, ws);
        gameplay = new GameplayClient(server, ws);
    }

    public void run() {
        System.out.print(reset());
        System.out.println(SET_TEXT_COLOR_MAGENTA + "Welcome to the 240 Chess Client ♕");
        System.out.println(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        State newState;
        while (!result.equals("quit")) {
            try {
                System.out.print(printInput());
                String line = scanner.nextLine();
                if (state == State.PRE_LOGIN) {
                    result = preLogin.evaluate(line);
                    newState = preLogin.getNewState();
                    authToken = preLogin.getAuthToken();
                } else if (state == State.POST_LOGIN) {
                    // should I assert we have an auth token?
                    if (authToken == null) {
                        throw new ResponseException(401, "Error, you do not have an auth token");
                    }
                    currentGameID = null;
                    result = postLogin.evaluate(line, authToken);
                    newState = postLogin.getNewState();
                    currentGameID = postLogin.getCurrentGameID();
                    authToken = postLogin.getAuthToken();
                } else {
                    gameplay.setGameID(currentGameID);
                    result = gameplay.evaluate(line, currentGameID, authToken);
                    newState = gameplay.getNewState();
                    currentGameID = gameplay.getCurrentGameID();
                }
                System.out.println(result);
                if (state != newState) {
                    state = newState;
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    public void notify(ServerMessage notification) {
        System.out.print('\n' + SET_TEXT_COLOR_RED + notification.message + '\n' + printInput());
    }
    public String printInput() { return SET_TEXT_COLOR_MAGENTA + ">>> "; }

    public String reset() { return RESET_TEXT_COLOR; }
}
