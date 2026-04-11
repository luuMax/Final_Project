public class Move {
    public enum MoveType {
        NORMAL, CAPTURE, PROMOTION, EN_PASSANT, SHORT_CASTLE, LONG_CASTLE
    }
    Piece piece;
    Piece capturedPiece;
    int fromRow;
    int fromCol;
    int toRow;
    int toCol;
    MoveType moveType;
    boolean isCheck;
    boolean isCheckmate;

    // keep check and checkmate separate, only one should be true at a time
    public Move(Piece piece, Piece capturedPiece, int fromRow, int fromCol, int toRow, int toCol, MoveType moveType, boolean isCheck, boolean isCheckmate) {
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.moveType = moveType;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
    }

    public String toString() {
        // TODO: add a way to disambiguate two or more pieces of the same type and color that can move to the same square
        String moveString = "";
        if (moveType != MoveType.SHORT_CASTLE && moveType != MoveType.LONG_CASTLE) {
            if (piece instanceof Pawn) {
                moveString += (char) ('a' + fromCol);
                if (moveType == MoveType.NORMAL) {
                    moveString += Math.abs(toRow - 8);
                }
            }
            else if (piece instanceof Knight) {
                moveString += "N";
            }
            else if (piece instanceof Bishop) {
                moveString += "B";
            }
            else if (piece instanceof Rook) {
                moveString += "R";
            }
            else if (piece instanceof Queen) {
                moveString += "Q";
            }
            else if (piece instanceof King) {
                moveString += "K";
            }

            if (capturedPiece != null) {
                moveString += "x" + (char)('a' + toCol) + (Math.abs(toRow - 8)); 
            }

            if (isCheckmate) {
                moveString += "#";
            }

            if (isCheck) {
                moveString += "+";
            }

        }
        else if (moveType == MoveType.SHORT_CASTLE){
            moveString = "O-O";
        }
        else if (moveType == MoveType.LONG_CASTLE) {
            moveString = "O-O-O";
        }


        return moveString;
    }
}
