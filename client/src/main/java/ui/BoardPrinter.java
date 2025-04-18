package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    // if you are black, everything is in order (except for the queen/king, but that is handled)
    String[] horizontalLabels = {"h", "g", "f", "e", "d", "c", "b", "a"};

    public String getBoardString(chess.ChessPiece[][] board, boolean whitePerspective, Collection<ChessMove> ends, ChessPosition start) {
        // if whitePerspective == true, then it will flip everything
        var out = new StringBuilder();

        out.append(getHorizontalBorder(whitePerspective));
        if (whitePerspective) {
            for (int i = 7; i >= 0; i--) {
                ChessPiece[] row = board[i];
                out.append(getRowString(row, 7 - i, whitePerspective, ends, start)).append('\n');
            }
        } else {
            for (int i = 0; i < 8; i++) {
                ChessPiece[] row = board[i];
                out.append(getRowString(row, i, whitePerspective, ends, start)).append('\n');
            }
        }
        out.append(getHorizontalBorder(whitePerspective));
        return out.toString();
    }

    private String getRowString(chess.ChessPiece[] row, int rowIndex, boolean flip, Collection<ChessMove> ends, ChessPosition start) {
        var out = new StringBuilder();
        out.append(getVerticalBorder(rowIndex, flip));

        if (!flip) {
            for (int j = 7; j >= 0; j--) {
                out.append(setSquare(row[j], rowIndex, j + 1, flip, ends, start)); // the +1 fixes the weird error we were getting
            }
        } else {
            for (int j = 0; j <= 7; j++) {
                out.append(setSquare(row[j], rowIndex, j, flip, ends, start));
            }
        }
        out.append(getVerticalBorder(rowIndex, flip));
        return out.toString();
    }

    private String setSquare(ChessPiece piece, Integer row, Integer col, boolean flip, Collection<ChessMove> ends, ChessPosition start) {
        if (ends == null && start == null) {
            return getBackgroundColor(piece == null ? EMPTY : getPieceType(piece), row, col);
        } else {
            return getBackgroundColorHighlight(piece == null ? EMPTY : getPieceType(piece), row, col, flip, ends, start);
        }
    }


    private String getBackgroundColor(String pieceType, int rowIndex, int colIndex) {
        var out = new StringBuilder();
        // if it is even, we start with the darker color
        if (rowIndex % 2 == colIndex % 2) {
            out.append(SET_BG_COLOR_LIGHT_TAN);
        } else {
            out.append(SET_BG_COLOR_DARK_TAN);
        }
        out.append(pieceType).append(RESET_BG_COLOR);
        return out.toString();
    }

    private String getBackgroundColorHighlight(String pieceType, int row, int col, boolean flip, Collection<ChessMove> ends, ChessPosition start) {
        var out = new StringBuilder();
        ChessPosition currentPosition;
        if (flip) {
            currentPosition = new ChessPosition(8 - row, col + 1);
        } else {
            currentPosition = new ChessPosition(row + 1, col);
        }
        ChessMove testMove = new ChessMove(start, currentPosition, null);
        if (currentPosition.equals(start)) {
            out.append(SET_BG_COLOR_YELLOW);
        } else if (ends.contains(testMove)) {
            out.append(SET_BG_COLOR_GREEN);
        } else {
            // if it is even, we start with the darker color
            if (row % 2 == col % 2) {
                out.append(SET_BG_COLOR_LIGHT_TAN);
            } else {
                out.append(SET_BG_COLOR_DARK_TAN);
            }
        }
        out.append(pieceType).append(RESET_BG_COLOR);
        return out.toString();
    }

    private String getPieceType(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return SET_TEXT_COLOR_WHITE + switch (piece.getPieceType()) {
                case ChessPiece.PieceType.BISHOP -> WHITE_BISHOP;
                case ChessPiece.PieceType.QUEEN -> WHITE_QUEEN;
                case ChessPiece.PieceType.KING -> WHITE_KING;
                case ChessPiece.PieceType.KNIGHT -> WHITE_KNIGHT;
                case ChessPiece.PieceType.ROOK -> WHITE_ROOK;
                case ChessPiece.PieceType.PAWN -> WHITE_PAWN;
            };
        } else {
            return SET_TEXT_COLOR_BLACK + switch (piece.getPieceType()) {
                case ChessPiece.PieceType.BISHOP -> BLACK_BISHOP;
                case ChessPiece.PieceType.QUEEN -> BLACK_QUEEN;
                case ChessPiece.PieceType.KING -> BLACK_KING;
                case ChessPiece.PieceType.KNIGHT -> BLACK_KNIGHT;
                case ChessPiece.PieceType.ROOK -> BLACK_ROOK;
                case ChessPiece.PieceType.PAWN -> BLACK_PAWN;
            };
        }
    }

    private String getHorizontalBorder(boolean flip) {
        var out = new StringBuilder();
        out.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE + EMPTY);
        if (flip) {
            for (int i = 7; i >= 0; i--) {
                out.append(" ").append(horizontalLabels[i]).append(" ");
            }
        } else {
            for (int i = 0; i < 8; i++) {
                out.append(" ").append(horizontalLabels[i]).append(" ");
            }
        }
        out.append(EMPTY + RESET_BG_COLOR + RESET_TEXT_COLOR).append('\n');
        return out.toString();
    }

    private String getVerticalBorder(int rowIndex, boolean flip) {
        if (flip) {
            rowIndex = 8 - rowIndex;
        } else {
            rowIndex++;
        }
        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE+ " " + rowIndex + " " + RESET_BG_COLOR + RESET_TEXT_COLOR;
    }
}
