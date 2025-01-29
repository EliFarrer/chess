package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position);

//    boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition);
}

