package ui;

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
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            if (state == State.PRE_LOGIN) {
                result = preLogin.evaluate(line);
            } else if (state == State.POST_LOGIN) {
                result = postLogin.evaluate(line);
            } else {
                result = gameplay.evaluate(line);
            }
            System.out.println(result);
        }
    }

    public String printInput() { return SET_TEXT_COLOR_MAGENTA + ">>> "; }

    public String reset() { return RESET_TEXT_COLOR; }
}
