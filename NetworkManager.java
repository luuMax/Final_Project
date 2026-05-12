import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.random.*;

public class NetworkManager
{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public boolean isWhite = new Random().nextBoolean(); // white = true; server color


    public NetworkManager(Socket socket, boolean isWhite) throws IOException {
        this.socket = socket;
        this.isWhite = isWhite;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Network playing as " + (isWhite ? "WHITE" : "BLACK"));
    }
}
