package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionManager {
    public final ConcurrentHashMap<Integer, ConnectionManager> connectionMap = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session, String visitorAuthToken) {
        ConnectionManager manager = connectionMap.get(gameID);
        if (manager == null) {
            manager = new ConnectionManager();
            manager.add(session, visitorAuthToken);
            connectionMap.put(gameID, manager);
        } else {
            manager.add(session, visitorAuthToken);
        }
    }

    public void remove(Integer gameID, String visitorAuthToken) {
        connectionMap.get(gameID).remove(visitorAuthToken);
    }

    public void broadcast(Integer gameId, String visitorAuthToken, ServerMessage message) throws IOException {
        connectionMap.get(gameId).broadcast(visitorAuthToken, message);
    }

    public void broadcastTo(Integer gameId, String visitorAuthToken, ServerMessage message) throws IOException {
        connectionMap.get(gameId).broadcastTo(visitorAuthToken, message);
    }
}
