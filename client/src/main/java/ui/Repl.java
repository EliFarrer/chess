package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import server.ResponseException;
import ui.client.GameplayClient;
import ui.client.PostLoginClient;
import ui.client.PreLoginClient;
import ui.websocket.ServerMessageHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.*;


import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl implements ServerMessageHandler {
    BoardPrinter boardPrinter = new BoardPrinter();
    ChessGame.TeamColor perspective;
    State state = State.PRE_LOGIN;
    final int port;
    PreLoginClient preLogin;
    PostLoginClient postLogin;
    GameplayClient gameplay;
    ServerFacade server;
    WebSocketFacade ws;
    Integer currentGameID = null;
    String authToken;

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
                        throw new ResponseException(401, "Error: not authorized");
                    }
                    currentGameID = null;
                    this.perspective = null;
                    result = postLogin.evaluate(line, authToken);
                    newState = postLogin.getNewState();
                    currentGameID = postLogin.getCurrentGameID();
                    authToken = postLogin.getAuthToken();
                    this.perspective = postLogin.getPerspective();
                } else {
                    gameplay.setGameID(currentGameID);
                    gameplay.setPerspective(this.perspective);
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
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            MoveResponse response = new Gson().fromJson(notification.getData(), MoveResponse.class);
            String boardString;
            if (response.move() == null) {
                boolean view = this.perspective == ChessGame.TeamColor.WHITE;
                boardString = boardPrinter.getBoardString(response.game().getBoard().board, view, null, null);
            } else {
                ArrayList<ChessMove> end = new ArrayList<>();
                end.add(response.move());
                ChessPosition start = response.move().getStartPosition();
                boolean view = this.perspective == ChessGame.TeamColor.WHITE;
                boardString = boardPrinter.getBoardString(response.game().getBoard().board, view, end, start);
            }
            System.out.print('\n' + boardString + printInput());
        } else {
            String message = notification.getData();
            System.out.print('\n' + SET_TEXT_COLOR_RED + message + '\n' + printInput());
        }
    }
    public String printInput() { return SET_TEXT_COLOR_MAGENTA + ">>> "; }

    public String reset() { return RESET_TEXT_COLOR; }
}
