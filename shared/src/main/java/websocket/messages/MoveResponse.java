package websocket.messages;

import chess.ChessGame;
import chess.ChessMove;

public record MoveResponse(ChessGame game, ChessMove move) {
}
