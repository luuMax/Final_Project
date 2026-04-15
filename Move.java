import java.util.ArrayList;
public class Move {
    public enum MoveType {
        NORMAL, PROMOTION, EN_PASSANT, SHORT_CASTLE, LONG_CASTLE
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
    Board board;

    // keep check and checkmate separate, only one should be true at a time
    // public Move(Piece piece, int fromRow, int fromCol, int toRow, int toCol, Piece capturedPiece, MoveType moveType, boolean isCheck, boolean isCheckmate, Board board) {
    //     this.piece = piece;
    //     this.fromRow = fromRow;
    //     this.fromCol = fromCol;
    //     this.toRow = toRow;
    //     this.toCol = toCol;
    //     this.capturedPiece = capturedPiece;
    //     this.moveType = moveType;
    //     this.isCheck = isCheck;
    //     this.isCheckmate = isCheckmate;
    //     this.board = board;
    // }
    public Move(int fromRow, int fromCol, int toRow, int toCol, MoveType moveType, boolean isCheck, boolean isCheckmate, Board board) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.moveType = moveType;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.board = board;
        piece = board.getPieceAt(fromRow, fromCol);
        capturedPiece = board.getPieceAt(toRow, toCol);
    }

    public String toString() {
        // TODO: add a way to disambiguate two or more pieces of the same type and color that can move to the same square
        String moveString = "";
        boolean isCapture = capturedPiece != null;
        if (moveType != MoveType.SHORT_CASTLE && moveType != MoveType.LONG_CASTLE) {
            if (piece instanceof Pawn) {
                moveString += (char) ('a' + fromCol);
                if (moveType == MoveType.NORMAL) {
                    moveString += Math.abs(toRow - 8);
                }
                if (isCapture) {
                    moveString += "x" + String.valueOf((char) ('a' + toCol) + Math.abs(toRow - 8));
                }
                if (moveType == MoveType.PROMOTION) {
                    moveString += "=Q"; // queen by default add logic for under promotions later
                }
            }
            else {
                if (piece instanceof Knight) {
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
                    moveString += "x";
                }

                // disambiguation
                boolean rankDis = false;
                ArrayList<Piece> dPieces = new ArrayList<>();
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        Piece dPiece = board.getPieceAt(row, col);
                        if (dPiece != null && dPiece.getClass() == piece.getClass() && dPiece.getColor() == piece.getColor() && dPiece.isLegalMove(row, col, toRow, toCol, board) && dPiece != piece) {
                            dPieces.add(dPiece);
                        }
                    }
                }
                for (Piece disPiece : dPieces) {
                    if (disPiece.getCol() == fromCol) {
                        rankDis = true;
                    }
                }

                moveString += String.valueOf((char) ('a' + fromCol));
                if (rankDis) {
                    moveString += Math.abs(fromRow - 8);
                }
                
                moveString += String.valueOf((char) ('a' + toCol));
                moveString += Math.abs(toRow - 8);

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
