import javax.swing.*;


/* import java.awt.event.ActionListener; */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;

/**
 * Class for game window
 */
public class BoardUI extends JFrame {
    // General Sizes //
    private int windowWidth;
    private int windowLength;
    private int tileSize;

    // Chess notation //
    String chessNotation[][] = {
            { "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8" },
            { "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7" },
            { "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6" },
            { "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5" },
            { "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4" },
            { "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3" },
            { "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2" },
            { "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1" }
    };

    // Statics //
    public static final Color VERY_LIGHT_BROWN = new Color(254, 228, 187);
    public static final Color DARK_BROWN = new Color(205, 154, 117);
    public static final Color HIGHLIGHT = new Color(247, 247, 105);
    public static final Color BACKGROUND = new Color(41, 41, 41);
    public static final Color OUTLINE = new Color(37, 28, 24);

    // Some useful stuff for UI logic //
    private Game game;
    private Board boardgrid;
    private JPanel[][] panelBoard = new JPanel[8][8];
    private JPanel board = new JPanel(new GridLayout(8, 8));
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private JPanel gamePanel = new JPanel(new GridBagLayout());
    private JPanel endPanel = new JPanel(new GridBagLayout());
    private JPanel modifierPanel;
    private JTextArea activeModifiersText;
    private boolean animationPlaying = false;

    // Mouse Inputs //
    private int selectedRow;
    private int selectedCol;
    private boolean pieceSelected = false;

    // Modifiers
    private boolean choosingModifier = false;
    private boolean placingSanctuary = false;
    private boolean placingWall = false;
    private boolean placingPortal1 = false;
    private boolean placingPortal2 = false;
    private boolean portal1Placed = false; // for updating the tile sprites
    private boolean portal2Placed = false; // for updating the tile sprites
    private boolean placingResurrection = false;

    // Networking //
    private NetworkManager network = null;
    private Color localColor = null;

    // Game ending //
    private boolean drawRequested = false;
    private Color decider = null;

    // Chat/Gamelog //
    JTextArea textBody = new JTextArea("Game started.\n");
    JScrollPane scrollPane = new JScrollPane(textBody);

    // Reduces a significant amount of lag
    private HashMap<String, ImageIcon> icons = new HashMap<>();

    /**
     * 4-param constructor for local play — delegates to full constructor
     * 
     * @param windowW
     * @param windowL
     * @param tileS
     * @param game
     */
    public BoardUI(int windowW, int windowL, int tileS, Game game) {
        this(windowW, windowL, tileS, game, null, null);
    }

    /**
     * 6-param constructor for networked play
     * 
     * @param windowW
     * @param windowL
     * @param tileS
     * @param game
     * @param net
     * @param loCol
     */
    public BoardUI(int windowW, int windowL, int tileS, Game game, NetworkManager net, Color loCol) {
        windowWidth = windowW;
        windowLength = windowL;
        tileSize = tileS;
        this.game = game;
        this.network = net;
        this.localColor = loCol;
        boardgrid = game.getBoard();
        initialize();
    }

    /**
     * Gets the image png for a certain piece then stores it in a JLabel to add to tiles to represent pieces
     * Scales the images to preferred tile size
     * 
     * @param piece
     * @return new JLabel
     */
    public JLabel getImage(Piece piece) {
        
        String key = tileSize + ":" + "./PieceSprites/new_" + piece.toString() + ".png";
        if(!icons.containsKey(key))
        {
            ImageIcon image = new ImageIcon("./PieceSprites/new_" + piece.toString() + ".png");
            Image scaled = image.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            if (image.getImage() == null) {
                System.out.println("Image not found: " + piece.toString());
                return null;
            }
            icons.put(key, new ImageIcon(scaled));
        }
        return new JLabel(icons.get(key));
    }

