import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

public class BoardUI extends JFrame
{
    // General Sizes    //
    private int windowWidth;
    private int windowLength;
    private int tileSize;

    // Tile Colors  //
    public static final Color VERY_LIGHT_BROWN = new Color(254,228,187);
    public static final Color DARK_BROWN = new Color(205,154,117);
    public static final Color HIGHLIGHT = new Color(247, 247, 105);

    // Some useful stuff for UI logic   //
    private Game game;
    private Board boardgrid;
    private JPanel[][] panelBoard = new JPanel[8][8];
    private JPanel board = new JPanel(new GridLayout(8,8));
    
    // Mosuse Inputs    //
    private int selectedRow;
    private int selectedCol;
    private boolean pieceSelected = false;

    // Board UI references the Game class for key logic //
    // Chess window and board are initialized           //
    public BoardUI(int windowW, int windowL, int tileS, Game game)
    {
        windowWidth = windowW;
        windowLength = windowL;
        tileSize = tileS;
        this.game = game;
        boardgrid = game.getBoard();
        initialize();
    }

    public JLabel getImage(Piece piece)
    {
        // the "./----" accesses that images directly from the project directory    //
        // so that we don't need to replace the file directory one every different  //
        // machine.                                                                 //
        ImageIcon image = new ImageIcon("./PieceSprites/new_" + piece.toString() + ".png");
        if (image.getImage() == null) {
            System.out.println("Image not found: " + piece.toString());
            return null;
        }
        Image scaled = image.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH); // Scales the image to preferred size
        return new JLabel(new ImageIcon(scaled));
    }

    // Makes the window and board, as well as some filler tiles //
    // on the side.                                             //
    public void initialize()
    {
        // General window intitializing //
        setTitle("The Game");
        setSize(windowLength,windowWidth);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout()); // Allows for custom layouts(ie. big middle tile for board, 
        // and thinner tiles on sides for addons like pieces taken or timer.)

        board.setBounds(windowLength/2, windowWidth/2, tileSize * 8, tileSize*8);

        // Fill our board JPanel with white and gray tiles to represent a chess board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = makeTile(i, j);
                panelBoard[i][j] = square;
                board.add(square);
            }
        }

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth =1;
        c.gridheight =1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        add(board, c);

        // Side Panels
        JPanel fillerTile1 = new JPanel();
        fillerTile1.setBackground(Color.WHITE);
        JPanel fillerTile2 = new JPanel();
        fillerTile2.setBackground(Color.WHITE);
        JPanel fillerTile3 = new JPanel();
        fillerTile3.setBackground(Color.WHITE);
        JPanel fillerTile4 = new JPanel();
        fillerTile4.setBackground(Color.WHITE);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 1;
        add(fillerTile1, c);

        c.gridx = 3;
        c.gridy = 1;
        add(fillerTile2, c);

        c.gridx = 1;
        c.gridy = 0;
        add(fillerTile3, c);

        c.gridx = 1;
        c.gridy = 3;
        add(fillerTile4, c);
        
        setVisible(true);
    }

    // Helper method to create a sqaure tile for a chess board  //
    // Useful when creating the board or redrawing. Eliminates  //
    // some logic overlap in initialize() and redraw().         //
    private JPanel makeTile(int i, int j)
    {
        JPanel square = new JPanel(new BorderLayout());
        Color tileColor = tileColor(i, j);
        square.setBackground(tileColor);
        square.setPreferredSize(new Dimension(tileSize, tileSize));

        Piece piece = boardgrid.getPieceAt(i,j);
        if (boardgrid.getPieceAt(i,j) != null) {
            JLabel label = getImage(piece);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            square.add(label, BorderLayout.CENTER);
        }

        square.addMouseListener(new MouseAdapter() {
            @Override
            // Using mousePressed instead of mouseClicked because mousePressed feels better //
            // Allows for clicking even when mouse moves, but the other does not allow      //
            // moving while clicking.                                                       //
            public void mousePressed(MouseEvent e) {
                handleTileClick(i, j);
            }
        });

        return square;
    }

    private void handleTileClick(int i, int j)
    {
        if(pieceSelected == false) // First click, selecting a piece to move.   //
        {
            Piece piece = boardgrid.getPieceAt(i, j);
            if (piece == null || piece.getColor() != game.getCurrentTurn())
            {
                return;
            }

            selectedRow = i;
            selectedCol = j;
            pieceSelected = true;
            highLightTile(panelBoard[i][j], HIGHLIGHT);
        }
        else    // Second click, moving the peice to a legal position. Nothing happens if illegal move. //
        {
            highLightTile(panelBoard[selectedRow][selectedCol], tileColor(selectedRow, selectedCol));
            pieceSelected = false;

            if (selectedRow != i || selectedCol != j)
            {
                game.makeMove(selectedRow, selectedCol, i, j);
                redrawBoard();
                if (game.isGameOver()) {
                    System.out.println(game.getCurrentTurn() == Color.WHITE ? "White wins!" : "Black wins!");
                }
                else if (game.shouldOfferModifier()) {
                    Modifier.Type[] options = game.offeredModifiers();
                    String[] optionStrings = new String[options.length];
                    for (int neel = 0; neel < options.length; neel++) {
                        optionStrings[neel] = (neel + 1) + ". " + options[neel];
                    }
                    String chosen = (String) JOptionPane.showInputDialog(
                        this,
                        "Choose a modifier:",
                        "Modifier",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        optionStrings,
                        optionStrings[0]
                    );
                    if (chosen != null) {
                        int choice = Integer.parseInt(chosen.substring(0, 1)) - 1;
                        if (options[choice] == Modifier.Type.EXPLODING_PIECE) {
                            int[] square = game.getBoard().randomSquare(Knight.class, game.getCurrentTurn());
                            Piece knight = game.getBoard().getPieceAt(square[0], square[1]);
                            System.out.println("The knight on " + (char)('a' + knight.getCol()) + Math.abs(knight.getRow() - 8) + " is about to explode mi bomboclat in 3 turns");
                            game.addModifier(new Modifier(10, Modifier.Type.EXPLODING_PIECE, knight));
                        }
                        else if (options[choice] == Modifier.Type.SNIPER_BISHOP) {
                            int[] square = game.getBoard().randomSquare(Bishop.class, game.getCurrentTurn());
                            Piece bishop = game.getBoard().getPieceAt(square[0], square[1]);
                            System.out.println("The bishop on " + (char)('a' + bishop.getCol()) + Math.abs(bishop.getRow() - 8) + " is una esniper for 3 turns");
                            game.addModifier(new Modifier(5, Modifier.Type.SNIPER_BISHOP, bishop));
                        }
                        else {
                            game.addModifier(new Modifier(5, options[choice]));
                        }
                        redrawBoard();
                    }
                }
            }
        }

        if(game.isGameOver())
        {
            endGame();
        }
    }

    private void endGame()
    {
        // End game screen then trnasition back to MainMenu once made   //
        Color winner = game.winner();
        MainMenuUI newMenu = new MainMenuUI(windowWidth, windowLength);
        
        dispose();
    }

    private void highLightTile(JPanel panel, Color highLightColor)
    {
        panel.setBackground(highLightColor);
    }

    private boolean hasLabel(JPanel panel) // Not used, but necessary for other helper getLabel //
    {
        Component[] components = panel.getComponents();
    
        for (Component comp : components)
        {
            if (comp instanceof JLabel)
            {
                return true;
            }
        }
        return false;
    }
    
    private Component getLabel(JPanel panel) // Not used now but could be useful when changing player sprites to indicate some type of modifier or powerup
    {
        if(hasLabel(panel))
        {
            Component[] components = panel.getComponents();
    
            for (Component comp : components)
            {
                if (comp instanceof JLabel)
                {
                    return comp;
                }
            }
        }
        return null;
    }

    private Color tileColor(int i, int j)   // Reduces a lot of space consuming logic deciding tile color   //
    {
        if((i +j) % 2 == 0)
        {
            return VERY_LIGHT_BROWN;
        }
        else
        {
            return DARK_BROWN;
        }
    }

    public void redrawBoard()   // Redraws the entire board, updating the postitions of each piece  //
    {                           // Could change to only redrawing the two squares that were changed //
        board.removeAll();      // to make more efficient                                           //
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
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
