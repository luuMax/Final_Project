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
    boolean needsFileDisambiguation;
    boolean needsRankDisambiguation;

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
        if (moveType == MoveType.EN_PASSANT) {
            capturedPiece = board.getPieceAt(fromRow, toCol);
        }
        else {
            capturedPiece = board.getPieceAt(toRow, toCol);
        }

        ArrayList<Piece> dPieces = getAmbiguousPieces();
        needsFileDisambiguation = needsFileDisambiguation(dPieces);
        needsRankDisambiguation = needsRankDisambiguation(dPieces);
    }

    private ArrayList<Piece> getAmbiguousPieces() {
        ArrayList<Piece> dPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece dPiece = board.getPieceAt(row, col);
                if (dPiece != null && dPiece.getClass() == piece.getClass() && dPiece.getColor() == piece.getColor() && dPiece.isLegalMove(row, col, toRow, toCol, board) && dPiece != piece) {
                    dPieces.add(dPiece);
                }
            }
        }
        return dPieces;
    }
    private boolean needsFileDisambiguation(ArrayList<Piece> dPieces) {
        if (!dPieces.isEmpty()) {
            for (Piece dPiece : dPieces) {
                if (dPiece.getCol() == fromCol) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private boolean needsRankDisambiguation(ArrayList<Piece> dPieces) {
        if (!dPieces.isEmpty()) {
            for (Piece dPiece : dPieces) {
                if (dPiece.getCol() == fromCol) {
                    return true;
                }
            }
        }

        return false;
    }
    public String toString() {
        String moveString = "";
        boolean isCapture = capturedPiece != null;
        if (moveType != MoveType.SHORT_CASTLE && moveType != MoveType.LONG_CASTLE) {
            if (piece instanceof Pawn) {
                if (moveType == MoveType.NORMAL) {
                    moveString += (char) ('a' + toCol);
                    moveString += Math.abs(toRow - 8);
                }
                else if (isCapture || moveType == MoveType.EN_PASSANT) {
                    moveString += (char) ('a' + fromCol);
                    moveString += "x";
                    moveString += (char) ('a' + toCol);
                    moveString += Math.abs(toRow - 8);
                }
                if (moveType == MoveType.PROMOTION) {
                    moveString += "=Q";
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
                if (needsFileDisambiguation) {
                    moveString += (char) ('a' + fromCol);
                }
                if (needsRankDisambiguation) {
                    moveString += Math.abs(fromRow - 8);
                }
                if (capturedPiece != null) {
                    moveString += "x";
                }

                moveString += String.valueOf((char) ('a' + toCol));
                moveString += Math.abs(toRow - 8);

            }
        }
        else if (moveType == MoveType.SHORT_CASTLE){
            moveString = "O-O";
        }
        else if (moveType == MoveType.LONG_CASTLE) {
            moveString = "O-O-O";
        }
        if (isCheckmate) {
            moveString += "#";
        }

        if (isCheck) {
            moveString += "+";
        }
        return moveString;
    }
}
