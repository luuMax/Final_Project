import java.awt.*;
import java.util.ArrayList;

public class Game {
    private Board board;
    private Color currentTurn;
    private boolean gameOver; //true = checkmate/stalemate (resign? <--problem for UI to fix. )
    private ArrayList<Move> moveHistory = new ArrayList<>();

    public Game() {
        board = new Board(Board.BoardType.DEFAULT);
        board.initializePieces();
        currentTurn = Color.WHITE;
        gameOver = false;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }
    
    /* 
    makeMove is main method that handles making a move, altering the board, and switching turns.
    It also creates a move obj and adds it to the move history list, which can be used for udoing moves/ ui
    It will also check gamestate, and be able to end the game if !hasAnLegalMoves.
    For now, It can largely replace Piece.move, it already handles the logic better, no need to do it twice. 
    */

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {

        if (gameOver) {
            System.out.println("Game is already over. No more moves can be made."); //testing
            return;
        }

        Piece piece = board.getPieceAt(fromRow, fromCol);
        
        if (piece == null || piece.getColor() != currentTurn) {
            System.out.println("Invalid move: No piece of current player's color at the source square.");
            return; // testing
        }

        if (!piece.isLegalMove(fromRow, fromCol, toRow, toCol, board)) {
            System.out.println("Invalid move: The piece cannot move to the target square.");
            return; // testing
        }




        //------------------------CATEGORIZING THE TYPE OF MOVE ITS ABOUT TO MAKE------------------------
        
        //(color doens't matter, works for both sides)
        //ALSO, isFirstMove is already handled by islegalmove. canmoveto checks it.
        
        Move.MoveType moveType; 

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

        //------------------------ADDS MOVE OBJ TO MOVEHISTORY LIST------------------------

        //MAKE move obj BEFORE altering board
        moveHistory.add(new Move(fromRow, fromCol, toRow, toCol, moveType, false, false, board));



        //------------------------DIRECTLY ALTERS BOARD STATE------------------------ //holy hell this is long
        
        //all alters work regardless of color

        if (moveType == Move.MoveType.EN_PASSANT) {
            board.getBoard()[fromRow][toCol] = null; //remove captured pawn 
        }

        else if (moveType == Move.MoveType.SHORT_CASTLE) {
            Piece rook = board.getPieceAt(fromRow, 7); //for now the rooks are hardcoded to be field a and h. (harder for gamdemoes like 960)
            board.getBoard()[fromRow][5] = rook;           //Setting the King is done down there, altogether with other normal states. 
            board.getBoard()[fromRow][7] = null;
            rook.setCol(5);
            rook.isFirstMove = false;
        }

        else if (moveType == Move.MoveType.LONG_CASTLE) {
            Piece rook = board.getPieceAt(fromRow, 0);
            board.getBoard()[fromRow][3] = rook;
            board.getBoard()[fromRow][0] = null;
            rook.setCol(3); rook.isFirstMove = false;
        }

        else if (moveType == Move.MoveType.PROMOTION) { // we need to still handle ui for promotion, for now auto queen
            piece = new Queen(piece.getColor(), toRow, toCol);
        }

        board.getBoard()[toRow][toCol] = piece;
        board.getBoard()[fromRow][fromCol] = null;
        piece.setRow(toRow);
        piece.setCol(toCol);
        if (moveType != Move.MoveType.PROMOTION) { //promotion = first move of promoted piece
            piece.isFirstMove = false;
        }

        //En passant special flags

        if (moveType != Move.MoveType.EN_PASSANT && piece instanceof Pawn && Math.abs(toRow - fromRow) == 2) {
            ((Pawn) piece).isEnPassantable = true; //for islegalmove pawn
        }

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPieceAt(r, c);
                if (p instanceof Pawn && p != piece && p.getColor() != currentTurn) {
                    ((Pawn) p).isEnPassantable = false;
                }
            }
        }

        //------------------------SWITCH TURNS------------------------

        if (currentTurn == Color.WHITE) {
            currentTurn = Color.BLACK;
        }

        else {
            currentTurn = Color.WHITE;
        }

        checkGameOver();

    }


    //HELPER detects checkmate/stalemate after every move

    //for later, we can handle gameover by draw, resignation, time loss, insufficent matieral, modifyer win cons, etc... 

    private void checkGameOver() {
        if (!hasAnyLegalMove(currentTurn)) {
            gameOver = true;
            boolean inCheck = board.getKing(currentTurn).isInCheck(currentTurn, board);
            System.out.println(inCheck ? "Checkmate!" : "Stalemate!"); //just print for now, ui needs win/loss/draw screen, elo change, etc...
        }
    }

    //HELPER returns true if the given color has any legal moves on board

    private boolean hasAnyLegalMove(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPieceAt(r, c);
                if (p != null && p.getColor() == color && !(p.getLegalMoves(board).isEmpty())) {
                    return true;
                }
            }
        }

        return false;
    }

}



    