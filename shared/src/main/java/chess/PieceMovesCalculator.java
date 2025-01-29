package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position);

//    public boolean inBounds(ChessPosition position);

//    public boolean spotEmpty(ChessBoard board, ChessPosition position);
boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition);
}
