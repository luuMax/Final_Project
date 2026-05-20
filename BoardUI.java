import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

public class BoardUI
    extends JFrame
{
    // General Sizes //
    private int               windowWidth;
    private int               windowLength;
    private int               tileSize;

    // Tile Colors //
    public static final Color VERY_LIGHT_BROWN = new Color(254, 228, 187);
    public static final Color DARK_BROWN       = new Color(205, 154, 117);
    public static final Color HIGHLIGHT        = new Color(247, 247, 105);
    public static final Color BACKGROUND       = new Color(41, 41, 41);
    public static final Color OUTLINE          = new Color(37, 28, 24);

    // Some useful stuff for UI logic //
    private Game              game;
    private Board             boardgrid;
    private JPanel[][]        panelBoard       = new JPanel[8][8];
    private JPanel            board            = new JPanel(new GridLayout(8, 8));

    // Mouse Inputs //
    private int               selectedRow;
    private int               selectedCol;
    private boolean           pieceSelected    = false;

    // Modifiers
    private boolean           placingSanctuary = false;
    private boolean           placingWall      = false;
    private boolean           placingPortal1   = false;
    private boolean           placingPortal2   = false;

    // Networking //
    private NetworkManager    network          = null;
    private Color             localColor       = null;

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
        setLayout(new GridBagLayout());

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
        add(board, c);

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
        forfeitButton.setPreferredSize(new Dimension(70, 70));
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
                endGame();
            }
        });
        buttonHolder.add(forfeitButton);
        backPanel.add(buttonHolder, BorderLayout.CENTER);

        JPanel backPanel2 = new JPanel(new BorderLayout());
        backPanel2.setBackground(BACKGROUND);

        // creating draw button
        ImageIcon drawImage = new ImageIcon("./PieceSprites/DrawIcon.png");
        scaled = drawImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JPanel buttonHolder2 = new JPanel(new GridLayout(6, 1));
        buttonHolder2.setOpaque(false);
        JButton drawButton = new JButton(new ImageIcon(scaled));
        drawButton.setFont(new Font("Arial", Font.BOLD, 20));
        drawButton.setPreferredSize(new Dimension(70, 70));
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
                endGame();
            }
        });

        // adding the stuff to the big panel
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        buttonHolder2.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 1;
        buttonHolder2.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 2;
        buttonHolder2.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 3;
        buttonHolder2.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 4;
        buttonHolder2.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 5;
        buttonHolder2.add(drawButton, c);
        backPanel2.add(buttonHolder2, BorderLayout.CENTER);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        add(fillerTile1, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        c.weighty = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        add(fillerTile2, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 3;
        add(fillerTile3, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 3;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 3;
        add(fillerTile4, c);

        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.SOUTHEAST;
        add(backPanel, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        add(backPanel2, c);

        // Modifier panel

        setVisible(true);
    }


    private JPanel makeTile(int i, int j)
    {
        JPanel square = new JPanel(new BorderLayout());
        square.setBackground(tileColor(i, j));
        square.setPreferredSize(new Dimension(tileSize, tileSize));

        Piece piece = boardgrid.getPieceAt(i, j);
        if (piece != null)
        {
            JLabel label = getImage(piece);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            square.add(label, BorderLayout.CENTER);
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
            game.setPortal1Pos(i, j);
            placingPortal1 = false;
            placingPortal2 = true;
            redrawBoard();
            return;
        }

        if (placingPortal2) {
            game.setPortal2Pos(i, j);
            game.setPortalsActive(true);
            game.setModifierOfferedThisCycle(true);
            placingPortal2 = false;
            redrawBoard();
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
                boolean moved = game.makeMove(selectedRow, selectedCol, i, j);

                if (moved && network != null)
                {
                    network.sendMove(selectedRow, selectedCol, i, j);
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
            }
        }
    }

    private void showEndScreen()
    {
        
    }


    void endGame()
    {
        Color winner = game.winner();
        MainMenuUI newMenu = new MainMenuUI(windowWidth, windowLength);
        dispose();
    }


    private void highLightTile(JPanel panel, Color highLightColor)
    {
        panel.setBackground(highLightColor);
    }

    // ------------------SHOW MODES METHOD -----------///


    void showMods(Modifier.Type[] options)
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

        int choice = Integer.parseInt(chosen.substring(0, 1)) - 1;
        Modifier.Type selected = options[choice];

        if (selected == Modifier.Type.SANCTUARY)
        {
            placingSanctuary = true;
            game.handleModifierChoice(options, choice);
            redrawBoard();
            return;
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


    public void redrawBoard()
    {
        board.removeAll();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                JPanel square = makeTile(i, j);
                panelBoard[i][j] = square;
                board.add(square);
            }
        }

        board.revalidate();
        board.repaint();
    }


}
