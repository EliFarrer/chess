package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();

        int[][] spots = new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        for (int[] spot : spots) {
            ChessPosition newPos = new ChessPosition(position.getRow() + spot[0], position.getColumn() + spot[1]);

            if (canMove(board, position, newPos)) {
                coll.add(new ChessMove(position, newPos, null));
            }

        }
        return coll;
    }
    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return ((board.spotEmpty(newPosition) || board.positionIsNotSameColor(position, newPosition)) && board.inBounds(newPosition));
    }

}
