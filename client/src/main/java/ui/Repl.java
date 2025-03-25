package ui;

import server.ResponseException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    State state = State.PRE_LOGIN;
    final int port;
    PreLoginClient preLogin;
    PostLoginClient postLogin;
    GameplayClient gameplay;

    public Repl(int port) {
        this.port = port;
        preLogin = new PreLoginClient(port);
        postLogin = new PostLoginClient(port);
        gameplay = new GameplayClient(port);
    }

    public void run() {
        System.out.print(reset());
        System.out.println("Welcome to the 240 Chess Client ♕");
        System.out.println(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        State newState;

        while (!result.equals("quit")) {
            try {
                String line = scanner.nextLine();
                if (state == State.PRE_LOGIN) {
                    result = preLogin.evaluate(line, state);
                    newState = preLogin.getNewState();
                } else if (state == State.POST_LOGIN) {
                    result = postLogin.evaluate(line, state);
                    newState = postLogin.getNewState();
                } else {
                    result = gameplay.evaluate(line, state);
                    newState = gameplay.getNewState();
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

    public String printInput() { return SET_TEXT_COLOR_MAGENTA + ">>> "; }

    public String reset() { return RESET_TEXT_COLOR; }
}
