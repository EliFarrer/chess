package chess;

import java.util.Collection;

public class KnightMovesCalculator extends HopperPieceCalculator{
    int[][] spots = new int[][]{{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        return super.getMoves(spots, board, position);
    }
}
