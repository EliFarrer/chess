package ui;

public interface Client {
    String help();

    // this will return a string of the result which will then be printed in the repl.
    // So this is basically a parser
    String evaluate(String line, State state);

    State getNewState();
}
