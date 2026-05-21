import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server
{

    public static void main(String[] args)
    {
        connect();
    }


    public static void connect()
    {
        try
        {

            ServerSocket serverSocket = new ServerSocket(5000);

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
