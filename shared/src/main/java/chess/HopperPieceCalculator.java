package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class HopperPieceCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> getMoves(int[][] spots, ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();

        for (int[] spot : spots) {
            ChessPosition newPositionA = new ChessPosition(spot[0] + position.getRow(), spot[1] + position.getColumn());

            if (canMove(board, position, newPositionA)) {
                coll.add(new ChessMove(position, newPositionA, null));
            }

        }
        return coll;
    }

    private boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return ((board.spotEmpty(newPosition) || board.positionIsNotSameColor(position, newPosition)) && board.inBounds(newPosition));
    }
}
