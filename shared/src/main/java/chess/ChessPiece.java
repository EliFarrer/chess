package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", pieceColor=" + pieceColor +
                '}';
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rtrn;
        PieceMovesCalculator calculator;
        switch (type) {
            case PieceType.KNIGHT:
                calculator = new KnightMovesCalculator();
                rtrn = calculator.getMoves(board, myPosition);
                break;
            case PieceType.KING:
                calculator = new KingMovesCalculator();
                rtrn = calculator.getMoves(board, myPosition);
                break;
            case PieceType.QUEEN:
                calculator = new QueenMovesCalculator();
                rtrn = calculator.getMoves(board, myPosition);
                break;
            case PieceType.ROOK:
                calculator = new RookMovesCalculator();
                rtrn = calculator.getMoves(board, myPosition);
                break;
            case PieceType.PAWN:
                calculator = new PawnMovesCalculator();
                rtrn = calculator.getMoves(board, myPosition);
                break;
            case PieceType.BISHOP:
                calculator = new BishopMovesCalculator();
                rtrn = calculator.getMoves(board, myPosition);
                break;
            default:
                rtrn = null;
        }
        return rtrn;
//        throw new RuntimeException("Not implemented");
    }
}
