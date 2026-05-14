import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverIP = "172.18.231.33";
        serverIP = "localhost"; // change to server's IP
        int port = 5000;

        try {
            System.out.println("Connecting to " + serverIP + ":" + port + "...");
            Socket socket = new Socket(serverIP, port);
            System.out.println("Connected!");

            // isWhite is temporary false, readSetup sets the real value
            NetworkManager network = new NetworkManager(socket, false);
            String assigned = network.readSetup();
            network.isWhite = assigned.equals("WHITE");

            System.out.println("You are " + (network.isWhite ? "WHITE" : "BLACK"));
            new GameRunner(network).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}