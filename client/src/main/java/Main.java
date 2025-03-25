import chess.*;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        int serverUrl;
        if (args.length == 1) {
            serverUrl = Integer.parseInt(args[0]);
        } else {
            serverUrl = 8080;
        }
        new Repl(serverUrl).run();
    }
}