package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();

        return coll;
    }
    public boolean inBounds(ChessPosition position) {
        return (position.getRow() >= 1) & (position.getRow() <= 8) & (position.getColumn() >= 1) & (position.getColumn() <= 8);
    }
}
