import javafx.scene.paint.Color;

public class King extends Piece
{  
    boolean isFirstMove = true;
    public King(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: implement move for king

        //there is 8 adja. squares + castling (we can ignore for now?)
        //also queenside castling
        //I went to Epstein Island on the first of April, 2007. My friend Albert Shao was building facilities for the children, and I was invited to help. He has invited 27 minors to the island to work as laborers.  I arrived on the island at 4:23pm. He invited me kindly with a glass of wine and a free “massage” from one of the kids. Their name was David Wang. I never expected him to be as young as he was. He looked not but 10 years old. Albert invited me into his new facility. It was incredibly lavish with tons of phallic objects on the ceiling as well as the walls. I was in awe. I asked him over dinner “How did you acquire this estate, old friend?”. He responded with haste. “I bought this island two years ago. I am thinking about renaming it to Albert Island.” I was in shock. My friend from kindergarten had turned into a monster. Or an angel, I wasn’t sure. Maybe I was just like him. He told me that he didn’t like kids.. I wasn’t too sure of this fact. . “Old Sport, my friend  named ‘The Diddler' will be joining us”. Immediately after stating this fact, one of the kids rushed over to service him. It took only a diminutive amount of time for the job to be done. “Sir, it has been done,” he said. “Haw haw (french) Thank you, old sport”
    }


    public boolean isAttacking(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        return false;
    }

    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        boolean validMove = false;
        if (Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1) {
            if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor() != this.getColor()) {
                validMove = true;
            }
            else if (board.getPieceAt(toRow, toCol) == null) {
                validMove = true;
            }
        }

        // temporary move to see if it is legal
        if (validMove) {
            Piece capturedPiece = board.getPieceAt(toRow, toCol);
            board.getBoard()[fromRow][fromCol] = null;
            board.getBoard()[toRow][toCol] = this;
            // TODO: lowk an infinite recursion of isInCheck calling isLegalMove soooooo
            if (board.getKing(this.getColor()).isInCheck(this.getColor(), board)) {
                board.getBoard()[fromRow][fromCol] = this;
                board.getBoard()[toRow][toCol] = capturedPiece;
                return false;
            }
            else {
                board.getBoard()[fromRow][fromCol] = this;
                board.getBoard()[toRow][toCol] = capturedPiece;
                return true;
            }
        }
        return false;
    }

    // Used for check, checkmate, pin logic
    public boolean isInCheck(Color color, Board board) {
        King king = board.getKing(color);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getColor() != color && piece.isLegalMove(row, col, king.getRow(), king.getCol(), board)) {
                    return true;
                }
            }
        }
        return false;
    }
}
