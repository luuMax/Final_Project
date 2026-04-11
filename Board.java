import javafx.scene.paint.Color;

// Board class to represent the chess board and piece positions

public class Board {
    private BoardType boardType;
    private Piece[][] boardArr;

    private King whiteKing;
    private King blackKing;

    public enum BoardType {
        DEFAULT, CUSTOM
    }

    public Board(BoardType boardType) {
        this.boardType = boardType;
        this.boardArr = new Piece[8][8];
        
    }

    public Board() {
        this(BoardType.DEFAULT);
    }

    public void initializePieces(BoardType boardType) {
            if (this.boardType == boardType) {
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

            // Store king references for easy check/checkmate access later
            whiteKing = (King) boardArr[7][4];
            blackKing = (King) boardArr[0][4];
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

    public Piece[][] getBoard() {
        return boardArr;
     }
}