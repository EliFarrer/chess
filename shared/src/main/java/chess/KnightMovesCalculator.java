package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();

        int[][] spots = new int[][]{{-2, -1}, {-2, 1}, {-1, 2}, {1, 2}};
        for (int[] spot : spots) {
            ChessPosition newPositionA = new ChessPosition(spot[0] + position.getRow(), spot[1] + position.getColumn());
            ChessPosition newPositionB = new ChessPosition(spot[1] + position.getRow(), spot[0] + position.getColumn());

            if ((board.getPiece(newPositionA) == null) & inBounds(newPositionA)) {
                coll.add(new ChessMove(position, newPositionA, null));
            }
            if ((board.getPiece(newPositionB) == null) & inBounds(newPositionA)) {
                coll.add(new ChessMove(position, newPositionB, null));
            }
        }
        return coll;
    }

    public boolean inBounds(ChessPosition position) {
        return (position.getRow() >= 1) & (position.getRow() <= 8) & (position.getColumn() >= 1) & (position.getColumn() <= 8);
    }

}
