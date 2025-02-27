import dataaccess.UserDAO;
import server.Server;
import service.ClearService;
import dataaccess.MemoryUserDAO;

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

//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
    }
}