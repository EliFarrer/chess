package ui.client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
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
    Map<String, Integer> letterArray = Map.of("a", 1, "b", 2, "c", 3, "d", 4, "e", 5, "f", 6, "g", 7, "h", 8);
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
        String fromColumn = String.valueOf(fromString.charAt(0));
        String toColumn = String.valueOf(toString.charAt(0));
        String fromRow = String.valueOf(fromString.charAt(1));
        String toRow = String.valueOf(toString.charAt(1));
        ChessPosition start;
        ChessPosition end;
        try {
            start = new ChessPosition(Integer.parseInt(fromRow), letterArray.get(fromColumn));
            end = new ChessPosition(Integer.parseInt(toRow), letterArray.get(toColumn));
        } catch (Exception e) {
            throw new ResponseException(400, "Error, expected <a-h><1-8> <a-h><1-8>");
        }

        GameData gameData = getGame(server, gameID);
        if (!gameData.game().isGameInPlay()) {
            throw new ResponseException(400, "Error, game not in play");
        }

        ChessPiece.PieceType promotion = null;
        if (gameData.game().getBoard().getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (end.getRow() == 8) {
                promotion = getPromotion();
            }
        } else {
            if (end.getRow() == 1) {
                promotion = getPromotion();
            }
        }

        ChessMove move = new ChessMove(start, end, promotion);   // do we need to do promotion stuff?

        ws.makeMove(this.authToken, currentGameID, move);
        this.state = State.GAMEPLAY;
        this.gameID = currentGameID;
        return "";
    }

    private String resign() {
        GameData gameData = getGame(server, gameID);
        if (!gameData.game().isGameInPlay()) {
            throw new ResponseException(400, "Error, game not in play");
        }

        System.out.print("Are you sure? (y|n): ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().toLowerCase();
            if (line.equals("y")) {
                ws.resign(authToken, gameID);
                break;
            } else if (!line.equals("n")) {
                System.out.println("Error: Expected y|n");
            } else {
                // else if it is n, then just continue and don't do anything
                System.out.println("You are still in the game.");
                break;
            }
        }
        this.state = State.GAMEPLAY;
        return "";//""resign";
    }

    private String highlight(String[] parameters) {
        ChessPosition position;
        try {
            String positionString = parameters[0];
            String firstCharacter = String.valueOf(positionString.charAt(0));
            String secondCharacter = String.valueOf(positionString.charAt(1));
            position = new ChessPosition(Integer.parseInt(secondCharacter), letterArray.get(firstCharacter));
        } catch (Exception e) {
            throw new ResponseException(400, "Error: bad input");
        }


        ChessGame game = getGame(server, gameID).game();

        if (game.getBoard().spotEmpty(position)) {
            throw new ResponseException(400, "Error, no piece there");
        }
        return this.getBoardString(server, gameID, perspective, game.validMoves(position), position);
    }

    public Integer getCurrentGameID() { return this.gameID; }

    private ChessPiece.PieceType getPromotion() {
        System.out.print("Promotion piece type: ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            ChessPiece.PieceType ret = getChessPiece(line);
            if (ret == null) {
                System.out.println("Error: Expected pawn|queen|rook|knight|bishop");
            } else {
                return ret;
            }
        }
    }

    private ChessPiece.PieceType getChessPiece(String line) {
        ChessPiece.PieceType ret;
        ret = switch (line.toLowerCase()) {
            case "pawn" -> ChessPiece.PieceType.PAWN;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            default -> null;
        };
        return ret;
    }
}
