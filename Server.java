import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
//Test: ipconfig getifaddr en0
    public static void main(String[] args) {
        connect(5000);
    }

    public static void connect(int port) {
        try {
            System.out.println("Starting server on port 5000...");
            System.out.println("Your IP (run 'ipconfig getifaddr en0' on Mac or 'ipconfig' on Windows)");

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for opponent to connect...");

            Socket socket = serverSocket.accept(); // blocks here until client joins
            serverSocket.close(); // only need one game, stop listening

            System.out.println("Opponent connected! Starting game...");

            boolean serverIsWhite = new Random().nextBoolean();
            PrintWriter setupOut = new PrintWriter(socket.getOutputStream(), true);
            setupOut.println(serverIsWhite ? "WHITE" : "BLACK");

            System.out.println("You are " + (serverIsWhite ? "WHITE" : "BLACK"));

            NetworkManager network = new NetworkManager(socket, true); // true = isServer = White
            new GameRunner(network).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}