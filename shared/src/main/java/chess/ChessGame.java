package chess;

import java.util.Collection;
import java.util.ArrayList;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board = new ChessBoard();
    private ChessGame.TeamColor teamTurn = TeamColor.WHITE;
    public ChessGame() {
        board.resetBoard();

    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) { this.teamTurn = team; }

    public boolean isMyTurn(TeamColor color) { return teamTurn == color; }

    public void changeTeamTurn() { this.teamTurn = this.teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE; }
    // the two teams in a game
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid move for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);

        // go through all the moves and see if each move would leave the king in check.
        for (ChessMove move : moves) {
            ChessPosition newPosition = move.getEndPosition();
            ChessPiece pieceBeingReplaced = board.getPiece(newPosition);

            // add a temporary piece
            board.addPiece(startPosition, null);
            board.addPiece(newPosition, piece);
            // see if it would leave the king in check
            if (isInCheck(piece.getTeamColor())) {
                validMoves.remove(move);
            }

            // replace the old piece
            board.addPiece(newPosition, pieceBeingReplaced);
            board.addPiece(startPosition, piece);
        }
        return validMoves;
    }


    public boolean hasValidMoves(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.spotEmpty(position)) { continue; }
                ChessPiece piece = board.getPiece(position);

                if (piece.getTeamColor() == teamColor) {
                    if (!validMoves(position).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isMoveLegal(ChessMove move) {
        return validMoves(move.getStartPosition()).contains(move);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece exists here");
        }
        if (isMyTurn(piece.getTeamColor()) && isMoveLegal(move)) {
            board.addPiece(move.getStartPosition(), null);
            // if the promotion piece in move is null, it will keep the same piece, if not it will get the promotion piece
            ChessPiece newPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece() == null ? piece.getPieceType() : move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), newPiece);
        } else {
            throw new InvalidMoveException("Invalid move");
        }
        changeTeamTurn();
    }



    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // this only goes over current scenarios
        // go over every piece that is of opposite color and see if the kings position is an option for any of the other pieces.
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition == null) {
            System.out.println("Your findKing method returned null. You don't have a king.");
            return false;
        }
        return positionIsInCheck(kingPosition, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean validMoves = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.spotEmpty(position)) { continue; }
                ChessPiece piece = board.getPiece(position);
                if (piece.getTeamColor() == teamColor) {    // verify the pieces we look at are on the same team
                    if (!validMoves(position).isEmpty()) {  // if there is a valid move, we are no longer in checkmate
                        validMoves = true;
                    }
                }
            }
        }
        return !validMoves && isInCheck(teamColor);  // checkmate only if there are no valid moves and in check
    }
