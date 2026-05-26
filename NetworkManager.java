import java.io.*;
import java.net.*;

public class NetworkManager
{
    private Socket         socket;
    private PrintWriter    out;
    private BufferedReader in;
    public boolean         isWhite;

    public NetworkManager(Socket socket, boolean isWhite)
        throws IOException
    {
        this.socket = socket;
        this.isWhite = isWhite;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    public void sendSetup(String msg)
    {
        out.println(msg);
    }


    public String readSetup()
    {
        try
        {
            return in.readLine();
        }
        catch (IOException e)
        {
            return null;
        }
    }


    public void sendMove(int fromRow, int fromCol, int toRow, int toCol)
    {
        out.println(fromRow + "," + fromCol + "," + toRow + "," + toCol);
    }


    public int[] receiveMove()
    {
        try
        {
            String line = in.readLine();
            if (line == null)
                return null;
            String[] p = line.split(",");
            return new int[] { Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                Integer.parseInt(p[2]), Integer.parseInt(p[3]) };
        }
        catch (IOException e)
        {
            return null;
        }
    }


    public void sendModifierOptions(Modifier.Type[] options)
    {
        out.println(options[0].name() + "," + options[1].name() + "," + options[2].name());
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

    // Sends everything needed to reconstruct a modifier on the other side:
    // type, turnsRemaining, pieceRow, pieceCol, affectedRow, affectedCol
    // pieceRow/pieceCol = -1 if no piece target
    // affectedRow/affectedCol = -1 if no square target
    public void sendModifier(Modifier m)
    {
        int pieceRow = -1;
        int pieceCol = -1;
        if (m.getAffectedPiece() != null)
        {
            pieceRow = m.getAffectedPiece().getRow();
            pieceCol = m.getAffectedPiece().getCol();
        }
        out.println(
            m.getType().name() + "," + m.getTurnsRemaining() + "," + pieceRow + "," + pieceCol + ","
                + m.getAffectedRow() + "," + m.getAffectedCol());
    }


    public void sendModifierData(
        Modifier.Type type,
        int turnsRemaining,
        int pieceRow,
        int pieceCol,
        int affectedRow,
        int affectedCol)
    {
        out.println(
            type.name() + "," + turnsRemaining + "," + pieceRow + "," + pieceCol + ","
                + affectedRow + "," + affectedCol);
    }


    public void sendResurrectionData(Piece piece, int row, int col)
    {
        out.println(
            Modifier.Type.RESURRECTION.name() + ",0,-1,-1," + row + "," + col + ","
                + piece.getType().name() + "," + piece.getSide().name());
    }

    // Returns a ModifierData record — raw data only, no Piece object
    // GameRunner resolves the piece from the board using pieceRow/pieceCol
    public ModifierData receiveModifier()
    {
        try
        {
            String line = in.readLine();
            if (line == null)
                return null;
            String[] p = line.split(",");
            Modifier.Type type = Modifier.Type.valueOf(p[0]);
            int turnsRemaining = Integer.parseInt(p[1]);
            int pieceRow = Integer.parseInt(p[2]);
            int pieceCol = Integer.parseInt(p[3]);
            int affectedRow = Integer.parseInt(p[4]);
            int affectedCol = Integer.parseInt(p[5]);
            Piece.Type revivedPieceType = p.length > 6 ? Piece.Type.valueOf(p[6]) : null;
            Piece.Side revivedPieceSide = p.length > 7 ? Piece.Side.valueOf(p[7]) : null;
            return new ModifierData(
                type,
                turnsRemaining,
                pieceRow,
                pieceCol,
                affectedRow,
                affectedCol,
                revivedPieceType,
                revivedPieceSide);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean getIsWhite()
    {
        return isWhite;
    }

    public void close()
    {
        try
        {
            socket.close();
        }
        catch (IOException ignored)
        {
        }
    }

    // Simple data carrier w/out game logic
    public static class ModifierData
    {
        public final Modifier.Type type;
        public final int           turnsRemaining;
        public final int           pieceRow;
        public final int           pieceCol;
        public final int           affectedRow;
        public final int           affectedCol;
        public final Piece.Type    revivedPieceType;
        public final Piece.Side    revivedPieceSide;

        public ModifierData(
            Modifier.Type type,
            int turnsRemaining,
            int pieceRow,
            int pieceCol,
            int affectedRow,
            int affectedCol)
        {
            this(type, turnsRemaining, pieceRow, pieceCol, affectedRow, affectedCol, null, null);
        }

        public ModifierData(
            Modifier.Type type,
            int turnsRemaining,
            int pieceRow,
            int pieceCol,
            int affectedRow,
            int affectedCol,
            Piece.Type revivedPieceType,
            Piece.Side revivedPieceSide)
        {
            this.type = type;
            this.turnsRemaining = turnsRemaining;
            this.pieceRow = pieceRow;
            this.pieceCol = pieceCol;
            this.affectedRow = affectedRow;
            this.affectedCol = affectedCol;
            this.revivedPieceType = revivedPieceType;
            this.revivedPieceSide = revivedPieceSide;
        }
    }
}
