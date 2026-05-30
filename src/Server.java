package src;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Represents the host of the game, and allows the client class to connect using server sockets.
 */
public class Server
{

    /**
     * Listens for exactly one incoming connection on port 4999, randomly
     * assigns colors, sends the client their color, then hands off to
     * GameRunner. The server socket is closed immediately after the first
     * client connects.
     */
    public static void connect()
    {
        try
        {

            ServerSocket serverSocket = new ServerSocket(4999);

            System.out.println("Server started. Waiting for connection...");
            System.out.println("GameCode is: ");

            Socket socket = serverSocket.accept();
            serverSocket.close();

            System.out.println("Client connected.");

            boolean serverIsWhite = new Random().nextBoolean();

            NetworkManager network = new NetworkManager(socket, serverIsWhite);

            network.sendSetup(serverIsWhite ? "BLACK" : "WHITE");

            System.out.println("Server is " + (serverIsWhite ? "WHITE" : "BLACK"));

            new GameRunner(network).start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
