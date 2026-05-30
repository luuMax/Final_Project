import java.io.*;
import java.net.*;

/**
 * Manages the TCP connection between two players. Wraps a socket with typed
 * send/receive methods so the rest of the codebase never touches raw streams.
 * All messages are line-delimited text sent in a fixed order, both sides must
 * read exactly what the other side writes, in the same sequence.
 */
public class NetworkManager
{

    private Socket         socket;
    private PrintWriter    out;
    private BufferedReader in;
    public boolean         isWhite;

    /**
     * Opens input/output streams on the given socket.
     *
     * @param socket
     *            the live TCP connection to the opponent
     * @param isWhite
     *            whether this player was assigned White
     */
    public NetworkManager(Socket socket, boolean isWhite)
        throws IOException
    {
        this.socket = socket;
        this.isWhite = isWhite;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    /**
     * Sends the color assignment to the client during setup. Called only by
     * Server, exactly once, before the game starts.
     *
     * @param msg
     *            "WHITE" or "BLACK" — the color assigned to the client
     */
    public void sendSetup(String msg)
    {
        out.println(msg);
    }


    /**
     * Blocks until the server's color assignment message arrives. Called only
     * by Client, exactly once, before the game starts.
     *
     * @return "WHITE" or "BLACK", or null if the connection dropped
     */
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


    /**
     * Sends a move to the opponent as a comma-separated string:
     * "fromRow,fromCol,toRow,toCol"
     *
     * @param fromRow
     *            row of the piece being moved (0 = top)
     * @param fromCol
     *            column of the piece being moved
     * @param toRow
     *            row of the destination square
     * @param toCol
     *            column of the destination square
     */
    public void sendMove(int fromRow, int fromCol, int toRow, int toCol)
    {
        out.println(fromRow + "," + fromCol + "," + toRow + "," + toCol);
    }


    /**
     * Blocks until the opponent sends a move. Called by GameRunner's listener
     * thread in a loop for the entire game.
     *
     * @return int[4] of {fromRow, fromCol, toRow, toCol}, or null if the
     *         opponent disconnected
     */
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


    /**
     * Sends the three modifier options that were offered to the local player.
     * The opponent's GameRunner reads and discards these — they only need to
     * know what was chosen, not what was offered.
     *
     * @param options
     *            array of exactly 3 modifier types to send
     */
    public void sendModifierOptions(Modifier.Type[] options)
    {
        out.println(options[0].name() + "," + options[1].name() + "," + options[2].name());
    }


    /**
     * Blocks until the opponent sends their three modifier options. Called by
     * the listener thread immediately after receiving a move that triggers a
     * modifier offer. The result is discarded — the listener only calls this to
     * drain the message from the stream.
     *
     * @return array of 3 Modifier.Types, or null if connection dropped
     */
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


    /**
     * Sends a fully serialized modifier so the opponent can reconstruct it.
     * Format: "type,turnsRemaining,pieceRow,pieceCol,affectedRow,affectedCol"
     * pieceRow/pieceCol are -1 if the modifier has no piece target.
     * affectedRow/affectedCol are -1 if the modifier has no square target.
     *
     * @param m
     *            the modifier chosen by the local player
     */
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


    /**
     * Sends raw modifier fields directly, used when a Modifier object is not
     * available (e.g. when forwarding already-parsed data). Format is identical
     * to sendModifier().
     *
     * @param type
     *            the modifier type
     * @param turnsRemaining
     *            turns until the modifier expires
     * @param pieceRow
     *            row of the targeted piece, or -1 if none
     * @param pieceCol
     *            column of the targeted piece, or -1 if none
     * @param affectedRow
     *            row of the targeted square, or -1 if none
     * @param affectedCol
     *            column of the targeted square, or -1 if none
     */
    public void sendModifierData(
        Modifier.Type type,
        int turnsRemaining,
        int pieceRow,
        int pieceCol,
        int affectedRow,
        int affectedCol)
    {
        out.println(
            type.name() + "," + turnsRemaining + "," + pieceRow + "," + pieceCol + "," + affectedRow
                + "," + affectedCol);
    }


    /**
     * Sends a resurrection modifier with the extra fields needed to reconstruct
     * the revived piece on the opponent's board. Appends the piece's type and
     * side to the standard modifier format.
     *
     * @param piece
     *            the piece being resurrected
     * @param row
     *            row where the piece will be placed
     * @param col
     *            column where the piece will be placed
     */
    public void sendResurrectionData(Piece piece, int row, int col)
    {
        out.println(
            Modifier.Type.RESURRECTION.name() + ",0,-1,-1," + row + "," + col + ","
                + piece.getType().name() + "," + piece.getSide().name());
    }


    /**
     * Blocks until the opponent sends a modifier, then deserializes it into a
     * ModifierData record. Does not create any Piece objects — GameRunner
     * resolves pieces from the board using pieceRow/pieceCol.
     *
     * @return a ModifierData with all fields populated, or null if the
     *         connection dropped or parsing failed
     */
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


    /**
     * Returns if the player is white
     * @return
     *          true if the player is white, false if not
     */
    public boolean getIsWhite()
    {
        return isWhite;
    }


    /**
     * Closes the underlying socket. Called when the game ends or the opponent
     * disconnects.
     */
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

    /**
     * Raw data carrier for a received modifier. Contains no game logic —
     * GameRunner is responsible for interpreting the fields and applying them
     * to the board. pieceRow/pieceCol and affectedRow/affectedCol are -1 when
     * unused. revivedPieceType and revivedPieceSide are null for
     * non-resurrection modifiers.
     */
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

        /**
         * Contstructor that initializes the object with relevant information for the game and network manager.
         * @param type
         * @param turnsRemaining
         * @param pieceRow
         * @param pieceCol
         * @param affectedRow
         * @param affectedCol
         */
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

        /**
         * Contstructor that initializes the object with relevant information for the game and network manager.
         * @param type
         * @param turnsRemaining
         * @param pieceRow
         * @param pieceCol
         * @param affectedRow
         * @param affectedCol
         * @param revivedPieceType
         * @param revivedPieceSide
         */
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