//    public boolean isInCheckmate(TeamColor teamColor) {
//        // this goes over possible future moves
//        // we only need to see if we can take out attacking pieces in checkmate.
//        ChessPosition kingPosition = findKing(teamColor);
//        if (kingPosition == null) {
//            System.out.println("Your findKing method returned null. You don't have a king.");
//            return false;
//        }
//        ChessPiece kingPiece = board.getPiece(kingPosition);
//
//        Collection<ChessMove> kingMoves = kingPiece.pieceMoves(board, kingPosition); // get the king moves
//        board.addPiece(kingPosition, null); // pretend the king isn't there
//
//        for (ChessMove move : kingMoves) {
//            // get the old piece because we will replace it with a king.
//            ChessPiece oldPiece = board.getPiece(move.getEndPosition());
//            // add a temporary king in the test spot. (we need to do this for the pawn's sake. It won't trigger the diagonal moves unless a king is actually there.
//            ChessPosition tempKingPosition = move.getEndPosition();
//            board.addPiece(tempKingPosition, new ChessPiece(teamColor, ChessPiece.PieceType.KING));
//
//            // if the position is not in check, then the king is not in checkmate
//            if (!positionIsInCheck(move.getEndPosition(), teamColor)) {
//                return false;
//            }
//
//            // if the attacking piece can be taken out, then the king is not in checkmate
//            ArrayList<ChessPosition> attackingPiecePositions = getAttackingPiecePositions(tempKingPosition);
//            if (attackingPiecePositions.size() == 1) {
//                ChessPosition attacker = attackingPiecePositions.getFirst();
//
//                ArrayList<ChessPosition> defendingPiecePositions = getAttackingPiecePositions(attacker);
//                defendingPiecePositions.remove(tempKingPosition);
//                // remove the king position it
//                if (!getAttackingPiecePositions(attacker).isEmpty()) {
//                    // problem is here. If we move the king to a different spot on the board, then the king can technically make a move to attack the pawn.
//                    return false;
//                }
//            }
//
//            // remove the temporary king
//            board.addPiece(move.getEndPosition(), oldPiece);
//        }
//        // add the original king back
//        board.addPiece(kingPosition, kingPiece);
//
//        // if the position itself is not in check, then we are not in checkmate. If after all this, it is in check, that is checkmate.
//        return isInCheck(teamColor);
//
//    }

    // helper function to determine if a certain position is in check
    public boolean positionIsInCheck(ChessPosition position, TeamColor teamColor) {
        // this only goes through current scenarios
        // this takes in a position and the color of the piece.
        // it searches across the whole board to see what pieces of the other color pose a threat
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition attackPosition = new ChessPosition(i, j);
                if (board.spotEmpty(attackPosition)) { continue; }
                ChessPiece attackPiece = board.getPiece(attackPosition);

                if (attackPiece.getTeamColor() != teamColor) {    //verify the opposite color
                    Collection<ChessMove> possibleMoves = attackPiece.pieceMoves(board, attackPosition);
                    if (extractEndPositionFromChessMoves(possibleMoves).contains(position)) { // extracts a collection of end positions. Is position in that list?
                        return true;
                    }
                }
            }
        }
        return false;
    }


//    public ArrayList<ChessPosition> getAttackingPiecePositions(ChessPosition defendPiecePosition) {
//        ArrayList<ChessPosition> attackPositions = new ArrayList<ChessPosition>();
//        ChessPiece defendPiece = board.getPiece(defendPiecePosition);
//        for (int i = 1; i < 9; i++) {
//            for (int j = 1; j < 9; j++) {
//                ChessPosition attackPiecePosition = new ChessPosition(i, j);
//                if (board.spotEmpty(attackPiecePosition)) { continue; }
//                ChessPiece attackPiece = board.getPiece(attackPiecePosition);
//
//                if (defendPiece.getTeamColor() != attackPiece.getTeamColor()) {    //verify the opposite color
//                    Collection<ChessMove> possibleAttackMoves = attackPiece.pieceMoves(board, attackPiecePosition);
//                    if (extractEndPositionFromChessMoves(possibleAttackMoves).contains(defendPiecePosition)) { // extracts a collection of end positions. Is the defendPosition in that list?
//                        attackPositions.add(attackPiecePosition);
//                    }
//                }
//            }
//        }
//        return attackPositions;
//    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        return (!isInCheckmate(teamColor) && !hasValidMoves(teamColor));
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard getBoard() {
        return board;
    }

    private ChessPosition findKing(TeamColor color) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.spotEmpty(position)) { continue; }
                ChessPiece piece = board.getPiece(position);
                if ((piece.getTeamColor() == color) && (piece.getPieceType() == ChessPiece.PieceType.KING)) {
                    return position;
                }
            }
        }
        return null;
    }

    private Collection<ChessPosition> extractEndPositionFromChessMoves(Collection<ChessMove> moves) {
        // this method just extracts the end positions from ChessMoves so it is easier to see if a position is in there.
        ArrayList<ChessPosition> ret = new ArrayList<ChessPosition>();
        for (ChessMove move:moves) {
            ret.add(move.getEndPosition());
        }
        return ret;
    }

}
