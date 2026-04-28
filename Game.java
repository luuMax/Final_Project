import javafx.scene.paint.Color;
import java.text.Normalizer;
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

    //
    public void makeMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        Piece piece = board.getPieceAt(fromRow, fromCol);
        if (piece.getColor() != currentTurn) {return;}
        if (piece == null || !piece.isLegalMove(fromRow, fromCol, toRow, toCol, board)) 
            {return;}

        Move.MoveType moveType = Move.MoveType.NORMAL; //default move type
        Piece capturedPiece = board.getPieceAt(toRow, toCol); //default placeholder *captured piece*

        //en passant

       

                
        
        




        //stuff it does after "making" the move:
        




            // more conditions and stuff
            // after making a move set isFirstMove to false
            // also make sure to check that if a pawn moves two squares, set isEnPassantable to true

        }
    }



    


