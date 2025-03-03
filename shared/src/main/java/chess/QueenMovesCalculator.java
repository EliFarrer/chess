package chess;

import java.util.Collection;

public class QueenMovesCalculator extends SliderPieceCalculator {
    // just the bishop and rook combined
    private final int[][] spots = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        return super.getMoves(spots, board, position);
    }

}
