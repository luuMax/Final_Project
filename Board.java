import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Board class to represent the chess board and piece positions

public class Board {
    private BoardType boardType;
    private Piece[][] boardArr;
    private ArrayList<Modifier> activeModifiers = new ArrayList<>();
    private Random rand = new Random();

    public enum BoardType {
        DEFAULT, CUSTOM //none for now
    }

    public Board(BoardType boardType) {
        this.boardType = boardType;
        this.boardArr = new Piece[8][8];
        
    }

    public Board() {
        this(BoardType.DEFAULT);
        initializePieces();

    }

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

    //getPieceAt for checking gameState 
    public Piece getPieceAt(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null; // out of bounds
        }
        return boardArr[row][col];
    }

    // mainly for testing purposes 
    public void setPieceAt(Piece piece, int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            boardArr[row][col] = piece;
        }
    }

    public Piece[][] getBoard() {
        return boardArr;
    }

    // MODIFIER METHODS
    public ArrayList<Modifier> getActiveModifiers() {
        return activeModifiers;
    }
    public void addModifier(Modifier modifier) {
        activeModifiers.add(modifier);
    }

    public void removeModifier(Modifier modifier) {
        activeModifiers.remove(modifier);
    }

    public boolean hasModifier(Modifier modifier) {
        return activeModifiers.contains(modifier);
    }

    public void decrementModifiers() {
        for (int i = 0; i < activeModifiers.size(); i++) {
            Modifier m = activeModifiers.get(i);
            m.decrementTurns();
            if (m.getTurnsRemaining() <= 0) {
                if (m.getType() == Modifier.Type.EXPLODING_PIECE) {
                    Piece[][] board = getBoard();
                    Piece piece = m.getAffectedPiece();
                    int row = piece.getRow();
                    int col = piece.getCol();
                    for (int x = row - 1; x <= row + 1; x++) {
                        for (int y = col - 1; y <= col + 1; y++) {
                            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                                board[x][y] = null;
                            }
                        }
                    }
                }
                activeModifiers.remove(m);
                i--;
            }
        }
    }

    /* Returns an int[row, col] which represents a random square on the board
     * Precondition: pieceType is either a subclass of Piece or null. Color is either black, white, or null
     * Postcondition: returns a random square that fulfills the requirements of being a specific piece and color
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
}