    /**
     * Creates the game window, board(with clickable tiles), gamelog, active modifier panel,
     * draw button, and forfeit button. Creates the end screen, which is seperate from the
     * normal game screen. Switches using cardlayout.
     */
    public void initialize() {
        setTitle("The Game");
        getContentPane().setBackground(BACKGROUND);
        setSize(windowLength, windowWidth);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        gamePanel.setBackground(BACKGROUND);

        ///////////
        // Board //
        ///////////
        board.setBounds(windowLength / 2, windowWidth / 2, tileSize * 8, tileSize * 8);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = makeTile(i, j);
                panelBoard[i][j] = square;
                board.add(square);
            }
        }

        GridBagConstraints c = new GridBagConstraints();

        ///////////////////////////
        // Adding board to frame //
        ///////////////////////////
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        board.setBorder(BorderFactory.createLineBorder(Color.BLACK, 6));
        gamePanel.add(board, c);

        // some filler panels, will be replaced with other thingies later
        JPanel fillerTile1 = new JPanel();
        fillerTile1.setBackground(BACKGROUND);
        JPanel fillerTile2 = new JPanel();
        fillerTile2.setBackground(BACKGROUND);
        JPanel fillerTile3 = new JPanel();
        fillerTile3.setBackground(BACKGROUND);
        JPanel fillerTile4 = new JPanel();
        fillerTile4.setBackground(BACKGROUND);

        ////////////////////
        // Forfeit Button //
        ////////////////////
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(BACKGROUND);

        ImageIcon forfeitImage = new ImageIcon("./PieceSprites/ForfeitIcon.png");
        Image scaled = forfeitImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        JPanel buttonHolder = new JPanel(new GridLayout(1, 1));
        buttonHolder.setOpaque(false);
        JButton forfeitButton = new JButton(new ImageIcon(scaled));
        forfeitButton.setFont(new Font("Arial", Font.BOLD, 20));
        forfeitButton.setPreferredSize(new Dimension(150, 150));
        forfeitButton.setMargin(new Insets(0, 0, 0, 0));
        forfeitButton.setBackground(new Color(155, 200, 120));
        forfeitButton.setFocusPainted(false);
        forfeitButton.setOpaque(true);
        forfeitButton.setContentAreaFilled(true);
        forfeitButton.setBorderPainted(true);
        forfeitButton.setBorder(BorderFactory.createLineBorder(OUTLINE, 4));
        forfeitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                game.setWinner(game.getCurrentTurn() == Color.WHITE ? Color.BLACK : Color.WHITE);
                endGameForfeit();
            }
        });

        JPanel backPanel2 = new JPanel(new BorderLayout());
        backPanel2.setBackground(BACKGROUND);

        /////////////////
        // Draw Button //
        /////////////////
        ImageIcon drawImage = new ImageIcon("./PieceSprites/DrawIcon.png");
        scaled = drawImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        JPanel buttonHolder2 = new JPanel(new GridBagLayout());
        buttonHolder2.setOpaque(false);
        JButton drawButton = new JButton(new ImageIcon(scaled));
        drawButton.setFont(new Font("Arial", Font.BOLD, 20));
        drawButton.setPreferredSize(new Dimension(150, 150));
        drawButton.setMargin(new Insets(0, 0, 0, 0));
        drawButton.setBackground(new Color(224, 47, 31));
        drawButton.setFocusPainted(false);
        drawButton.setOpaque(true);
        drawButton.setContentAreaFilled(true);
        drawButton.setBorderPainted(true);
        drawButton.setBorder(BorderFactory.createLineBorder(OUTLINE, 4));
        drawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (localColor != null && game.getCurrentTurn() != localColor) {
                    return;
                } else {
                    if (drawRequested == true) {
                        endGame();
                    }
                    decider = game.getCurrentTurn();
                    drawRequested = true;

                    addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black") + " has offered to draw. "
                            + (game.getCurrentTurn() == Color.WHITE ? "Black" : "White")
                            + " must click draw to accept");
                }
            }
        });

        //////////////
        // Game log //
        //////////////
        textBody = new JTextArea();
        textBody.setEditable(false);
        textBody.setLineWrap(true);
        textBody.setWrapStyleWord(true);
        textBody.setFont(new Font("Arial", Font.PLAIN, 14));
        textBody.setMargin(new Insets(10, 15, 10, 15));
        textBody.setForeground(Color.WHITE);
        textBody.setBackground(BACKGROUND);
        textBody.setText("Game started");

        scrollPane = new JScrollPane(textBody);
        scrollPane.setPreferredSize(new Dimension(220, 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(BACKGROUND, 10));
        scrollPane.setBorder(BorderFactory.createLineBorder(OUTLINE, 5));

        ////////////////////////////
        // Active Modifiers Panel //
        ////////////////////////////
        modifierPanel = new JPanel(new BorderLayout());
        modifierPanel.setBackground(BACKGROUND);
        modifierPanel.setBorder(BorderFactory.createLineBorder(OUTLINE, 5));
        modifierPanel.setPreferredSize(new Dimension(100, 380));

        JLabel modPanelTitle = new JLabel("Active Modifers", SwingConstants.CENTER);
        modPanelTitle.setFont(new Font("Sans", Font.BOLD, 20));
        modPanelTitle.setForeground(new Color(214, 214, 213));

        JLabel activeModsTitle = new JLabel("Active Modifiers", SwingConstants.CENTER);
        activeModsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        activeModsTitle.setForeground(Color.WHITE);

        activeModifiersText = new JTextArea("None");
        activeModifiersText.setEditable(false);
        activeModifiersText.setLineWrap(true);
        activeModifiersText.setWrapStyleWord(true);
        activeModifiersText.setFont(new Font("Arial", Font.PLAIN, 13));
        activeModifiersText.setForeground(Color.WHITE);
        activeModifiersText.setBackground(BACKGROUND);
        activeModifiersText.setMargin(new Insets(8, 8, 8, 8));

        modifierPanel.add(activeModsTitle, BorderLayout.NORTH);
        modifierPanel.add(activeModifiersText, BorderLayout.CENTER);

        //////////////////////////////////////////////
        // Right side panels(theres a lot going on) //
        //////////////////////////////////////////////
        GridBagConstraints bc = new GridBagConstraints(); // made a different Gridbagcontraints object cause i keep
                                                          // screwing the other stuff up :(
        bc.insets = new Insets(4, 5, 4, 5);
        bc.weighty = 1;
        bc.weightx = 1;

        // Game log
        bc.gridx = 0;
        bc.gridy = 0;
        bc.gridwidth = 2;
        bc.gridheight = 2;
        bc.weightx = 1;
        bc.weighty = 1;
        bc.fill = GridBagConstraints.BOTH;
        bc.anchor = GridBagConstraints.CENTER;
        buttonHolder2.add(scrollPane, bc);

        // Active modifier pane
        bc.insets = new Insets(0, 5, 4, 5);
        bc.gridx = 0;
        bc.gridy = 2;
        bc.gridwidth = 1;
        bc.gridheight = 2;
        bc.weightx = 1;
        bc.weighty = 1;
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.NORTHWEST;
        buttonHolder2.add(modifierPanel, bc);

        // Draw button
        bc.insets = new Insets(0, 5, 0, 5);
        bc.gridx = 1;
        bc.gridy = 2;
        bc.gridwidth = 1;
        bc.gridheight = 1;
        bc.weightx = 0;
        bc.weighty = 0;
        bc.fill = GridBagConstraints.NONE;
        bc.anchor = GridBagConstraints.CENTER;
        buttonHolder2.add(drawButton, bc);

        // Forfeit button
        bc.gridx = 1;
        bc.gridy = 3;
        bc.gridwidth = 1;
        bc.gridheight = 1;
        bc.weightx = 0;
        bc.weighty = 0;
        bc.fill = GridBagConstraints.NONE;
        bc.anchor = GridBagConstraints.CENTER;
        buttonHolder2.add(forfeitButton, bc);

        backPanel2.add(buttonHolder2, BorderLayout.CENTER);

        /////////////////////
        // Everything else //
        /////////////////////
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        c.weighty = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        gamePanel.add(fillerTile2, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 3;
        gamePanel.add(fillerTile3, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 3;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 3;
        gamePanel.add(fillerTile4, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        gamePanel.add(backPanel2, c);

        mainPanel.add(gamePanel, "Game");
        mainPanel.add(endPanel, "End");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Game");

        /////////////////////////
        // Creating end screen //
        /////////////////////////
        endPanel.setBackground(new Color(36, 34, 32));
        JButton backButton1 = MainMenuUI.makeButton("Return to main menu", 20, 500, 60, new Color(214, 214, 213));
        backButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new MainMenuUI(800, 800);
                dispose();
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        endPanel.add(backButton1, c);

        redrawBoard();
        updateActiveModifiersPanel();
        setVisible(true);
    }

    /**
     * Helper method that makes the individiual clickable tiles. Creates any background modifier sprites(portals, bricks, sanctuary)
     * Adds the bomb icon or watergun icon to respective piece modifiers. Defines mouseaction respone as handTileClick()
     * 
     * @param i
     *      row
     * @param j
     *      column
     * @return
     */
    private JPanel makeTile(int i, int j) {
        JPanel square = new JPanel(new BorderLayout());
        square.setBackground(tileColor(i, j));
        square.setPreferredSize(new Dimension(tileSize, tileSize));
        square.setBorder(null);

        int[] p1 = game.getPortal1();
        int[] p2 = game.getPortal2();
        boolean showActivePortals = game.arePortalsActive();
        boolean isPortalSquare = ((portal1Placed || showActivePortals) && i == p1[0] && j == p1[1]) ||
                ((portal2Placed || showActivePortals) && i == p2[0] && j == p2[1]);

        boolean isSanctuarySquare = false;
        for (Modifier m : boardgrid.getActiveModifiers()) {
            if (m.getType() == Modifier.Type.SANCTUARY && m.getAffectedRow() == i && m.getAffectedCol() == j) {
                isSanctuarySquare = true;
                break;
            }
        }

        Piece piece = boardgrid.getPieceAt(i, j);

        String modPath = null;
        if (piece != null) {
            for (Modifier mod : boardgrid.getActiveModifiers()) {
                if (mod.getAffectedPiece() != null && mod.getAffectedPiece().equals(piece)) {
                    if ((mod.getType().toString()).equals("Mi Bombo")) {
                        modPath = "./PieceSprites/bombo.png";
                        break;
                    } else if ((mod.getType().toString()).equals("Sniper Bishop")) {
                        modPath = "./PieceSprites/aguabishop.png";
                        break;
                    } else if ((mod.getType().toString()).equals("Invincible Pawns")) {
                        modPath = "./PieceSprites/invincible_pawn.png";
                        break;
                    }
                }
            }
        }

        if (isPortalSquare || isSanctuarySquare) {
            JLayeredPane layered = new JLayeredPane();
            layered.setPreferredSize(new Dimension(tileSize, tileSize));

            String bgPath = isPortalSquare ? "./PieceSprites/portal.png" : "./PieceSprites/sanctuary.png";
            ImageIcon bgImage = new ImageIcon(bgPath);
            Image scaledBg = bgImage.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            JLabel bgLabel = new JLabel(new ImageIcon(scaledBg));
            bgLabel.setBounds(0, 0, tileSize, tileSize);
            layered.add(bgLabel, JLayeredPane.DEFAULT_LAYER);

            if (piece != null) {
                JPanel sprite = makeModifiedPieceSprite(piece, modPath);
                sprite.setOpaque(false);
                sprite.setBounds(0, 0, tileSize, tileSize);
                layered.add(sprite, JLayeredPane.PALETTE_LAYER);
            }

            square.add(layered, BorderLayout.CENTER);
        } else if (piece != null) {
            JPanel sprite = makeModifiedPieceSprite(piece, modPath);
            square.add(sprite, BorderLayout.CENTER);
        }

        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleTileClick(i, j);
            }
        });

        return square;
    }

    /**
     * Dictates the response of clicking a tile. Responsible for allowing the player to pick up and move pieces,
     * place board affecting modifiers, trigger animations, and updates whether the game is over or not. Adds
     * necessary game log messages to the gamelog.
     * 
     * @param i
     *      row
     * @param j
     *      column
     */
    private void handleTileClick(int i, int j) {
        if(animationPlaying){
            return;
        }

        if (choosingModifier) return;
        if (localColor != null && game.getCurrentTurn() != localColor) {
            return;
        }
        if (placingSanctuary) {
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null && target.getColor() != game.getCurrentTurn())
                return;
            game.addModifier(new Modifier(5, Modifier.Type.SANCTUARY, i, j));
            if (network != null) {
                // sanctuary modifier was just added — send it immediately
                Modifier sanctuaryMod = null;
                for (Modifier m : game.getBoard().getActiveModifiers()) {
                    if (m.getType() == Modifier.Type.SANCTUARY && m.getAffectedRow() == i
                            && m.getAffectedCol() == j) {
                        sanctuaryMod = m;
                        break;
                    }
                }
                if (sanctuaryMod != null) {
                    final Modifier toSend = sanctuaryMod;
                    new Thread(() -> network.sendModifier(toSend)).start();
                }
            }
            placingSanctuary = false;
            addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black")
                    + " has chosen the sanctuary placement at " + chessNotation[i][j]);
            updateActiveModifiersPanel();
            redrawBoard();
            game.switchTurn();
            return;
        }

        if (placingWall) {
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null && game.getPortal1()[0] != i && game.getPortal1()[1] != j && game.getPortal2()[0] != i
                    && game.getPortal2()[1] != i) {
                return; // can't place on an occupied square
            }
            boardgrid.setPieceAt(new Brick(Color.GRAY, i, j), i, j);
            game.setModifierOfferedThisCycle(true);
            if (network != null) {
                new Thread(() -> network.sendModifierData(
                        Modifier.Type.BRICK,
                        0,
                        -1,
                        -1,
                        i,
                        j)).start();
            }
            placingWall = false;
            updateActiveModifiersPanel();
            redrawBoard();
            game.switchTurn();
            return;
        }

        if (placingPortal1) {
            System.out.println("Place portal 1");
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null) {
                System.out.println("You cannot place a portal on an occupied square");
                return; // can't place on an occupied square
            }
            game.setPortal1Pos(i, j);
            placingPortal1 = false;
            placingPortal2 = true;
            portal1Placed = true;
            redrawBoard();
            System.out.println("Portal 1 placed at " + i + " " + j);
            return;
        }

        if (placingPortal2) {
            System.out.println("Place portal 2");
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null) {
                System.out.println("You cannot place a portal on an occupied square");
                return; // can't place on an occupied square
            }
            game.setPortal2Pos(i, j);
            game.setPortalsActive(true);
            game.setModifierOfferedThisCycle(true);
            if (network != null) {
                int[] portal1 = game.getPortal1();
                int[] portal2 = game.getPortal2();
                new Thread(() -> network.sendModifierData(
                        Modifier.Type.PORTAL,
                        0,
                        portal1[0],
                        portal1[1],
                        portal2[0],
                        portal2[1])).start();
            }
            placingPortal2 = false;
            portal2Placed = true;
            updateActiveModifiersPanel();
            redrawBoard();
            game.switchTurn();
            System.out.println("Portal 2 placed at " + i + " " + j);
            return;
        }

        if (placingResurrection) {
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null) {
                return; // can't place on an occupied square
            }
            Piece revivedPiece = game.getCapturedPieces(game.getCurrentTurn()).pop();
            game.getBoard().setPieceAt(revivedPiece, i, j);
            game.setModifierOfferedThisCycle(true);
            if (network != null) {
                new Thread(() -> network.sendResurrectionData(revivedPiece, i, j)).start();
            }
            redrawBoard();
            game.switchTurn();
            placingResurrection = false;
            updateActiveModifiersPanel();
            return;
        }

        if (pieceSelected == false) {
            Piece piece = boardgrid.getPieceAt(i, j);
            if (piece == null || piece.getColor() != game.getCurrentTurn())
                return;

            selectedRow = i;
            selectedCol = j;
            pieceSelected = true;

            highLightTile(panelBoard[i][j], HIGHLIGHT);
            highlightLegalMoves(i, j);
            //redrawBoard();
        } else {
            highLightTile(panelBoard[selectedRow][selectedCol], tileColor(selectedRow, selectedCol));
            pieceSelected = false;

            if (selectedRow != i || selectedCol != j) {
                Color current = game.getCurrentTurn();
                Piece piece = game.getBoard().getPieceAt(selectedRow, selectedCol);
                boolean moved = game.makeMove(selectedRow, selectedCol, i, j);
                if (moved) {
                    addChatMessage((current == Color.WHITE ? "White" : "Black") + " has moved "
                            + piece.toString().split("_")[0] + " to " + chessNotation[i][j]);

                    if (network != null) {
                        network.sendMove(selectedRow, selectedCol, i, j);
                    }

                    System.out.println("Move made. currentTurn=" + game.getCurrentTurn()
                            + " moveCount=" + game.getMoveCount()
                            + " shouldOffer=" + game.shouldOfferModifier());

                    if (game.shouldOfferModifier()) {
                        Modifier.Type[] options = game.offeredModifiers();
                        if (network != null) {
                            network.sendModifierOptions(options);
                        }
                        showMods(options);
                    }

                    // Only switch turns if no placement interaction is pending
                    if (!isPlacementModifierActive() && !choosingModifier) {
                        game.switchTurn();
                    }

                    System.out.println("After switchTurn. currentTurn=" + game.getCurrentTurn());

                    if (game.isGameOver()) {
                        endGame();
                    }

                    if (drawRequested && current == decider) {
                        drawRequested = false;
                        addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black")
                                + " declined draw offer by moving");
                    }
                }
            }
            redrawBoard();
            ArrayList<int[]> explosions = game.kaboomKnight();
            System.out.println("Explosions found: " + explosions.size());
            for (int[] explosion : explosions)
            {
                playBomboExplosion(explosion[0], explosion[1]);
            }
            
        }
        updateActiveModifiersPanel();
    }

    public void endGame() {
        Color winner = game.winner();
        String result;
        if (winner == null) {
            result = "The game ended in draw.";
        } else if (winner == Color.WHITE) {
            result = "White won.";
        } else {
            result = "Black won.";
        }
        JLabel outcome = new JLabel(result);
        outcome.setFont(new Font("Sans", Font.BOLD, 20));
        outcome.setForeground(new Color(214, 214, 213));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 20, 20, 20);
        endPanel.add(outcome, c);

        cardLayout.show(mainPanel, "End");

        endPanel.revalidate();
        endPanel.repaint();
    }

    /**
     * Ends the game with the condiiton that it ended in forfeit.
     */
    public void endGameForfeit() {
        Color winner = game.winner();
        String result;
        if (winner == Color.WHITE) {
            result = "White Wins due to Resignation.";
        } else {
            result = "Black Wins due to Resignation.";
        }
        JLabel outcome = new JLabel(result);
        outcome.setFont(new Font("Sans", Font.BOLD, 20));
        outcome.setForeground(new Color(214, 214, 213));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 20, 20, 20);
        endPanel.add(outcome, c);

        cardLayout.show(mainPanel, "End");

        endPanel.revalidate();
        endPanel.repaint();
    }

    private void highLightTile(JPanel panel, Color highLightColor) {
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    }

    // ------------------SHOW MODS METHOD -----------///

    /**
     * Creates the pop up that allows players to select a modifier. Certain modifiers
     * choices are retrived from game and presented to the player. Chosen modifier
     * is updated on the board.
     * 
     * @param options
     */
    public void showMods(Modifier.Type[] options) {
        redrawBoard();
        choosingModifier = true;
        String[] optionStrings = new String[options.length];
        for (int neel = 0; neel < options.length; neel++) {
            optionStrings[neel] = (neel + 1) + ". " + options[neel];
        }

        String chosen = null;
        while (chosen == null) {
            chosen = (String) JOptionPane.showInputDialog(
                    this,
                    "Choose a modifier:",
                    "Modifier",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    optionStrings,
                    optionStrings[0]);
        }
        int choice = Integer.parseInt(chosen.substring(0, 1)) - 1;
        Modifier.Type selected = options[choice];

        if (selected == Modifier.Type.SANCTUARY) {
            placingSanctuary = true;
            redrawBoard();
            choosingModifier = false;
            
            return;
        }

        if (selected == Modifier.Type.RESURRECTION && !game.getCapturedPieces(game.getCurrentTurn()).isEmpty()) {
            choosingModifier = false;
            placingResurrection = true;
            game.setModifierOfferedThisCycle(true);
            return;
        }

        if (selected == Modifier.Type.BRICK) {
            choosingModifier = false;
            placingWall = true;
            return;
        }

        if (selected == Modifier.Type.PORTAL) {
            choosingModifier = false;
            placingPortal1 = true;
            game.setModifierOfferedThisCycle(true);
            return;
        }

        // apply locally
        Modifier appliedModifier = game.handleModifierChoice(options, choice);
        redrawBoard();

        if (network != null) {
            if (appliedModifier != null) {
                final Modifier toSend = appliedModifier;
                new Thread(() -> network.sendModifier(toSend)).start();
            }
        }
        updateActiveModifiersPanel();
        addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black") + " has chosen the " + selected + " modifier");
        choosingModifier = false;
    }

    /**
     * Simple helper for long boolean statements in modifier methods
     * @return
     */
    private boolean isPlacementModifierActive() {
        return placingSanctuary
            || placingWall
            || placingPortal1
            || placingPortal2
            || placingResurrection;
    }

    /**
     * Returns the tile color of the tile at the indicated position on the board
     * @param i
     * @param j
     * @return
     */
    private Color tileColor(int i, int j) {
        return ((i + j) % 2 == 0) ? VERY_LIGHT_BROWN : DARK_BROWN;
    }

    /**
     * Redraws the whole board, updating each tile sperately
     */
    public void redrawBoard() // Our main source of lag/delay
    {
        board.removeAll();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int viewedRow = i;
                int viewedCol = j;

                if (localColor == Color.BLACK) {
                    viewedRow = 7 - i;
                    viewedCol = 7 - j;
                }

                JPanel square = makeTile(viewedRow, viewedCol);
                panelBoard[viewedRow][viewedCol] = square;
                board.add(square);
            }
        }

        board.revalidate();
        board.repaint();
        updateActiveModifiersPanel();
    }

    /**
     * Adds a message to the game log panel
     * @param message
     */
    private void addChatMessage(String message) {
        textBody.append("\n" + message);
        textBody.setCaretPosition(textBody.getDocument().getLength());// Just sets where the next append will be, so rn
                                                                      // at the end of all msgs
    }

    /**
     * Loads in the icons used for modifiers like mi bombo or sniper bishop and places
     * them on the top left corner of piece
     * @param piece
     * @param modPath
     * @return
     */
    private JPanel makeModifiedPieceSprite(Piece piece, String modPath) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(tileSize, tileSize));
        ImageIcon image1 = new ImageIcon("./PieceSprites/new_" + piece.toString() + ".png");

        Image scaled1 = image1.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        JLabel pieceSprite = new JLabel(new ImageIcon(scaled1));
        pieceSprite.setOpaque(false);
        pieceSprite.setBounds(0, 0, tileSize, tileSize);
        layeredPane.add(pieceSprite, JLayeredPane.DEFAULT_LAYER);

        JLabel modifierLabel = new JLabel();
        if (modPath != null) {
            ImageIcon image2 = new ImageIcon(modPath);

            Image scaled2 = image2.getImage().getScaledInstance(tileSize / 3, tileSize / 3, Image.SCALE_SMOOTH);
            modifierLabel = new JLabel(new ImageIcon(scaled2));
            modifierLabel.setOpaque(false);
            modifierLabel.setBounds(0, 0, tileSize / 2, tileSize / 2);
            layeredPane.add(modifierLabel, JLayeredPane.DRAG_LAYER);
        }

        JPanel sprite = new JPanel(new BorderLayout());
        sprite.setOpaque(false);
        sprite.add(layeredPane, BorderLayout.CENTER);

        return sprite;
    }

    /**
     * Updates the active modifier panel with the active modifiers retrived from game
     */
    private void updateActiveModifiersPanel() {
        ArrayList<Modifier> modifiers = boardgrid.getActiveModifiers();

        if (modifiers.isEmpty()) {
            activeModifiersText.setText("None");
            return;
        }

        String text = "";

        for (Modifier mod : modifiers) {
            text += "- " + mod.getType();

            if (mod.getAffectedPiece() != null) {
                text += " on " + mod.getAffectedPiece();
            }

            if (mod.getTurnsRemaining() > 0) {
                text += " for " + mod.getTurnsRemaining() + " more turns";
            }

            text += "\n";
        }

        activeModifiersText.setText(text);
    }

    /**
     * Outlines the legal moves a certain piece at the indicated position on the board can move to
     * using piece can move to logic.
     * @param row
     * @param col
     */
    private void highlightLegalMoves(int row, int col)
    {
        Piece piece  = boardgrid.getPieceAt(row, col);
        if(piece == null)
        {
            return;
        }

        ArrayList<String> legalMoves = piece.getLegalMoves(boardgrid);

        for(String move : legalMoves)
        {
            int r = Character.getNumericValue(move.charAt(0));
            int c = Character.getNumericValue(move.charAt(2));

            panelBoard[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        }
    }

    /**
     * Loads the gif animation, temporarily freezes tile clicks to allow for the animation to play
     * and carrys out the intended effect of the mi bombo modifier
     * @param row
     * @param col
     */
    public void playBomboExplosion(int row, int col)
    {
        animationPlaying = true;
        System.out.println("Playing explosion at " + row + ", " + col);
        JLayeredPane kaboom = new JLayeredPane();
        kaboom.setPreferredSize(new Dimension(tileSize, tileSize));

        JPanel tile = panelBoard[row][col];

        ImageIcon icon = new ImageIcon("./PieceSprites/explosion.gif");

        JLabel gifLabel = new JLabel(icon);
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setVerticalAlignment(SwingConstants.CENTER);

        tile.removeAll();
        tile.setLayout(new BorderLayout());
        tile.add(gifLabel, BorderLayout.CENTER);

        tile.revalidate();
        tile.repaint();

        Timer timer = new Timer(6000, e -> {
            ((Timer)e.getSource()).stop();
            Piece[][] boardArr = boardgrid.getBoard();

            for (int x = row - 1; x <= row + 1; x++)
            {
                for (int y = col - 1; y <= col + 1; y++)
                {
                    if (x >= 0 && x < 8 && y >= 0 && y < 8)
                    {
                        if(boardArr[x][y] instanceof King)
                        {
                            game.setWinner(boardArr[x][y].getColor().equals(Color.WHITE) ? Color.BLACK: Color.WHITE );
                        }
                        boardArr[x][y] = null;
                    }
                }
            }

            redrawBoard();
            animationPlaying = false;

            if(game.winner() != null)
            {
                endGame();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
