package websocket.messages;

import model.GameData;

public class ErrorMessage extends ServerMessage {
    String errorMessage;
    public ErrorMessage(ServerMessage.ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
}
