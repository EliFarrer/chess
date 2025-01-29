package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator extends SliderPieceCalculator {
    private final int[][] spots = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};


    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        return super.getMoves(spots, board, position);
    }


    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return false;
    }
}
