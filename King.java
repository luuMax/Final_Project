import javafx.scene.paint.Color;

public class King extends Piece
{  
    boolean isFirstMove = true;
    public King(Color color, int row, int col)
    {
        super(color, row, col);
    }

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8 && Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1) {
            if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor() != this.getColor()) {
                return true;
            }
            else if (board.getPieceAt(toRow, toCol) == null) {
                return true;
            }
        }
        // castling
        else if (toRow == fromRow && isFirstMove && Math.abs(toCol-fromCol) == 2 && !isInCheck(getColor(), board)) {
            int dir = (int) Math.signum(toCol - fromCol);
            if (isSafeMove(fromRow, fromCol, toRow, fromCol + dir, board) && isSafeMove(fromRow, fromCol, toRow, fromCol + 2 * dir, board)) {
                Piece piece;
                if (dir == 1) {
                    if (isPathClear(fromRow, fromCol, toRow, 7, board)) {
                        piece = board.getPieceAt(toRow, 7);
                    }
                    else {
                        return false;
                    }
                    
                }
                else {
                    if (isPathClear(fromRow, fromCol, toRow, 0, board)) {
                        piece = board.getPieceAt(toRow, 0);
                    }
                    else {
                        return false;
                    }
                }
                if (piece instanceof Rook && piece.isFirstMove) {
                    return true;
                }
            }
        }
        return false;
    }

    // override move method specifically for castling

    // king.move has errors, islegalmove should return if false. makeMove should handle king castling

    @Override
    public void move(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        if (isLegalMove(fromRow, fromCol, toRow, toCol, board)) {
            board.getBoard()[toRow][toCol] = this;
            board.getBoard()[fromRow][fromCol] = null;
            this.setRow(toRow);
            this.setCol(toCol);
        }
        int dir = (int) Math.signum(toCol - fromCol);
        Rook rook;
        if (dir == 1) {
            rook = (Rook) board.getPieceAt(toRow, 7);
            board.getBoard()[toRow][fromCol + dir] = rook;
            board.getBoard()[toRow][7] = null;
            
        }
        else {
            rook = (Rook) board.getPieceAt(toRow, 0);
            board.getBoard()[toRow][fromCol + dir] = rook;
            board.getBoard()[toRow][0] = null;
        }
        isFirstMove = false;
    }

    // Used for check, checkmate, pin logic
    public boolean isInCheck(Color color, Board board) {
        King king = board.getKing(color);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getColor() != color && piece.canMoveTo(row, col, king.getRow(), king.getCol(), board)) {
                    return true;
                }
            }
        }
        return false;
    }
}
