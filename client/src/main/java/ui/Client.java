package ui;

public interface Client {
    public String help();

    // this will return a string of the result which will then be printed in the repl.
    // So this is basically a parser
    public String evaluate(String line);
}
