package chess;

import java.util.Collection;

public class KingMovesCalculator extends HopperPieceCalculator{
    int[][] spots = new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-1, 0}, {0, -1}, {1, 0}, {0, 1}};

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        return super.getMoves(spots, board, position);
    }
}
