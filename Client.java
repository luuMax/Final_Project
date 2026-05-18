import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        //currently testing connections; will fix/connect to mainmenuui later with gamelink approaproately. 
        System.out.println("localhost");
        System.out.println("172.18.231.34");
        System.out.println("10.18.81.146");
        System.out.println("input gamelink: ");
        Scanner scan = new Scanner(System.in);
        String hoster = scan.nextLine();

        connect(hoster); // change to server's IP when on different machines
    }

    public static void connect(String hostIP) {
        try {
            Socket socket = new Socket(hostIP, 5000);
            System.out.println("Connected to server.");

            NetworkManager network = new NetworkManager(socket, false); // color set by readSetup
            String assigned = network.readSetup();
            network.isWhite = assigned.equals("WHITE");

            System.out.println("Client is " + (network.isWhite ? "WHITE" : "BLACK"));

            new GameRunner(network).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}