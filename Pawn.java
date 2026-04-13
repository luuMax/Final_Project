import javafx.scene.paint.Color;

public class Pawn extends Piece
{
    boolean isFirstMove = true;
    public Pawn(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement move for pawn
    }

    public boolean isAttacking(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        return false;
    }


    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        boolean validMove = false;
        // TODO: implement isLegalMove for pawn
        int direction = (this.getColor() == Color.WHITE) ? -1 : 1; // different directions for white and black

        // forward moves
        // move up 2
        if (toRow - fromRow == 2 * direction && isFirstMove && toCol - fromCol == 0 && board.getPieceAt(fromRow + direction, fromCol) == null && board.getPieceAt(fromRow + 2 * direction, fromCol) == null) {
            validMove = true;
        }

        // move up 1
        if (toRow - fromRow == direction && toCol - fromCol == 0 && board.getPieceAt(fromRow + direction, fromCol) == null) {
            validMove = true;
        }
        
        if (toRow - fromRow == direction && Math.abs(toCol - fromCol) == 1 && board.getPieceAt(toRow, toCol) != null && getColor() != board.getPieceAt(toRow, toCol).getColor()) {
            validMove = true;
        }

        // TODO: en passant

        // checks for a pin
        if (validMove) {
            Piece capturedPiece = board.getPieceAt(toRow, toCol);
            board.getBoard()[toRow][toCol] = this;
            board.getBoard()[fromRow][fromCol] = null;
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
}
