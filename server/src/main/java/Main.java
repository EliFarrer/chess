import dataaccess.DataAccess;
import server.Server;
import service.Service;
import dataaccess.MemoryDAO;


public class Main {
    public static void main(String[] args) {
        try {

            DataAccess dataaccess = new MemoryDAO();

            Service service = new Service(dataaccess);
            Server server = new Server(service);
            int port = Integer.parseInt(args[0]);
            server.run(port);

        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Put in an integer for the port you want to use.");
        }

//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
    }
}