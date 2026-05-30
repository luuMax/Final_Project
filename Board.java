import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Board class to represent the chess board and piece positions
/**
 * Reperesents the chess board and contains piece positions
 */
public class Board {
    private BoardType boardType;
    private Piece[][] boardArr;
    private ArrayList<Modifier> activeModifiers = new ArrayList<>();
    private Random rand = new Random();

    /**
     * Defines what type of board the board is -- currently only one type, which is default
     */
    public enum BoardType {
        /**
         * The default board type, in this case a normal chess board
         */
        DEFAULT,
        /**
         * A custom board type, none defined in this project 
         */ 
         CUSTOM //none for now
    }

    /**
     * Creates the intended type of board
     * @param boardType
     *              enum of board type, can either be custom or default(there is not custom board yet)
     */
    public Board(BoardType boardType) {
        this.boardType = boardType;
        this.boardArr = new Piece[8][8];
        
    }

    /**
     * Constructor that creates the defualt type of board
     */
    public Board() {
        this(BoardType.DEFAULT);
        initializePieces();

    }

    /**
     * Initializes the pieces in standard chess piece starting positions(aka on opposite sides of the board in correct formation)
     */
    public void initializePieces() {
        if (this.boardType == BoardType.DEFAULT) {
        //Row 0 is basically 8th rank. Row 7 is 1st rank.
            //Col 0 is A file. Col 7 is H file.
            
            // Black back rank
            boardArr[0][0] = new Rook(Color.BLACK, 0, 0);
            boardArr[0][1] = new Knight(Color.BLACK, 0, 1);
            boardArr[0][2] = new Bishop(Color.BLACK, 0, 2);
            boardArr[0][3] = new Queen(Color.BLACK, 0, 3);
            boardArr[0][4] = new King(Color.BLACK, 0, 4);
            boardArr[0][5] = new Bishop(Color.BLACK, 0, 5);
            boardArr[0][6] = new Knight(Color.BLACK, 0, 6);
            boardArr[0][7] = new Rook(Color.BLACK, 0, 7);
            for (int col = 0; col < 8; col++) {
                boardArr[1][col] = new Pawn(Color.BLACK, 1, col);
            }

            // White back rank
            boardArr[7][0] = new Rook(Color.WHITE, 7, 0);
            boardArr[7][1] = new Knight(Color.WHITE, 7, 1);
            boardArr[7][2] = new Bishop(Color.WHITE, 7, 2);
            boardArr[7][3] = new Queen(Color.WHITE, 7, 3);
            boardArr[7][4] = new King(Color.WHITE, 7, 4);
            boardArr[7][5] = new Bishop(Color.WHITE, 7, 5);
            boardArr[7][6] = new Knight(Color.WHITE, 7, 6);
            boardArr[7][7] = new Rook(Color.WHITE, 7, 7);
            for (int col = 0; col < 8; col++) {
                boardArr[6][col] = new Pawn(Color.WHITE, 6, col);
            }

        }
   
        else {
            // do other stuff for other modes maybe
        }
    }

    /**
     * Returns the piece at the location indicated on the board
     * @param row
     * @param col
     * @return
     *      the piece to return
     */
    public Piece getPieceAt(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null; // out of bounds
        }
        return boardArr[row][col];
    }

    /**
     * Places a piece at the position specified on the board array
     * @param piece
     * @param row
     * @param col
     */
    public void setPieceAt(Piece piece, int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            boardArr[row][col] = piece;
            if (piece != null) {
                piece.setRow(row);
                piece.setCol(col);
            }
        }
    }

    /**
     * Returns the board array
     * @return
     *      returns the board
     */
    public Piece[][] getBoard() {
        return boardArr;
    }

    /**
     * returns the activeModifier list
     * @return
     *          the list of active modifiers
     */
    public ArrayList<Modifier> getActiveModifiers() {
        return activeModifiers;
    }

    /**
     * Adds a modifier to the active modifier list
     * @param modifier
     *              modifier to be added
     */
    public void addModifier(Modifier modifier) {
        activeModifiers.add(modifier);
    }
    /**
     * Removes a certain modifier from the active modifier list
     * @param modifier
     *              modifier to be removed
     */
    public void removeModifier(Modifier modifier) {
        activeModifiers.remove(modifier);
    }

    /**
     * Checks if the modifier specified is in the active modifer list
     * @param modifier
     *          modifier to be checked
     * @return
     *          returns true if the modifier was found, false if not
     */
    public boolean hasModifier(Modifier modifier) {
        return activeModifiers.contains(modifier);
    }

    /**
     * Decrements the number of turns left on all modifiers.
     * @return
     *          the updated list of modifiers with turns remaining decremented
     */
    public ArrayList<Modifier> decrementModifiers() {
        ArrayList<Modifier> expiredModifiers = new ArrayList<>();
        for (int i = 0; i < activeModifiers.size(); i++) {
            Modifier m = activeModifiers.get(i);
            m.decrementTurns();
            if (m.getTurnsRemaining() <= 0) {
                expiredModifiers.add(m);
                activeModifiers.remove(m);
                i--;
            }
        }
        return expiredModifiers;
    }

    /**
     * Returns the coordinates of a random square matching the given filters
     * if both parameters are null, returns a random square including both occupied and empty squares
     * if either parameter is specified, a random square meeting both requirements is returned
     * @param pieceType the class of the piece to filter by
     * @param color the color to filter by
     * @return an int[row, col] representing the coordinates of a random square, or null if no eligible squares are found
     */
    public int[] randomSquare(Class<?> pieceType, Color color) {
        ArrayList<int[]> candidates = new ArrayList<>();
        for (int row = 0; row < getBoard().length; row++) {
            for (int col = 0; col < getBoard()[0].length; col++) {
                Piece piece = getPieceAt(row, col);
                if (((pieceType == null || pieceType.isInstance(piece)) && (color == null || (piece != null && piece.getColor() == color)))) {
                    candidates.add(new int[]{row, col});
                }
            }
        }

        // if no squares that meet the criteria are found, return null;
        if (candidates.isEmpty()) {
            return null;
        }

        int[] randomSquare = candidates.get(rand.nextInt(0, candidates.size()));
        return randomSquare;
    }

    /**
     * Returns the coordinates of a random empty square, or null if no eligible squares are found (probably impossible)
     * @return an int[row, col]
     */
    public int[] randomEmptySquare() {
        ArrayList<int[]> candidates = new ArrayList<>();
        for (int row = 0; row < getBoard().length; row++) {
            for (int col = 0; col < getBoard()[0].length; col++) {
                if (getBoard()[row][col] == null) {
                    candidates.add(new int[]{row, col});
                }
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }

        int[] randomSquare = candidates.get(rand.nextInt(0, candidates.size()));
        return randomSquare;
    }
}