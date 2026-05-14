import java.io.*;
import java.net.*;

public class NetworkManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public boolean isWhite;

    public NetworkManager(Socket socket, boolean isWhite) throws IOException {
        this.socket  = socket;
        this.isWhite = isWhite;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("[Network] Playing as " + (isWhite ? "WHITE" : "BLACK"));
    }

    public void sendSetup(String msg) {
        out.println(msg);
    }

    public String readSetup() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void sendMove(int fromRow, int fromCol, int toRow, int toCol) {
        out.println(fromRow + "," + fromCol + "," + toRow + "," + toCol);
    }

    public int[] receiveMove() {
        try {
            String line = in.readLine();
            if (line == null) return null;
            String[] p = line.split(",");
            return new int[]{
                Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                Integer.parseInt(p[2]), Integer.parseInt(p[3])
            };
        } catch (IOException e) {
            return null;
        }
    }

    public boolean getIsWhite() { return isWhite; }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}