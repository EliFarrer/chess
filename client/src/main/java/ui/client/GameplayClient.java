package ui.client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import server.ResponseException;
import ui.ServerFacade;
import ui.State;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class GameplayClient extends Client {
    Map<Integer, String> letterArray = Map.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g", 8, "h");
    public ServerFacade server;
    WebSocketFacade ws;
    public State state = State.GAMEPLAY;
    Integer gameID;
    String authToken;
    ChessGame.TeamColor perspective;

    public GameplayClient(ServerFacade server, WebSocketFacade ws) {
        this.server = server;
        this.ws = ws;
    }

    public void setGameID(int gameID) { this.gameID = gameID; }
    public State getNewState() { return state; }
    public void setPerspective(ChessGame.TeamColor perspective) { this.perspective = perspective; }

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

    public String evaluate(String line, Integer currentGameID, String authToken) {
        this.authToken = authToken;
        try {
            var commands = line.toLowerCase().split(" ");
            var command = (commands.length > 0) ? commands[0] : "help";
            if (commands.length == 0) { return help(); }
            var parameters = Arrays.copyOfRange(commands, 1, commands.length);

            return switch (command) {
                case "redraw" -> redrawBoard(currentGameID);
                case "leave" -> leave(currentGameID);
                case "move" -> move(currentGameID, parameters);
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
        this.gameID = currentGameID;
        return getBoardString(server, currentGameID, this.perspective, null, null);
    }

    private String leave(Integer gameID) {
        ws.leave(authToken, gameID);
        this.state = State.POST_LOGIN;
        this.gameID = null;
        return "left game";
    }

    private String move(Integer currentGameID, String[] parameters) {
        String fromString = parameters[0].toLowerCase();
        String toString = parameters[1].toLowerCase();
        String fromFirstCharacter = String.valueOf(fromString.charAt(0));
        String toFirstCharacter = String.valueOf(toString.charAt(0));
        String fromSecondCharacter = String.valueOf(fromString.charAt(1));
        String toSecondCharacter = String.valueOf(toString.charAt(1));
        ChessPosition start;
        ChessPosition end;
        try {
            start = new ChessPosition(Integer.parseInt(letterArray.get(fromFirstCharacter)), Integer.parseInt(fromSecondCharacter));
            end = new ChessPosition(Integer.parseInt(letterArray.get(toFirstCharacter)), Integer.parseInt(toSecondCharacter));
        } catch (Exception e) {
            throw new ResponseException(400, "Error, expected <a-h><1-8> <a-h><1-8>");
        }

        GameData gameData = getGame(server, gameID);

        ChessMove move = new ChessMove(start, end, null);   // do we need to do promotion stuff?

        ws.makeMove(this.authToken, currentGameID, move);
        this.state = State.GAMEPLAY;
        this.gameID = currentGameID;
        return "";
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
        ws.resign(authToken, gameID);
        this.state = State.GAMEPLAY;
        return "";//""resign";
    }

    private String highlight(String[] parameters) {
        String positionString = parameters[0];
        String firstCharacter = String.valueOf(positionString.charAt(0));
        String secondCharacter = String.valueOf(positionString.charAt(1));
        ChessPosition position = new ChessPosition(Integer.parseInt(letterArray.get(firstCharacter)), Integer.parseInt(secondCharacter));

        ChessGame game = getGame(server, gameID).game();
        return this.getBoardString(server, gameID, perspective, game.validMoves(position), position);
    }

    public Integer getCurrentGameID() { return this.gameID; }

}
