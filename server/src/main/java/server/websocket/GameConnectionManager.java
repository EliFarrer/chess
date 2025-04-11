package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionManager {
    public final ConcurrentHashMap<Integer, ConnectionManager> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session, String visitorAuthToken) {
        ConnectionManager manager = connections.get(gameID);
        if (manager == null) {
            manager = new ConnectionManager();
            manager.add(session, visitorAuthToken);
            connections.put(gameID, manager);
        } else {
            manager.add(session, visitorAuthToken);
        }
    }

    public void remove(Integer gameID, String visitorAuthToken) {
        connections.get(gameID).remove(visitorAuthToken);
    }

    public void broadcast(Integer gameId, String visitorAuthToken, ServerMessage message) throws IOException {
        connections.get(gameId).broadcast(visitorAuthToken, message);
    }

    public void broadcastTo(Integer gameId, String visitorAuthToken, ServerMessage message) throws IOException {
        connections.get(gameId).broadcastTo(visitorAuthToken, message);
    }
}
