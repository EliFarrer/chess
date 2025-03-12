import server.Server;

// next time split up the data access, server and service things into multiple classes. Start calling them between different classes.
public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            int port = Integer.parseInt(args[0]);
            server.run(port);

        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Put in an integer for the port you want to use.");
        }
    }
}