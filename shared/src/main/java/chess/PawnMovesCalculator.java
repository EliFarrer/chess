package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        var coll = new ArrayList<ChessMove>();
        /* getForwardMove returns an ArrayList of ChessMoves. So do the diagonal moves.
        Within those, we check in range and validate promotion
        */
        coll.addAll(getForwardMoves(board, position));
        coll.addAll(getDiagonalMoves(board, position, "EAST"));
        coll.addAll(getDiagonalMoves(board, position, "WEST"));
        coll.addAll(getForwardMoveDouble(board, position));
        return coll;
    }

    public void addPromotionPieces(ArrayList<ChessMove> array, ChessPosition position, ChessPosition newPosition) {
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
        array.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
    }

    public ArrayList<ChessMove> getForwardMoves(ChessBoard board, ChessPosition position) {
        // this sees if the pawn can move directly forward without checking promotion. It does check bounds and promotions
        // pawns can only move forward if the spot ahead of them is empty
        // need to check promotion
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        int newRow;
        boolean promotion = false;
        if (board.isPositionWhite(position)) {
            newRow = position.getRow() + 1;
            if (newRow == 8) {
                promotion = true;
            }
        } else {
            newRow = position.getRow() - 1;
            if (newRow == 1) {
                promotion = true;
            }
        }
        ChessPosition newPosition = new ChessPosition(newRow, position.getColumn());

        if (board.spotEmpty(newPosition) && board.inBounds(newPosition)) {
            if (promotion) {
                addPromotionPieces(validMoves, position, newPosition);
            } else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        return validMoves;
    }

    public ArrayList<ChessMove> getDiagonalMoves(ChessBoard board, ChessPosition position, String direction) {
        // need to check promotion and out of bounds
        int newCol = position.getColumn();

        if (direction.equals("EAST")) {
            newCol -= 1;
        } else {
            newCol += 1;
        }
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        int newRow;
        boolean promotion = false;
        if (board.isPositionWhite(position)) {
            newRow = position.getRow() + 1;
            if (newRow == 8) {
                promotion = true;
            }
        } else {
            newRow = position.getRow() - 1;
            if (newRow == 1) {
                promotion = true;
            }
        }
        ChessPosition newPosition = new ChessPosition(newRow, newCol);

        if (board.spotEmpty(newPosition)) {
            return validMoves;
        }
        if (board.positionIsNotSameColor(position, newPosition) && board.inBounds(newPosition)) {
            if (promotion) {
                addPromotionPieces(validMoves, position, newPosition);
            } else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        return validMoves;
    }
    public ArrayList<ChessMove> getForwardMoveDouble(ChessBoard board, ChessPosition position) {
        // this sees if the pawn can move directly forward without checking promotion. It does check bounds and promotions
        // pawns can only move forward if the spot ahead of them is empty
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        int newRow1;
        int newRow2;
        boolean jump = false;
        if (board.isPositionWhite(position)) {
            newRow1 = position.getRow() + 1;
            newRow2 = position.getRow() + 2;
            if (position.getRow() == 2) {
                jump = true;
            }
        } else {
            newRow1 = position.getRow() - 1;
            newRow2 = position.getRow() - 2;
            if (position.getRow() == 7) {
                jump = true;
            }
        }

        if (jump) {
            ChessPosition newPosition1 = new ChessPosition(newRow1, position.getColumn());
            ChessPosition newPosition2 = new ChessPosition(newRow2, position.getColumn());

            if (board.spotEmpty(newPosition1) && board.spotEmpty(newPosition2)) {
                validMoves.add(new ChessMove(position, newPosition2, null));
            }
        }

        return validMoves;
    }
}