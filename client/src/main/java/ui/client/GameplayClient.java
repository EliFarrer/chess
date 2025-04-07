package ui.client;

import chess.ChessGame;
import server.ResponseException;
import ui.ServerFacade;
import ui.State;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Scanner;

public class GameplayClient extends PrintingClient {
    public ServerFacade server;
    WebSocketFacade ws;
    public State state = State.GAMEPLAY;
    Integer gameID;
    String authToken;

    public GameplayClient(ServerFacade server, WebSocketFacade ws) {
        this.server = server;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String help() {
        // should leave remove the player from the game?
        this.state = State.GAMEPLAY;
        return """
                "redraw" to redraw the chess board
                "leave" to leave the game
                "move <from> <to>" to move a piece (letters come first)
                "resign" to forfeit the game
                "highlight <position>" to show the legal moves for a position
                "help" for this menu
                """;    }

    public State getNewState() {
        return state;
    }

    public String getAuthToken() { return this.authToken; }

    public String evaluate(String line, Integer currentGameID, String authToken) {
        this.authToken = authToken;
        try {
            var commands = line.toLowerCase().split(" ");
            var command = (commands.length > 0) ? commands[0] : "help";
            if (commands.length == 0) { return help(); }
            var parameters = Arrays.copyOfRange(commands, 1, commands.length);

            return switch (command) {
                case "redraw" -> redrawBoard(currentGameID);
                case "leave" -> leave();
                case "move" -> move(parameters);
                case "resign" -> resign();
                case "highlight" -> highlight(parameters);
                default -> help();
            };
        } catch (ResponseException ex) {
            this.state = State.GAMEPLAY;
            return ex.getMessage();
        }
    }

    private String redrawBoard(Integer currentGameID) {
        this.state = State.GAMEPLAY;
        return this.getBoardString(this.server, currentGameID, ChessGame.TeamColor.WHITE);
    }

    private String leave() {
        // I need to check if it is the same user so they can hop in if it is them
        this.state = State.POST_LOGIN;
        // note that the REPL handles resetting the gameID and this one is reset every time
        return "left game";
    }

    private String move(String[] parameters) {
        return "move";
    }

    private String resign() {
        System.out.print("Are you sure? (y|n): ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("y")) {
                // end the game
                break;
            } else if (!line.equals("n")) {
                System.out.println("Error: Expected y|n");
            } else {
                // else if it is n, then just continue and don't do anything
                System.out.println("You are still in the game.");
            }
        }
        this.state = State.GAMEPLAY;

        return "resign";
    }

    private String highlight(String[] parameters) {
        // print everything
        return "highlight";
    }
}
