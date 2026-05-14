import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("Starting server on port 5000...");
            System.out.println("Find your IP: run 'ipconfig getifaddr en0' on Mac, 'ipconfig' on Windows");

            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Waiting for opponent to connect...");

            Socket socket = serverSocket.accept();
            serverSocket.close();
            System.out.println("Opponent connected!");

            boolean serverIsWhite = new Random().nextBoolean();
            NetworkManager network = new NetworkManager(socket, serverIsWhite);
            network.sendSetup(serverIsWhite ? "WHITE" : "BLACK");

            System.out.println("You are " + (serverIsWhite ? "WHITE" : "BLACK"));
            new GameRunner(network).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}