package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage{
    String game;
    public LoadGameMessage(ServerMessage.ServerMessageType type, String game) {
        super(type);
        this.game = game;
    }
}
