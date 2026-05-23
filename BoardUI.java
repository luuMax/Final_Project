import javax.swing.*;
import javax.swing.border.Border;

/* import java.awt.event.ActionListener; */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.*;

public class BoardUI extends JFrame
{
    // General Sizes //
    private int windowWidth;
    private int windowLength;
    private int tileSize;

    // Chess notation //
    String chessNotation[][] = {
        {"a8", "b8","c8", "d8","e8", "f8", "g8", "h8"},
        {"a7", "b7","c7", "d7","e7", "f7", "g7", "h7"},
        {"a6", "b6","c6", "d6","e6", "f6", "g6", "h6"},
        {"a5", "b5","c5", "d5","e5", "f5", "g5", "h5"},
        {"a4", "b4","c4", "d4","e4", "f4", "g4", "h4"},
        {"a3", "b3","c3", "d3","e3", "f3", "g3", "h3"},
        {"a2", "b2","c2", "d2","e2", "f2", "g2", "h2"},
        {"a1", "b1","c1", "d1","e1", "f1", "g1", "h1"}
    };

    // Tile Colors //
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

    // Mouse Inputs //
    private int selectedRow;
    private int selectedCol;
    private boolean pieceSelected = false;

    // Modifiers
    private boolean           placingSanctuary = false;
    private boolean           placingWall      = false;
    private boolean           placingPortal1   = false;
    private boolean           placingPortal2   = false;
    private boolean           placingResurrection = false;

    // Networking //
    private NetworkManager network = null;
    private Color localColor = null;

    // Game ending //
    private boolean drawRequested = false;
    private Color decider = null;


    // Chat/Gamelog //
    JTextArea textBody = new JTextArea("Game started.\n");
    JScrollPane scrollPane = new JScrollPane(textBody);

    // 4-param constructor for local play — delegates to full constructor //
    public BoardUI(int windowW, int windowL, int tileS, Game game)
    {
        this(windowW, windowL, tileS, game, null, null);
    }

    // 6-param constructor for networked play //
    public BoardUI(int windowW, int windowL, int tileS, Game game, NetworkManager net, Color loCol)
    {
        windowWidth = windowW;
        windowLength = windowL;
        tileSize = tileS;
        this.game = game;
        this.network = net;
        this.localColor = loCol;
        boardgrid = game.getBoard();
        initialize();
    }

    public JLabel getImage(Piece piece)
    {
        ImageIcon image = new ImageIcon("./PieceSprites/new_" + piece.toString() + ".png");
        if (image.getImage() == null)
        {
            System.out.println("Image not found: " + piece.toString());
            return null;
        }
        Image scaled = image.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }


