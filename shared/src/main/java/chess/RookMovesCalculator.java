package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator extends SliderPieceCalculator {
    private final int[][] spots = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        return super.getMoves(spots, board, position);
    }
}
