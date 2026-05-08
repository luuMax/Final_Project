import java.util.ArrayList;
public class Move { //a recipt for a move, contains all info about move. 
    public enum MoveType {
        NORMAL, PROMOTION, EN_PASSANT, SHORT_CASTLE, LONG_CASTLE
    }
    private Piece piece;
    private Piece capturedPiece;
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private MoveType moveType;
    private Board board;
    private boolean needsFileDisambiguation;
    private boolean needsRankDisambiguation;
    private String notation;

    public Move(int fromRow, int fromCol, int toRow, int toCol, MoveType moveType, Board board) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.moveType = moveType;
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
        notation = toString();
    }

    private ArrayList<Piece> getAmbiguousPieces() {
        ArrayList<Piece> dPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece dPiece = board.getPieceAt(row, col);
                if (dPiece != null && dPiece.getClass() == piece.getClass() && dPiece.getColor() == piece.getColor() && dPiece.canMoveTo(row, col, toRow, toCol, board) && dPiece != piece) {
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
                if (isCapture || moveType == MoveType.EN_PASSANT) {
                    moveString += (char) ('a' + fromCol);
                    moveString += "x";
                    moveString += (char) ('a' + toCol);
                    moveString += Math.abs(toRow - 8);
                }
                
                else if (moveType == MoveType.NORMAL) {
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
        return moveString;
    }

    public void setCheck() {
        notation += "+";
    }
    
    public void setCheckmate() {
        notation += "#";
    }

    public String getNotation() {
        return notation;
    }
}
