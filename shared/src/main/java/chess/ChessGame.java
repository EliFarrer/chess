package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;


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

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    // if we can prevent the king from being in check or checkmate, we have to, that is one of the moves we need
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // if it isn't the turn
        // or if it puts the king in check
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
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
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition == null) {
            System.out.println("Your findKing method returned null. You don't have a king.");
            return false;
        }

        ChessPiece kingPiece = board.getPiece(kingPosition);

        Collection<ChessMove> kingMoves = kingPiece.pieceMoves(board, kingPosition);
        for (ChessMove move : kingMoves) {
            if (!positionIsInCheck(move.getEndPosition(), teamColor)) {
                return false;
            }
        }
        // if the position itself is not in check, then we are not in checkmate. If after all this, it is in check, that is checkmate.
        return isInCheck(teamColor);

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
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

    public boolean positionIsInCheck(ChessPosition position, TeamColor teamColor) {
        // this takes in a position and the color of the piece.
        // it searches across the whole board to see what pieces of the other color pose a threat
        //4,5 is the problem
        // pawn at 5, 4 should be the problem
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition attackPosition = new ChessPosition(i, j);
                if (board.spotEmpty(attackPosition)) { continue; }
                ChessPiece attackPiece = board.getPiece(attackPosition);


                // things get kind of weird for the pawn. In check, the king can move to a different spot, and if that spot is near an attacking pawn, then the pawn can attack, but it is all hypothetical. The king is not actually there. So we will simulate the king being there by creating a king and putting it in all of these positions.
                var tstpos = new ChessPosition(4, 5);
                if ((i == 5 && j == 4) && (Objects.equals(tstpos, position))) {
                    System.out.println("Breakpoint");
                }

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


    private Collection<ChessPosition> extractEndPositionFromChessMoves(Collection<ChessMove> moves) {
        // this method just extracts the end positions from ChessMoves so it is easier to see if a position is in there.
        ArrayList<ChessPosition> ret = new ArrayList<ChessPosition>();
        for (ChessMove move:moves) {
            ret.add(move.getEndPosition());
        }
        return ret;
    }

}
