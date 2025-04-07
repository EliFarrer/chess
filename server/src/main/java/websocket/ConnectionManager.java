package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

// this is on the server side.
public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        Connection connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    // the server sends ServerMessages to the clients.
    public void broadcast(String excludeVisitorName, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();

        for (var conn : connections.values()) {
            if (conn.session.isOpen()) {
                if (!conn.visitorName.equals(excludeVisitorName)) {
                    conn.send(message.toString());
                }
            } else {
                removeList.add(conn);
            }
        }

        for (var remove : removeList) {
            connections.remove(remove.visitorName);
        }
    }


}
