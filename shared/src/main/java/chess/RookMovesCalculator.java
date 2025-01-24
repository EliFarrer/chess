package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();
        int[][] spots = new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        for (int[] spot : spots) {
            for (int i = 1; i < 9; i++) {
                ChessPosition newPosition = new ChessPosition((position.getRow() + spot[0] * i), (position.getColumn()) + spot[1] * i);
                // if we are out of bounds or the new position is a piece of our same color, break immediately
                if (!board.inBounds(newPosition)) {
                    break;
                }
                if (!board.spotEmpty(newPosition)) {
                    // if we are attacking
                    if (board.positionIsNotSameColor(newPosition, position)) {
                        coll.add(new ChessMove(position, newPosition, null));
                    }
                    break;

                }
                // if no other options, add it
                else {
                    coll.add(new ChessMove(position, newPosition, null));
                }
            }

        }
        return coll;
    }

    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return false;
    }
}
