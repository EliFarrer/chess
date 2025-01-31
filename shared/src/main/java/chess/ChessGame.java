package chess;

import java.util.Collection;

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
        throw new RuntimeException("Not implemented");
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
        // go over every piece that is of opposite color and see if the kings position is an option for any of the other pieces.

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition testPosition = new ChessPosition(i, j);
                if (board.spotEmpty(testPosition)) { continue; }
                ChessPiece testPiece = board.getPiece(testPosition);
                if (testPiece.getTeamColor() != teamColor) {    //verify the opposite color
                    Collection<ChessMove> possibleMoves = testPiece.pieceMoves(board, position);
                    if (possibleMoves.contains(position)) { // if our kingPosition is in the moves of the opposite color, we are in check.
                        return true;
                    }
                }
            }
        }
        return false;
    }}
