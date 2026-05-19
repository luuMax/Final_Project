import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args)
    {
        // currently testing connections; will fix/connect to mainmenuui later
        // with gamelink approaproately.
        // does not work with modifiers, should rotate board btwn clinet and
        // server
        System.out.println("localhost"); // local
        System.out.println("172.18.231.33"); // Alex
        System.out.println("172.18.231.34"); // neel
        System.out.println("10.18.81.146"); // max
        System.out.println("10.18.81.246"); //also max

        System.out.print("input gamelink: ");
        Scanner scan = new Scanner(System.in);
        String hoster = scan.nextLine();

        connect(hoster); // change to server's IP when on different machines

        scan.close();
    }


    public static void connect(String gamecode)
    {
        try
        {
            String hostIP = GameCode.decode(gamecode);
            Socket socket = new Socket(hostIP, 5000);
            System.out.println("Connected to server.");

            NetworkManager network = new NetworkManager(socket, false); // color
                                                                        // set
                                                                        // by
                                                                        // readSetup
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
