import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game {
    private Board board;
    private Color currentTurn;
    private boolean gameOver;
    private ArrayList<Move> moveHistory = new ArrayList<>();
    private int moveCount = 0;
    private boolean modifierOfferedThisCycle = false;
    private Color winner = null;

    public Game() {
        board = new Board(Board.BoardType.DEFAULT);
        board.initializePieces();
        currentTurn = Color.WHITE;
        gameOver = false;
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Color winner()
    {
        return winner;
    }

    // ==================== CORE GAME LOGIC ====================
    /**
     * Attempts to make a move after checking if it is legal; handles modifiers; checks if the game is over
     * @param fromRow the row of the piece the player is moving
     * @param fromCol the col of the piece the player is moving
     * @param toRow the row of the square the player is moving the piece to
     * @param toCol the row of the square the player is moving the piece to
     * @return true if the move was successfully made, false if invalid
     */
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (gameOver) {
            System.out.println("Game is already over.");
            return false;
        }

        Piece piece = board.getPieceAt(fromRow, fromCol);

        if (piece == null || piece.getColor() != currentTurn) {
            System.out.println("Invalid move: No piece of current player's color at the source square.");
            return false;
        }

        if (!piece.canMoveTo(fromRow, fromCol, toRow, toCol, board)) {
            System.out.println("Invalid move: The piece cannot move to the target square.");
            return false;
        }

        if (isBlockedByModifier(piece, toRow, toCol)) {
            return false;
        }

        Move.MoveType moveType = categorizeMoveType(fromRow, fromCol, toRow, toCol);
        moveHistory.add(new Move(fromRow, fromCol, toRow, toCol, moveType, board));
        piece = applyMove(fromRow, fromCol, toRow, toCol, moveType, piece);
        updateEnPassantFlags(piece, fromRow, toRow, moveType);

        // System.out.println(moveHistory.get(moveHistory.size() - 1).getNotation());
        ArrayList<Modifier> expiredModifiers = board.decrementModifiers();
        handleExpiredModifiers(expiredModifiers);

        if (checkKingsAlive()) {
            return true;
        }

        if (currentTurn == Color.WHITE) {
            currentTurn = Color.BLACK;
        } else {
            currentTurn = Color.WHITE;
        }

        moveCount++;
        modifierOfferedThisCycle = false;
        // for (Modifier m : board.getActiveModifiers()) {
        //     System.out.println(m.getType().toString() + " - turns remaining: " + m.getTurnsRemaining());
        // }
        return true;
    }

    /**
     * Categorizes the move type of a move
     * @param fromRow the row of the piece the player is moving
     * @param fromCol the col of the piece the player is moving
     * @param toRow the row of the square the player is moving the piece to
     * @param toCol the col of the square the player is moving the piece to
     * @return MoveType of the move the player is making
     */
    public Move.MoveType categorizeMoveType(int fromRow, int fromCol, int toRow, int toCol) {
        Move.MoveType moveType;
        Piece piece = board.getPieceAt(fromRow, fromCol);

        if (piece instanceof King && (Math.abs(toCol - fromCol) == 2)) {
            if (toCol > fromCol) {
                moveType = Move.MoveType.SHORT_CASTLE;
            } else {
                moveType = Move.MoveType.LONG_CASTLE;
            }
        } else if (piece instanceof Pawn && (Math.abs(toCol - fromCol) == 1)
                && board.getPieceAt(toRow, toCol) == null) {
            moveType = Move.MoveType.EN_PASSANT;
        } else if (piece instanceof Pawn && (toRow == 0 || toRow == 7)) {
            moveType = Move.MoveType.PROMOTION;
        } else {
            moveType = Move.MoveType.NORMAL;
        }
        return moveType;
    }

    /**
     * Applies the move to the board
     * @param fromRow the row of the piece the player is moving
     * @param fromCol the col of the piece the player is moving
     * @param toRow the row of the square the player is moving the piece to
     * @param toCol the col of the square the player is moving the piece to
     * @param moveType the moveType of the move the player is making
     * @param piece the piece the player is moving
     * @return the Piece that is moved
     */
    public Piece applyMove(int fromRow, int fromCol, int toRow, int toCol, Move.MoveType moveType, Piece piece) {
        Piece capturedPiece = board.getPieceAt(toRow, toCol);
        if (capturedPiece != null) {
            for (int i = 0; i < board.getActiveModifiers().size(); i++) {
                Modifier m = board.getActiveModifiers().get(i);
                if (m.getAffectedPiece() == capturedPiece) {
                    board.getActiveModifiers().remove(i);
                    i--;
                }
            }
        }
        if (moveType == Move.MoveType.EN_PASSANT) {
            board.getBoard()[fromRow][toCol] = null;
        } else if (moveType == Move.MoveType.SHORT_CASTLE) {
            Piece rook = board.getPieceAt(fromRow, 7);
            board.getBoard()[fromRow][5] = rook;
            board.getBoard()[fromRow][7] = null;
            rook.setCol(5);
            rook.isFirstMove = false;
        } else if (moveType == Move.MoveType.LONG_CASTLE) {
            Piece rook = board.getPieceAt(fromRow, 0);
            board.getBoard()[fromRow][3] = rook;
            board.getBoard()[fromRow][0] = null;
            rook.setCol(3);
            rook.isFirstMove = false;
        } else if (moveType == Move.MoveType.PROMOTION) {
            piece = new Queen(piece.getColor(), toRow, toCol);
        }

        board.setPieceAt(piece, toRow, toCol);
        board.setPieceAt(null, fromRow, fromCol);
        if (moveType != Move.MoveType.PROMOTION) {
            piece.isFirstMove = false;
        }
        return piece;
    }

    /**
     * updates enPassantFlags
     * @param piece
     * @param fromRow the row of the piece the player is moving
     * @param toRow the row of the square the player is moving the piece to
     * @param moveType the moveType of the move the player is making
     */
    public void updateEnPassantFlags(Piece piece, int fromRow, int toRow, Move.MoveType moveType) {
        if (moveType != Move.MoveType.EN_PASSANT && piece instanceof Pawn && Math.abs(toRow - fromRow) == 2) {
            ((Pawn) piece).isEnPassantable = true;
        }

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPieceAt(r, c);
                if (p instanceof Pawn && p != piece && p.getColor() != currentTurn) {
                    ((Pawn) p).isEnPassantable = false;
                }
            }
        }
    }

    // ==================== MODIFIER LOGIC ====================
    /**
     * Offers three random modifiers from the pool of all modifiers 
     * and excludes modifiers that don't make sense for the current board state
     * @return an array of Modifier.Type that contains the three random modifiers chosen 
     */
    public Modifier.Type[] offeredModifiers() {
        ArrayList<Modifier.Type> pool = new ArrayList<>(Arrays.asList(Modifier.Type.values()));
        pool.removeIf(type -> {
            switch (type) {
                case PAWNS_ONLY:      return !hasPieceOfType(Pawn.class);
                /*case KNIGHTS_ONLY:    return !hasPieceOfType(Knight.class);
                case BISHOPS_ONLY:    return !hasPieceOfType(Bishop.class);
                case ROOKS_ONLY:      return !hasPieceOfType(Rook.class);
                case QUEENS_ONLY:     return !hasPieceOfType(Queen.class);
                case KINGS_ONLY:      return !hasPieceOfType(King.class);*/
                case EXPLODING_PIECE: return !hasPieceOfType(Knight.class);
                case SNIPER_BISHOP:   return !hasPieceOfType(Bishop.class);
                default:              return false;
            }
        });

        Collections.shuffle(pool);
        Modifier.Type[] offered = new Modifier.Type[Math.min(3, pool.size())];
        for (int i = 0; i < offered.length; i++) {
            offered[i] = pool.get(i);
        }
        return offered;
    }
    /**
     * Adds a modifier to the Board's activeModifiers
     * @param modifier the modifier being added
     */
    public void addModifier(Modifier modifier) {
        // use if statements for instant effect modifiers
        if (modifier.getType() == Modifier.Type.BACK_IT_UP) {
            // white pawns
            for (int row = board.getBoard().length - 1; row >= 0; row--) {// reverse so that the same pawn isn't moved back multiple times
                for (int col = 0; col < board.getBoard()[0].length; col++) {
                    Piece piece = board.getPieceAt(row, col);
                    if (piece instanceof Pawn && piece.getColor() == Color.WHITE && (row + 1) < 8 && board.getPieceAt(row + 1, col) == null) {
                        board.setPieceAt(piece, row + 1, col);
                        board.setPieceAt(null, row, col);
                    }
                }
            }

            // black pawns
            for (int row = 0; row < board.getBoard().length; row++) {// reverse so that the same pawn isn't moved back multiple times
                for (int col = 0; col < board.getBoard()[0].length; col++) {
                    Piece piece = board.getPieceAt(row, col);
                    if (piece instanceof Pawn && piece.getColor() == Color.BLACK && (row - 1) >= 0 && board.getPieceAt(row - 1, col) == null) {
                        board.setPieceAt(piece, row - 1, col);
                        board.setPieceAt(null, row, col);
                    }
                }
            }
        }
        board.addModifier(modifier);
        modifierOfferedThisCycle = true; // prevents infinite loop of modifiers being offered
    }

    /**
     * Considers whether modifiers should be offered or not based on move number and whether modifiers were already offered
     * @return true if modifiers should be offered and false if not
     */
    public boolean shouldOfferModifier() {
        return (moveCount >= 5 && moveCount % 5 == 0 && !modifierOfferedThisCycle);
    }

    /**
     * Checks if a move is illegal based on current modifiers
     * @param piece the piece in question of being able to move
     * @return true if the piece is illegal, false if it is legal
     */
    private boolean isBlockedByModifier(Piece piece, int toRow, int toCol) {
        for (Modifier m : board.getActiveModifiers()) {
            Class<?> required = m.affectedClass();
            if (required != null && !required.isInstance(piece)) {
                return true;
            }
            if (m.getType() == Modifier.Type.INVINCIBLE_PAWNS) {
                Piece target = board.getPieceAt(toRow, toCol);
                if (target instanceof Pawn) {
                    return true;
                }
            }
            else if (m.getType() == Modifier.Type.SANCTUARY) {
                if (m.getAffectedRow() == toRow && m.getAffectedCol() == toCol && board.getPieceAt(toRow, toCol) != null) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Handles modifiers on their expiration
     * @param expiredModifiers all the modifiers that have expired
     */
    private void handleExpiredModifiers(ArrayList<Modifier> expiredModifiers) {
        for (Modifier m : expiredModifiers) {
            if (m.getType() == Modifier.Type.EXPLODING_PIECE) {
                Piece[][] boardArr = board.getBoard();
                Piece piece = m.getAffectedPiece();
                int row = piece.getRow();
                int col = piece.getCol();
                for (int x = row - 1; x <= row + 1; x++) {
                    for (int y = col - 1; y <= col + 1; y++) {
                        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                            boardArr[x][y] = null;
                        }
                    }
                }
            }
            // add more conditions for new modifiers
        }
    }

    public void handleModifierChoice(Modifier.Type[] options, int choice) {
        if (options[choice] == Modifier.Type.EXPLODING_PIECE) {
            int[] square = getBoard().randomSquare(Knight.class, getCurrentTurn());
            Piece knight = getBoard().getPieceAt(square[0], square[1]);
            System.out.println("The knight on " + (char)('a' + knight.getCol()) + Math.abs(knight.getRow() - 8) + " is about to explode mi bomboclat in 3 turns");
            addModifier(new Modifier(10, Modifier.Type.EXPLODING_PIECE, knight));
        }
        else if (options[choice] == Modifier.Type.SNIPER_BISHOP) {
            int[] square = getBoard().randomSquare(Bishop.class, getCurrentTurn());
            Piece bishop = getBoard().getPieceAt(square[0], square[1]);
            System.out.println("The bishop on " + (char)('a' + bishop.getCol()) + Math.abs(bishop.getRow() - 8) + " is una esniper for 3 turns");
            addModifier(new Modifier(5, Modifier.Type.SNIPER_BISHOP, bishop));
        }
        else {
            addModifier(new Modifier(5, options[choice]));
        }
    }

    // ==================== GAME STATE ====================
    /**
     * Checks whether the Kings are alive, which is important for game over logic
     * @return true if the game is over, false if the game is still going
     */
    private boolean checkKingsAlive() {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[0].length; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece instanceof King) {
                    if (piece.getColor() == Color.WHITE) {
                        whiteKingAlive = true;
                    }
                    else {
                        blackKingAlive = true;
                    }
                }
            }
        }
        if (!whiteKingAlive && !blackKingAlive) {
            gameOver = true;
            winner = Color.GRAY;
            System.out.println("DRAW: BOTH KINGS ARE DEAD");
            return true;
        }
        if (!whiteKingAlive || !blackKingAlive) {
            gameOver = true;
            if(whiteKingAlive)
            {
                winner = Color.WHITE;
            }
            else
            {
                winner = Color.BLACK;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether there is a piece of a given type on the board
     * @param pieceClass the type of piece
     * @return true if there exists a piece of that type, false if not
     */
    private boolean hasPieceOfType(Class<?> pieceClass) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPieceAt(r, c);
                if (p != null && p.getColor() == currentTurn && pieceClass.isInstance(p)) {
                    return true;
                }
            }
        }
        return false;
    }
}