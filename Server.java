import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

            System.out.println("GameCode is: " + GameCode.encode(GameCode.getIpAddress()));

            Socket socket = serverSocket.accept();

            serverSocket.close();

            System.out.println("Client connected.");

            // SERVER IS ALWAYS WHITE
            boolean serverIsWhite = true;

            NetworkManager network = new NetworkManager(socket, serverIsWhite);

            /*
             * IMPORTANT: Send the OPPOSITE color to the client. Server = WHITE
             * Client = BLACK
             */
            network.sendSetup("BLACK");

            System.out.println("Server is WHITE");

            new GameRunner(network).start();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
