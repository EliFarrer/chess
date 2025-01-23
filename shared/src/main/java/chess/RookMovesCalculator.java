package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();
        for (int i = position.getRow()+1; i < 9; i++) {
            ChessPosition newPos = new ChessPosition(i, position.getColumn());
            if (canMove(board, position, newPos)) {
                coll.add(new ChessMove(position, newPos, null));
            } else { break; }
        }
        for (int i = position.getColumn()+1; i < 9; i++) {
            ChessPosition newPos = new ChessPosition(position.getRow(), i);
            if (canMove(board, position, newPos)) {
                coll.add(new ChessMove(position, newPos, null));
            } else { break; }
        }
        for (int i = position.getRow()-1; i > 0; i--) {
            ChessPosition newPos = new ChessPosition(i, position.getColumn());
            if (canMove(board, position, newPos)) {
                coll.add(new ChessMove(position, newPos, null));
            } else { break; }
        }
        for (int i = position.getColumn()-1; i > 0; i--) {
            ChessPosition newPos = new ChessPosition(position.getRow(), i);
            if (canMove(board, position, newPos)) {
                coll.add(new ChessMove(position, newPos, null));
            } else { break; }
        }
        return coll;
    }


    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return ((board.spotEmpty(newPosition) || board.positionIsNotSameColor(position, newPosition)) && (board.inBounds(newPosition)));
    }
}
