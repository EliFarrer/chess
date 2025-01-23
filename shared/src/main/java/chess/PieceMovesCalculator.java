package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

//    public boolean inBounds(ChessPosition position);

//    public boolean spotEmpty(ChessBoard board, ChessPosition position);
    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition);
}
