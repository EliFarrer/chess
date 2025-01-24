package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();
        // white
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() == 2) {
                // first time, two options to move
            }
        }
        // black
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (position.getRow() == 7) {
                // first time, two options to move
            }
        }
//        addPromotionPieces(coll, new ChessPosition(1,1), new ChessPosition(2,2));
        //first move has two options
        //direction is based off of color
        //promotions
        //diagonal capturing
        //shut out
        //blocked by another piece
        return coll;
    }

    public boolean canMove(ChessBoard board, ChessPosition position, ChessPosition newPosition) {
        return ((board.spotEmpty(newPosition) || board.positionIsNotSameColor(position, newPosition)) && board.inBounds(newPosition));
    }

    public void addPromotionPieces(ArrayList<ChessMove> array, ChessPosition position, ChessPosition newPosition) {
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
    }

    public boolean canMoveForward(ChessBoard board, ChessPosition position) {
        // this sees if the pawn can move directly forward without checking promotion. It does check bounds.
        // pawns can only move forward if the spot ahead of them is empty
        // need to check promotion
        int newRow;
        if (board.isPositionWhite(position)) {
            newRow = position.getRow() + 1;
        } else {
            newRow = position.getRow() - 1;
        }
        ChessPosition newPosition = new ChessPosition(newRow, position.getColumn());
        return board.spotEmpty(newPosition) && board.inBounds(newPosition);
    }
    public boolean canMoveDiagonalEast(ChessBoard board, ChessPosition position) {
        // need to check promotion and out of bounds
        int newRow;
        int newCol = position.getColumn() - 1;
        if (board.isPositionWhite(position)) {
            newRow = position.getRow() + 1;
        } else {
            newRow = position.getRow() - 1;
        }
        return board.spotEmpty(new ChessPosition(newRow, newCol));
    }
}
