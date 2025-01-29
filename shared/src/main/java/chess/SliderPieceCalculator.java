package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class SliderPieceCalculator implements PieceMovesCalculator {
    private int[][] spots;

//    SliderPieceCalculator(int[][] spots) {
//        this.spots = spots;
//    }

    public void setSpots(int[][] spots) {
        this.spots = spots;
    }

    public Collection<ChessMove> getMoves(int[][] spots, ChessBoard board, ChessPosition position) {
        setSpots(spots);
        var coll = new ArrayList<ChessMove>();

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
}