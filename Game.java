import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Game {
    private Board board;
    private Color currentTurn;
    private boolean gameState; // true means ongoing, false means game over
    private ArrayList<Move> moveHistory = new ArrayList<>();

    public Game() {
        board = new Board(Board.BoardType.DEFAULT);
        board.initializePieces();
        currentTurn = Color.WHITE;
        gameState = true; 
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        Piece piece = board.getPieceAt(fromRow, fromCol);
        
        if (piece == null || piece.getColor() != currentTurn) {
            System.out.println("Invalid move: No piece of current player's color at the source square.");
            return; // testing
        }

        if (!piece.isLegalMove(fromRow, fromCol, toRow, toCol, board)) {
            System.out.println("Invalid move: The piece cannot move to the target square.");
            return; // testing
        }

        Move.MoveType moveType; 

        //(color doens't matter, works for both sides)
        //short/long castling classifcation 
        if (piece instanceof King && (Math.abs(toCol - fromCol) == 2)) {
            if (toCol > fromCol) {
                moveType = Move.MoveType.SHORT_CASTLE;
            }

            else {
                moveType = Move.MoveType.LONG_CASTLE;
            }
        }

        //enpassant 
        else if (piece instanceof Pawn && (Math.abs(toCol - fromCol) == 1) 
        && board.getPieceAt(toRow, toCol) == null) {
            moveType = Move.MoveType.EN_PASSANT;
        }

        //promotion
        else if (piece instanceof Pawn && (toRow == 0 || toRow == 7)) {
            moveType = Move.MoveType.PROMOTION;
        }

        else {
            moveType = Move.MoveType.NORMAL;
        }
        //MAKE move obj BEFORE altering board
        moveHistory.add(new Move(fromRow, fromCol, toRow, toCol, moveType, false, false, board));
        




    }

}



    


