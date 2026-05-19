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

    public void sendModifierOptions(Modifier.Type[] options)
{
    out.println(
        options[0].name() + "," +
        options[1].name() + "," +
        options[2].name()
    );
}

public Modifier.Type[] receiveModifierOptions()
{
    try
    {
        String line = in.readLine();

        String[] p = line.split(",");

        Modifier.Type[] options = new Modifier.Type[3];

        for (int i = 0; i < 3; i++)
        {
            options[i] = Modifier.Type.valueOf(p[i]);
        }

        return options;
    }
    catch (IOException e)
    {
        return null;
    }
}

public void sendModifierChoice(Modifier.Type type)
{
    out.println(type.name());
}

public Modifier.Type receiveModifierChoice()
{
    try
    {
        String line = in.readLine();

        return Modifier.Type.valueOf(line);
    }
    catch (IOException e)
    {
        return null;
    }
}

    public boolean getIsWhite() { return isWhite; }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}