import java.io.IOException;
import java.net.Socket;

/**
 * Represents the connecting player to a game
 */
public class Client
{

    /**
     * Decodes the game code into an IP address, connects to the host on port
     * 4999, receives the color assignment, then hands off to GameRunner.
     *
     * @param gamecode
     *            the alphanumeric code shared by the host, encoding their IP
     *            address in base-36
     */
    public static void connect(String gamecode)
    {
        try
        {
            String hostIP = GameCode.decode(gamecode);
            Socket socket = new Socket(hostIP, 4999);
            System.out.println("Connected to server.");

            NetworkManager network = new NetworkManager(socket, false);

            String assigned = network.readSetup();
            network.isWhite = assigned.equals("WHITE");

            System.out.println("Client is " + (network.isWhite ? "WHITE" : "BLACK"));

            new GameRunner(network).start();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