    public void initialize()
    {
        setTitle("The Game");
        getContentPane().setBackground(BACKGROUND);
        setSize(windowLength, windowWidth);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        gamePanel.setBackground(BACKGROUND);
        

        // creating the board
        board.setBounds(windowLength / 2, windowWidth / 2, tileSize * 8, tileSize * 8);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                JPanel square = makeTile(i, j);
                panelBoard[i][j] = square;
                board.add(square);
            }
        }

        GridBagConstraints c = new GridBagConstraints();

        // adding board to big panel
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

        // creating the forfeit button
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(BACKGROUND);

        ImageIcon forfeitImage = new ImageIcon("./PieceSprites/ForfeitIcon.png");
        Image scaled = forfeitImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JPanel buttonHolder = new JPanel(new GridLayout(1, 1));
        buttonHolder.setOpaque(false);
        JButton forfeitButton = new JButton(new ImageIcon(scaled));
        forfeitButton.setFont(new Font("Arial", Font.BOLD, 20));
        forfeitButton.setPreferredSize(new Dimension(140, 100));
        forfeitButton.setMargin(new Insets(0, 0, 0, 0));
        forfeitButton.setBackground(new Color(155, 200, 120));
        forfeitButton.setFocusPainted(false);
        forfeitButton.setOpaque(true);
        forfeitButton.setContentAreaFilled(true);
        forfeitButton.setBorderPainted(true);
        forfeitButton.setBorder(BorderFactory.createLineBorder(OUTLINE, 4));
        forfeitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                game.setWinner(game.getCurrentTurn() == Color.WHITE ? Color.BLACK : Color.WHITE);
                endGameForfeit();
            }
        });
        buttonHolder.add(forfeitButton);
        backPanel.add(buttonHolder, BorderLayout.CENTER);

        JPanel backPanel2 = new JPanel(new BorderLayout());
        backPanel2.setBackground(BACKGROUND);

        // creating draw button
        ImageIcon drawImage = new ImageIcon("./PieceSprites/DrawIcon.png");
        scaled = drawImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JPanel buttonHolder2 = new JPanel(new GridBagLayout());
        buttonHolder2.setOpaque(false);
        JButton drawButton = new JButton(new ImageIcon(scaled));
        drawButton.setFont(new Font("Arial", Font.BOLD, 20));
        drawButton.setPreferredSize(new Dimension(140, 100));
        drawButton.setMargin(new Insets(0, 0, 0, 0));
        drawButton.setBackground(new Color(226, 48, 32));
        drawButton.setFocusPainted(false);
        drawButton.setOpaque(true);
        drawButton.setContentAreaFilled(true);
        drawButton.setBorderPainted(true);
        drawButton.setBorder(BorderFactory.createLineBorder(OUTLINE, 4));
        drawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (localColor != null && game.getCurrentTurn() != localColor)
                {
                    return;
                }
                else
                {
                    if(drawRequested == true)
                    {
                        endGame();
                    }
                    decider = game.getCurrentTurn();
                    drawRequested = true;
                    
                    addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black") + " has offered to draw. " + (game.getCurrentTurn() == Color.WHITE ? "Black" : "White")+ " must click draw to accept");
                }
            }
        });

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
        scrollPane.setBorder(BorderFactory.createLineBorder(OUTLINE, 3));
        // adding the stuff to the big panel

        GridBagConstraints bc = new GridBagConstraints();
        bc.insets = new Insets(0, 0, 0, 0);
        bc.gridx = 0;
        bc.weightx = 1;

        bc.gridy = 0;
        bc.weighty = 1;
        bc.fill = GridBagConstraints.BOTH;
        buttonHolder2.add(scrollPane, bc);

        bc.gridy = 1;
        bc.weighty = 0;
        bc.fill = GridBagConstraints.NONE;
        bc.anchor = GridBagConstraints.SOUTHEAST;
        buttonHolder2.add(drawButton, bc);
        bc.anchor = GridBagConstraints.CENTER;
        backPanel2.add(buttonHolder2, BorderLayout.CENTER);

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
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTHEAST;
        gamePanel.add(backPanel, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
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

        //////////////////////////
        // Creating end screen  //
        //////////////////////////
        
        endPanel.setBackground(new Color(36,34,32));
        JButton backButton1 = MainMenuUI.makeButton("Return to main menu", 20, 500, 60, new Color(214,214,213));
        backButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                new MainMenuUI(800, 800);
                dispose();
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        endPanel.add(backButton1, c);

        redrawBoard();
        setVisible(true);
    }


    private JPanel makeTile(int i, int j)
    {
        JPanel square = new JPanel(new BorderLayout());
        square.setBackground(tileColor(i, j));
        square.setPreferredSize(new Dimension(tileSize, tileSize));
        
        Piece piece = boardgrid.getPieceAt(i, j);

        if(piece != null)
        {
            String modPath = null;
            ArrayList<Modifier> modifiers = boardgrid.getActiveModifiers();

            for (Modifier mod : modifiers)
            {
                if(mod.getAffectedPiece() != null && mod.getAffectedPiece().equals(piece))
                {
                    if ((mod.getType().toString()).equals("Mi Bomboclart"))
                    {
                        System.out.println("Adding bomb at " + i + ", " + j);
                        modPath = "./PieceSprites/bombo.png";
                        break;
                    }
                    else if ((mod.getType().toString()).equals("Sniper Bishop"))
                    {
                        modPath = "./PieceSprites/aguabishop.png";
                        break;
                    }
                    else if ((mod.getType().toString()).equals("[Title Card] Pawns"))
                    {
                        modPath = "./PieceSprites/invincible_pawn.png";
                        break;
                    }
                }
            }
            JPanel sprite = makeModifiedPieceSprite(piece, modPath);
            square.add(sprite, BorderLayout.CENTER);
        }

        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                handleTileClick(i, j);
            }
        });

        return square;
    }


    private void handleTileClick(int i, int j)
    {
        if (localColor != null && game.getCurrentTurn() != localColor)
        {
            return;
        }
        if (placingSanctuary)
        {
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null && target.getColor() != game.getCurrentTurn())
                return;
            game.addModifier(new Modifier(5, Modifier.Type.SANCTUARY, i, j));
            if (network != null)
            {
                // sanctuary modifier was just added — send it immediately
                Modifier sanctuaryMod = null;
                for (Modifier m : game.getBoard().getActiveModifiers())
                {
                    if (m.getType() == Modifier.Type.SANCTUARY && m.getAffectedRow() == i
                        && m.getAffectedCol() == j)
                    {
                        sanctuaryMod = m;
                        break;
                    }
                }
                if (sanctuaryMod != null)
                {
                    final Modifier toSend = sanctuaryMod;
                    new Thread(() -> network.sendModifier(toSend)).start();
                }
            }
            placingSanctuary = false;
            addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black") + " has chosen the sanctuary placement at " + chessNotation[i][j]);
            redrawBoard();
            return;
        }

        if (placingWall) {
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null) {
                return; // can't place on an occupied square
            }
            boardgrid.setPieceAt(new Brick(Color.GRAY, i, j), i, j);
            game.setModifierOfferedThisCycle(true);
            placingWall = false;
            redrawBoard();
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
            placingPortal2 = false;
            redrawBoard();
            System.out.println("Portal 2 placed at " + i + " " + j);
            return;
        }

        if (placingResurrection) {
            Piece target = boardgrid.getPieceAt(i, j);
            if (target != null) {
                return; // can't place on an occupied square
            }
            game.getBoard().setPieceAt(game.getCapturedPieces(game.getCurrentTurn()).pop(), i, j);
            redrawBoard();
            placingResurrection = false;
            return;
        }

        if (pieceSelected == false)
        {
            Piece piece = boardgrid.getPieceAt(i, j);
            if (piece == null || piece.getColor() != game.getCurrentTurn())
                return;

            selectedRow = i;
            selectedCol = j;
            pieceSelected = true;
            highLightTile(panelBoard[i][j], HIGHLIGHT);
        }
        else
        {
            highLightTile(
                panelBoard[selectedRow][selectedCol],
                tileColor(selectedRow, selectedCol));
            pieceSelected = false;

            if (selectedRow != i || selectedCol != j)
            {
                Color current = game.getCurrentTurn();
                Piece piece = game.getBoard().getPieceAt(selectedRow, selectedCol);
                boolean moved = game.makeMove(selectedRow, selectedCol, i, j);

                if (moved)
                {
                    addChatMessage((current == Color.WHITE ? "White" : "Black") + " has moved " + piece.toString().split("_")[0] + " to " + chessNotation[i][j]);
                    if(network != null)
                    {
                        network.sendMove(selectedRow, selectedCol, i, j);
                    }
                }
                

                redrawBoard();

                if (game.isGameOver())
                {
                    endGame();
                }
                else if (moved && game.shouldOfferModifier())
                {
                    Modifier.Type[] options = game.offeredModifiers();
                    if (network != null)
                    {
                        network.sendModifierOptions(options);
                    }
                    showMods(options);
                }

                if(drawRequested == true &&  current == decider)
                {
                    drawRequested = false;
                    
                    addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black") + " declined draw offer by moving");
                }
            }

        }

    }


    public void endGame()
    {
        Color winner = game.winner();
        String result;
        if(winner == null)
        {
            result = "The game ended in draw.";
        }
        else if(winner == Color.WHITE)
        {
            result = "White won by checkmate.";
        }
        else
        {
            result = "Black won by checkmate.";
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

    public void endGameForfeit()
    {
        Color winner = game.winner();
        String result;
        if(winner == Color.WHITE)
        {
            result = "White Wins due to Resignation.";
        }
        else
        {
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



    private void highLightTile(JPanel panel, Color highLightColor)
    {
        panel.setBackground(highLightColor);
    }

    // ------------------SHOW MODS METHOD -----------///


    public void showMods(Modifier.Type[] options)
    {
        String[] optionStrings = new String[options.length];
        for (int neel = 0; neel < options.length; neel++)
        {
            optionStrings[neel] = (neel + 1) + ". " + options[neel];
        }

        String chosen = null;
        while (chosen == null)
        {
            chosen = (String)JOptionPane.showInputDialog(
                this,
                "Choose a modifier:",
                "Modifier",
                JOptionPane.PLAIN_MESSAGE,
                null,
                optionStrings,
                optionStrings[0]);
        }
        addChatMessage((game.getCurrentTurn() == Color.WHITE ? "White" : "Black") + " has chosen the " + chosen.substring(2) + "modifier");
        int choice = Integer.parseInt(chosen.substring(0, 1)) - 1;
        Modifier.Type selected = options[choice];

        if (selected == Modifier.Type.SANCTUARY)
        {
            placingSanctuary = true;
            game.handleModifierChoice(options, choice);
            redrawBoard();
            return;
        }

        if (selected == Modifier.Type.RESURRECTION && !game.getCapturedPieces(game.getCurrentTurn()).isEmpty()) {
            placingResurrection = true;
        }

        if (selected == Modifier.Type.BRICK)
        {
            placingWall = true;
            return;
        }

        if (selected == Modifier.Type.PORTAL) {
            placingPortal1 = true;
            return;
        }

        // apply locally
        game.handleModifierChoice(options, choice);
        redrawBoard();

        if (network != null)
        {
            // find the modifier that was just added and send it
            // it will be the most recently added one of the chosen type
            Modifier justAdded = null;
            for (Modifier m : game.getBoard().getActiveModifiers())
            {
                if (m.getType() == selected)
                {
                    justAdded = m;
                }
            }
            if (justAdded != null)
            {
                final Modifier toSend = justAdded;
                new Thread(() -> network.sendModifier(toSend)).start();
            }
        }
    }

    private Color tileColor(int i, int j)
    {
        return ((i + j) % 2 == 0) ? VERY_LIGHT_BROWN : DARK_BROWN;
    }


    public void redrawBoard() //Our main source of lag/delay
    {
        board.removeAll();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                int viewedRow = i;
                int viewedCol = j;

                if(localColor == Color.BLACK)
                {
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
    }

    private void addChatMessage(String message)
    {
        textBody.append("\n" + message);
        textBody.setCaretPosition(textBody.getDocument().getLength());// Just sets where the next append will be, so rn at the end of all msgs
    }  
    
    private JPanel makeModifiedPieceSprite(Piece piece, String modPath)
    {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(tileSize,tileSize));
        ImageIcon image1 = new ImageIcon("./PieceSprites/new_" + piece.toString() +".png");

        Image scaled1 = image1.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        JLabel pieceSprite = new JLabel(new ImageIcon(scaled1));
        pieceSprite.setOpaque(false);
        pieceSprite.setBounds(0,0,tileSize,tileSize);
        layeredPane.add(pieceSprite, JLayeredPane.DEFAULT_LAYER);

        JLabel modifierLabel = new JLabel();
        if(modPath != null)
        {
            ImageIcon image2 = new ImageIcon(modPath);

            Image scaled2 = image2.getImage().getScaledInstance(tileSize/3, tileSize/3, Image.SCALE_SMOOTH);
            modifierLabel = new JLabel(new ImageIcon(scaled2));
            modifierLabel.setOpaque(false);
            modifierLabel.setBounds(0,0,tileSize/2,tileSize/2);
            layeredPane.add(modifierLabel, JLayeredPane.DRAG_LAYER); 
        } 

        JPanel sprite = new JPanel(new BorderLayout());
        sprite.setOpaque(false);
        sprite.add(layeredPane, BorderLayout.CENTER);

        return sprite;
    }
}
