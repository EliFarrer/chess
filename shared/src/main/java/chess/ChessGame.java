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


    public boolean hasNoValidMoves(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.spotEmpty(position)) { continue; }
                ChessPiece piece = board.getPiece(position);

                if (piece.getTeamColor() == teamColor) {
                    if (!validMoves(position).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
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

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition attackPosition = new ChessPosition(i, j);
                if (board.spotEmpty(attackPosition)) { continue; }
                ChessPiece attackPiece = board.getPiece(attackPosition);

                if (attackPiece.getTeamColor() != teamColor) {    //verify the opposite color
                    Collection<ChessMove> possibleMoves = attackPiece.pieceMoves(board, attackPosition);
                    if (extractEndPositionFromChessMoves(possibleMoves).contains(kingPosition)) { // extracts a collection of end positions. Is position in that list?
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return hasNoValidMoves(teamColor) && isInCheck(teamColor);  // checkmate only if there are no valid moves and in check
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        return (!isInCheckmate(teamColor) && hasNoValidMoves(teamColor));
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
        ArrayList<ChessPosition> ret = new ArrayList<>();
        for (ChessMove move:moves) {
            ret.add(move.getEndPosition());
        }
        return ret;
    }

}
