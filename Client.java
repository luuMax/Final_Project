import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        
            String IPlocal = "172.18.231.34";
            int port = 5000;

            // connect to server computer
            /* Socket socket = new Socket("192.168.1.5", 5000) */

        try {
            System.out.println("Connecting to " + IPlocal + ":" + port + "..");

            Socket socket = new Socket(IPlocal, port);

            System.out.println("Connected to server!");

            BufferedReader setupIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String assigned = setupIn.readLine();
            boolean clientIsWhite = assigned.equals("WHITE");

            System.out.println("You are " + (clientIsWhite ? "WHITE" : "BLACK"));
            
            NetworkManager network = new NetworkManager(socket, false); 
            new GameRunner(network).start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}