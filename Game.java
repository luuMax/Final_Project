import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class Game
{

    private Board               board;
    private Color               currentTurn;

    // true = game ended
    private boolean             gameOver;

    private ArrayList<Move>     moveHistory     = new ArrayList<>();

    private ArrayList<Modifier> activeModifiers = new ArrayList<>();

    private int                 moveCount       = 0;

    public Game()
    {

        board = new Board(Board.BoardType.DEFAULT);

        board.initializePieces();

        currentTurn = Color.WHITE;

        gameOver = false;
    }


    public Board getBoard()
    {

        return board;
    }


    public Color getCurrentTurn()
    {

        return currentTurn;
    }


    public boolean isGameOver()
    {

        return gameOver;
    }

    /*
     * Main move handler
     */


    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol)
    {

        if (gameOver)
        {

            System.out.println("Game is already over.");

            return false;
        }

        Piece piece = board.getPieceAt(fromRow, fromCol);

        Piece capturedPiece = board.getPieceAt(toRow, toCol);

        // validate piece ownership

        if (piece == null || piece.getColor() != currentTurn)
        {

            System.out.println("Invalid move: wrong turn or empty square.");

            return false;
        }

        // validate move legality

        if (!piece.canMoveTo(fromRow, fromCol, toRow, toCol, board))
        {

            System.out.println("Invalid move.");

            return false;
        }

        Move.MoveType moveType = categorizeMoveType(fromRow, fromCol, toRow, toCol);

        // store move BEFORE altering board

        moveHistory.add(new Move(fromRow, fromCol, toRow, toCol, moveType, board));

        // apply move

        piece = applyMove(fromRow, fromCol, toRow, toCol, moveType, piece);

        // update special flags

        updateEnPassantFlags(piece, fromRow, toRow, moveType);

        // king captured = game over

        if (capturedPiece instanceof King)
        {

            gameOver = true;

            System.out.println("Game over.");

            return true;
        }

        // SWITCH TURN

        if (currentTurn == Color.WHITE)
        {

            currentTurn = Color.BLACK;

        }
        else
        {

            currentTurn = Color.WHITE;
        }

        // notation print

        System.out.println(moveHistory.get(moveHistory.size() - 1).getNotation());

        // modifiers

        decrementModifiers();

        moveCount++;

        // every 3 moves
        // TEMP FIX:
        // auto-select modifier
        // NO console input

        if (moveCount >= 3 && moveCount % 3 == 0)
        {

            Modifier.Type[] options = offeredModifiers();

            if (options.length > 0)
            {

                System.out.println("Random modifier selected:");

                for (int i = 0; i < options.length; i++)
                {

                    System.out.println((i + 1) + ". " + options[i]);
                }

                int choice = new Random().nextInt(options.length);

                System.out.println("Chosen: " + options[choice]);

                activeModifiers.add(new Modifier(3, options[choice]));
            }
        }

        return true;
    }


    public Move.MoveType categorizeMoveType(int fromRow, int fromCol, int toRow, int toCol)
    {

        Move.MoveType moveType;

        Piece piece = board.getPieceAt(fromRow, fromCol);

        // castling

        if (piece instanceof King && Math.abs(toCol - fromCol) == 2)
        {

            if (toCol > fromCol)
            {

                moveType = Move.MoveType.SHORT_CASTLE;

            }
            else
            {

                moveType = Move.MoveType.LONG_CASTLE;
            }
        }

        // en passant

        else if (piece instanceof Pawn && Math.abs(toCol - fromCol) == 1
            && board.getPieceAt(toRow, toCol) == null)
        {

            moveType = Move.MoveType.EN_PASSANT;
        }

        // promotion

        else if (piece instanceof Pawn && (toRow == 0 || toRow == 7))
        {

            moveType = Move.MoveType.PROMOTION;
        }

        else
        {

            moveType = Move.MoveType.NORMAL;
        }

        return moveType;
    }


    public Piece applyMove(
        int fromRow,
        int fromCol,
        int toRow,
        int toCol,
        Move.MoveType moveType,
        Piece piece)
    {

        // en passant

        if (moveType == Move.MoveType.EN_PASSANT)
        {

            board.getBoard()[fromRow][toCol] = null;
        }

        // short castle

        else if (moveType == Move.MoveType.SHORT_CASTLE)
        {

            Piece rook = board.getPieceAt(fromRow, 7);

            board.getBoard()[fromRow][5] = rook;

            board.getBoard()[fromRow][7] = null;

            rook.setCol(5);

            rook.isFirstMove = false;
        }

        // long castle

        else if (moveType == Move.MoveType.LONG_CASTLE)
        {

            Piece rook = board.getPieceAt(fromRow, 0);

            board.getBoard()[fromRow][3] = rook;

            board.getBoard()[fromRow][0] = null;

            rook.setCol(3);

            rook.isFirstMove = false;
        }

        // promotion

        else if (moveType == Move.MoveType.PROMOTION)
        {

            piece = new Queen(piece.getColor(), toRow, toCol);
        }

        // final board update

        board.getBoard()[toRow][toCol] = piece;

        board.getBoard()[fromRow][fromCol] = null;

        piece.setRow(toRow);

        piece.setCol(toCol);

        if (moveType != Move.MoveType.PROMOTION)
        {

            piece.isFirstMove = false;
        }

        return piece;
    }


    public void updateEnPassantFlags(Piece piece, int fromRow, int toRow, Move.MoveType moveType)
    {

        if (moveType != Move.MoveType.EN_PASSANT && piece instanceof Pawn
            && Math.abs(toRow - fromRow) == 2)
        {

            ((Pawn)piece).isEnPassantable = true;
        }

        for (int r = 0; r < 8; r++)
        {

            for (int c = 0; c < 8; c++)
            {

                Piece p = board.getPieceAt(r, c);

                if (p instanceof Pawn && p != piece && p.getColor() != currentTurn)
                {

                    ((Pawn)p).isEnPassantable = false;
                }
            }
        }
    }


    public void decrementModifiers()
    {

        for (Modifier m : activeModifiers)
        {

            m.decrementTurns();
        }

        activeModifiers.removeIf(Modifier::isExpired);
    }


    public Modifier.Type[] offeredModifiers()
    {

        ArrayList<Modifier.Type> pool = new ArrayList<>(Arrays.asList(Modifier.Type.values()));

        pool.removeIf(type -> {

            switch (type)
            {

                case PAWNS_ONLY:
                    return !hasPieceOfType(Pawn.class);

                case KNIGHTS_ONLY:
                    return !hasPieceOfType(Knight.class);

                case BISHOPS_ONLY:
                    return !hasPieceOfType(Bishop.class);

                case ROOKS_ONLY:
                    return !hasPieceOfType(Rook.class);

                case QUEENS_ONLY:
                    return !hasPieceOfType(Queen.class);

                case KINGS_ONLY:
                    return !hasPieceOfType(King.class);

                case EXPLODING_PIECE:
                    return !hasPieceOfType(Knight.class);

                default:
                    return false;
            }
        });

        Collections.shuffle(pool);

        Modifier.Type[] offered = new Modifier.Type[Math.min(3, pool.size())];

        for (int i = 0; i < offered.length; i++)
        {

            offered[i] = pool.get(i);
        }

        return offered;
    }


    private boolean hasPieceOfType(Class<?> pieceClass)
    {

        for (int r = 0; r < 8; r++)
        {

            for (int c = 0; c < 8; c++)
            {

                Piece p = board.getPieceAt(r, c);

                if (p != null && p.getColor() == currentTurn && pieceClass.isInstance(p))
                {

                    return true;
                }
            }
        }

        return false;
    }
}
