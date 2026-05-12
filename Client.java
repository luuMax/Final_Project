import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        try {

            // connect to server computer
            /* Socket socket = new Socket("192.168.1.5", 5000) */
            Socket socket = new Socket("172.18.231.34", 5000);

            System.out.println("Connected to server!");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // receive message
            String message = in.readLine();

            System.out.println("Server says: " + message);

            // send reply
            out.println("Hello from client!");
            GameRunner runner = new GameRunner();
            runner.start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}