import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server
{

    public static void main(String[] args)
    {

        try
        {

            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("Server started.");
            System.out.println("Waiting for connection...");

            Socket socket = serverSocket.accept();

            System.out.println("Client connected.");

            // randomly assign server color

            /* boolean serverIsWhite = new Random().nextBoolean(); */
            boolean serverIsWhite = true;

            NetworkManager network = new NetworkManager(socket, serverIsWhite);

            System.out.println("Server is " + (serverIsWhite ? "WHITE" : "BLACK"));

            GameRunner runner = new GameRunner(network);

            runner.start();

        }

        catch (IOException e)
        {

            e.printStackTrace();
        }
    }
}
