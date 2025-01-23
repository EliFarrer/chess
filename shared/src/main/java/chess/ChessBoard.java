package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] board = new ChessPiece[8][8]; // why we are using 9, I dunno, but the first row is all null, and each column starts with null after that...
    public ChessBoard() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (!inBounds(position)) {
            return null;
        }
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    public boolean inBounds(ChessPosition position) {
        return (((position.getRow() >= 1) && (position.getRow() <= 8)) && ((position.getColumn() >= 1) && (position.getColumn() <= 8)));
    }

    public boolean spotEmpty(ChessPosition position) {
        return this.getPiece(position) == null;
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        /*
        Row 2 and 7 are all pawns
        */
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);

        for (int i = 0; i < 8; i++) {
            board[1][i] = whitePawn;
            board[6][i] = blackPawn;
        }

        board[0][0] = whiteRook; board[0][7] = whiteRook;
        board[7][0] = blackRook; board[7][7] = blackRook;

        board[0][1] = whiteKnight; board[0][6] = whiteKnight;
        board[7][1] = blackKnight; board[7][6] = blackKnight;

        board[0][2] = whiteBishop; board[0][5] = whiteBishop;
        board[7][2] = blackBishop; board[7][5] = blackBishop;

        board[0][3] = whiteQueen; board[0][4] = whiteKing;
        board[7][3] = blackQueen; board[7][4] = blackKing;

        // set the nulls
        for (int row = 2; row < 6; row ++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
//        for (ChessGame.TeamColor color : ChessGame.TeamColor.values()) {
//            for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
//
//            }
//        }



//        board[1][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

    }


}
