import java.awt.*;

public class GameRunner
{
    private Game           game;
    private BoardUI        boardUI;
    private NetworkManager network;
    private Color          localColor;

    public GameRunner(NetworkManager network)
    {
        this.network = network;
        this.localColor =
            network != null ? (network.getIsWhite() ? Color.WHITE : Color.BLACK) : null;
    }


    /* 
    Gamerunner handles all listening thread logic, used for BoardUI to handle updates between Client-Server. 
    Currenlty, MainMenuUI bypasses gameRunner for local games (singleplayer), but Gamerunner (more like NetworkRunner) is used 
    when a Network is active. 
    
    */
    public void start() 
    {
        game = new Game();

        javax.swing.SwingUtilities.invokeLater(() -> {
            if (network != null)
            {
                boardUI = new BoardUI(800, 1200, 75, game, network, localColor);

                Thread listenerThread = new Thread(() -> {
                    while (!game.isGameOver())
                    {
                        int[] move = network.receiveMove();

                        if (move == null)
                        {
                            System.out.println("Opponent disconnected.");
                            break;
                        }

                        int fr = move[0], fc = move[1], tr = move[2], tc = move[3];

                        boolean valid = game.makeMove(fr, fc, tr, tc);

                        if (!valid)
                        {
                            System.out.println(
                                "WARNING: invalid move received " + fr + "," + fc + " -> " + tr
                                    + "," + tc);
                            continue;
                        }

                        if (game.shouldOfferModifier())
                        {
                            // discard options — mover already picked
                            network.receiveModifierOptions();

                            // receive the fully serialized modifier
                            NetworkManager.ModifierData data = network.receiveModifier();

                            if (data != null)
                            {
                                applyReceivedModifier(data);

                                javax.swing.SwingUtilities.invokeLater(() -> {
                                    boardUI.redrawBoard();
                                });
                            }
                        }

                        game.switchTurn();

                        javax.swing.SwingUtilities.invokeLater(() -> {
                            boardUI.redrawBoard();
                            if (game.isGameOver())
                                boardUI.endGame();
                        });
                    }
                    network.close();
                });

                listenerThread.setDaemon(true);
                listenerThread.start();

            }
            else
            {
                boardUI = new BoardUI(800, 1200, 75, game);
            }
        });
    }


    private void applyReceivedModifier(NetworkManager.ModifierData data)
    {
        if (data.type == Modifier.Type.BRICK)
        {
            game.getBoard().setPieceAt(
                new Brick(Color.GRAY, data.affectedRow, data.affectedCol),
                data.affectedRow,
                data.affectedCol);
            game.setModifierOfferedThisCycle(true);
            return;
        }

        if (data.type == Modifier.Type.PORTAL)
        {
            game.setPortal1Pos(data.pieceRow, data.pieceCol);
            game.setPortal2Pos(data.affectedRow, data.affectedCol);
            game.setPortalsActive(true);
            game.setModifierOfferedThisCycle(true);
            return;
        }

        if (data.type == Modifier.Type.RESURRECTION)
        {
            Piece revivedPiece =
                createPiece(data.revivedPieceType, data.revivedPieceSide, data.affectedRow, data.affectedCol);

            if (revivedPiece != null)
            {
                removeFromCapturedPieces(revivedPiece);
                game.getBoard().setPieceAt(
                    revivedPiece,
                    data.affectedRow,
                    data.affectedCol);
            }
            else if (!game.getCapturedPieces(game.getCurrentTurn()).isEmpty())
            {
                game.getBoard().setPieceAt(
                    game.getCapturedPieces(game.getCurrentTurn()).pop(),
                    data.affectedRow,
                    data.affectedCol);
            }
            game.setModifierOfferedThisCycle(true);
            return;
        }

        // reconstruct the Modifier — only wiring, no game rules
        Modifier m;
        if (data.pieceRow != -1 && data.pieceCol != -1)
        {
            // piece-targeted modifier — look up piece on our board
            Piece piece = game.getBoard().getPieceAt(data.pieceRow, data.pieceCol);
            m = new Modifier(data.turnsRemaining, data.type, piece);
        }
        else if (data.affectedRow != -1 && data.affectedCol != -1)
        {
            // square-targeted modifier
            m = new Modifier(
                data.turnsRemaining,
                data.type,
                data.affectedRow,
                data.affectedCol);
        }
        else
        {
            // board-wide modifier
            m = new Modifier(data.turnsRemaining, data.type);
        }

        game.addModifier(m);
    }


    private Piece createPiece(Piece.Type type, Piece.Side side, int row, int col)
    {
        if (type == null || side == null)
        {
            return null;
        }

        Color color = side == Piece.Side.WHITE ? Color.WHITE : Color.BLACK;

        switch (type)
        {
            case PAWN:
                return new Pawn(color, row, col);
            case ROOK:
                return new Rook(color, row, col);
            case KNIGHT:
                return new Knight(color, row, col);
            case BISHOP:
                return new Bishop(color, row, col);
            case QUEEN:
                return new Queen(color, row, col);
            case KING:
                return new King(color, row, col);
            default:
                return null;
        }
    }


    private void removeFromCapturedPieces(Piece piece)
    {
        java.util.Stack<Piece> capturedPieces = game.getCapturedPieces(piece.getColor());

        for (int i = capturedPieces.size() - 1; i >= 0; i--)
        {
            Piece capturedPiece = capturedPieces.get(i);
            if (capturedPiece.getType() == piece.getType()
                && capturedPiece.getSide() == piece.getSide())
            {
                capturedPieces.remove(i);
                return;
            }
        }
    }
}
