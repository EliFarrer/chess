package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    chess.ChessPiece[][] board;
    // if you are black, everything is in order (except for the queen/king, but that is handled)
    String[] horizontalLabels = {"h", "g", "f", "e", "d", "c", "b", "a"};

    public BoardPrinter(ChessBoard board) {
        this.board = board.board;
    }

    public String getBoardString(boolean whitePerspective) {
        // if whitePerspective == true, then it will flip everything
        var out = new StringBuilder();

        out.append(getHorizontalBorder(whitePerspective));
        if (whitePerspective) {
            for (int i = 7; i >= 0; i--) {
                ChessPiece[] row = board[i];
                out.append(getRowString(row, 7 - i, whitePerspective)).append('\n');
            }
        } else {
            for (int i = 0; i < 8; i++) {
                ChessPiece[] row = board[i];
                out.append(getRowString(row, i, whitePerspective)).append('\n');
            }
        }
        out.append(getHorizontalBorder(whitePerspective));
        return out.toString();
    }

    private String getRowString(chess.ChessPiece[] row, int rowIndex, boolean flip) {
        var out = new StringBuilder();
        out.append(getVerticalBorder(rowIndex, flip));

        if (!flip) {
            for (int j = 7; j >= 0; j--) {
                out.append(setSquare(row[j], rowIndex, j + 1)); // the +1 fixes the weird error we were getting
            }
        } else {
            for (int j = 0; j < 8; j++) {
                out.append(setSquare(row[j], rowIndex, j));
            }
        }
        out.append(getVerticalBorder(rowIndex, flip));
        return out.toString();
    }

    private String setSquare(ChessPiece piece, Integer rowIndex, Integer colIndex) {
        if (piece == null) {
            return getBackgroundColor(EMPTY, rowIndex, colIndex);
        } else {
            return getBackgroundColor(getPieceType(piece), rowIndex, colIndex);
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
