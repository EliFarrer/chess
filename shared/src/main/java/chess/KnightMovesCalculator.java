package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();

        int[][] spots = new int[][]{{-2, -1}, {-2, 1}, {-1, 2}, {1, 2}};
        for (int[] spot : spots) {
            ChessPosition newPositionA = new ChessPosition(spot[0] + position.getRow(), spot[1] + position.getColumn());
            ChessPosition newPositionB = new ChessPosition(spot[1] + position.getRow(), spot[0] + position.getColumn());

            if (canMove(board, position, newPositionA)) {
                coll.add(new ChessMove(position, newPositionA, null));
            }
            if (canMove(board, position, newPositionB)) {
                coll.add(new ChessMove(position, newPositionB, null));
            }
        }
        return coll;
    }
    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return ((board.spotEmpty(newPosition) || board.positionIsNotSameColor(position, newPosition)) && board.inBounds(newPosition));
    }
}
