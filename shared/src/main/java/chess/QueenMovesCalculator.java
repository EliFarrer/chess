package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends SliderPieceCalculator {
    private final int[][] spots = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}}; // just the bishop and rook combined

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        return super.getMoves(spots, board, position);
    }

    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return false;
    }
}
