package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

// this is on the server side.
public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(Session session, String visitorAuthToken) {
        Connection connection = new Connection(visitorAuthToken, session);
        connections.put(visitorAuthToken, connection);
    }

    public void remove(String visitorAuthToken) {
        connections.remove(visitorAuthToken);
    }

    // the server sends ServerMessages to the clients.
    public void broadcast(String excludeVisitorAuthToken, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();

        for (var conn : connections.values()) {
            if (conn.session.isOpen()) {
                if (!conn.visitorAuthToken.equals(excludeVisitorAuthToken)) {
                    conn.send(message.toString());
                }
            } else {
                removeList.add(conn);
            }
        }

        for (var remove : removeList) {
            connections.remove(remove.visitorAuthToken);
        }
    }

    public void broadcastTo(String userAuthToken, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();

        for (var conn : connections.values()) {
            if (conn.session.isOpen()) {
                if (conn.visitorAuthToken.equals(userAuthToken)) {
                    conn.send(message.toString());
                }
            } else {
                removeList.add(conn);
            }
        }

        for (var remove : removeList) {
            connections.remove(remove.visitorAuthToken);
        }
    }


}
