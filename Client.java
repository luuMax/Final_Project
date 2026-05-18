import java.io.IOException;
import java.net.Socket;

public class Client
{

    public static void main(String[] args)
    {
        String localIP = "172.18.231.33";
        localIP = "localhos";
        connect(localIP);
    }




    public static void connect(String hostIP) {
        try
        {

            Socket socket = new Socket(hostIP, 5000);

            System.out.println("Connected to server.");

            // client is ALWAYS opposite color

            boolean clientIsWhite = false;

            NetworkManager network = new NetworkManager(socket, clientIsWhite);

            System.out.println("Client is BLACK");

            GameRunner runner = new GameRunner(network);

            runner.start();

        }

        catch (IOException e)
        {

            e.printStackTrace();
        }
    }
}
