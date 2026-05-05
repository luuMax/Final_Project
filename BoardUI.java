import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

public class BoardUI extends JFrame{
    
    private int windowWidth;
    private int windowLength;
    private int tileSize;

    public static final Color VERY_LIGHT_BROWN = new Color(254,228,187);
    public static final Color DARK_BROWN = new Color(205,154,117);
    public static final Color HIGHLIGHT = new Color(130, 200, 100, 180);

    Board boardgrid;
    JPanel[][] panelBoard = new JPanel[8][8];
    JPanel board = new JPanel(new GridLayout(8,8));
    
    private int selectedRow;
    private int selectedCol;
    private int moveToRow;
    private int moveToCol;
    private boolean pieceSelected = false;
    private Piece pieceToMove = null;

    //BoardUI will eventually need a reference to game, to update board visually and stuff
    public BoardUI(int windowW, int windowL, int tileS, Board board)
    {
        windowWidth = windowW;
        windowLength = windowL;
        tileSize = tileS;
        boardgrid = board;
        initialize();
    }

    public JLabel getImage(Piece piece)
    {
        /*
        The underscored must be removed and placed with the appropriate path to the PieceSprites
        folder on your machine. Next push will resolve this, but currently, for GUI testing purposes,
        please replace the file path.
        */
        ImageIcon image = new ImageIcon("/Users/neelparimi/Documents/GitHub/Final_Project/PieceSprites/new_" + piece.toString() + ".png");
        if (image.getImage() == null) {
            System.out.println("Image not found: " + piece.toString());
            return null;
        }
        Image scaled = image.getImage().getScaledInstance( (int)(tileSize * 2), (int)(tileSize * 2), Image.SCALE_SMOOTH); // Scales the image to preferred size
        return new JLabel(new ImageIcon(scaled));
    }

    public void initialize()
    {
        setTitle("Chess");
        setSize(windowLength,windowWidth);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout()); // Allows for custom layouts(ie. big middle tile for board, 
        // and thinner tiles on sides for addons like pieces taken or timer.)
        // We are NOT using GridLayout because that forces all tiles, including our board, the same size

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
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 5;
        c.weighty = 5;
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

    private JPanel makeTile(int i, int j)
    {
        JPanel square = new JPanel(new BorderLayout());
        if ((i + j) % 2 == 0) {
            square.setBackground(VERY_LIGHT_BROWN);
            square.setPreferredSize(new Dimension(tileSize, tileSize));
        } else {
            square.setBackground(DARK_BROWN);
            square.setPreferredSize(new Dimension(tileSize, tileSize));
        }

        Piece piece = boardgrid.getPieceAt(i,j);
        if (boardgrid.getPieceAt(i,j) != null) {
            JLabel label = getImage(piece);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            square.add(label, BorderLayout.CENTER);
        }

        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTileClick(i, j);
            }
        });

        return square;
    }

    private void handleTileClick(int i, int j)
    {
        if(pieceSelected == false)
        {
            if (hasLabel(panelBoard[i][j])) {
                selectedRow = i;
                selectedCol = j;
                pieceSelected = true;
                highLightTile(panelBoard[selectedRow][selectedCol], HIGHLIGHT);
                System.out.println("I have been clicked");
            }
        }
        else
        {
            moveToRow = i;
            moveToCol = j;
            if (selectedRow == moveToRow && moveToCol == selectedCol) {
                pieceSelected = false;
                if((selectedCol + selectedRow) % 2 == 0)
                {
                    highLightTile(board, VERY_LIGHT_BROWN);
                }
                else
                {
                    highLightTile(board, DARK_BROWN);
                }
            }
            else
            {
                System.out.println("I have been clicked again");
                pieceToMove = boardgrid.getPieceAt(selectedRow, selectedCol);
                if(pieceToMove.canMoveTo(selectedRow, selectedCol, moveToRow, moveToCol, boardgrid))
                {
                    boardgrid.setPieceAt(pieceToMove, moveToRow, moveToCol);
                    boardgrid.setPieceAt(null, selectedRow, selectedCol);

                    pieceSelected = false;
                    if((selectedCol + selectedRow) % 2 == 0)
                    {
                        highLightTile(board, VERY_LIGHT_BROWN);
                    }
                    else
                    {
                        highLightTile(board, DARK_BROWN);
                    }
                }
            }
        }
        redrawBoard();
    }

    private void highLightTile(JPanel panel, Color highLightColor)
    {
        panel.setBackground(highLightColor);
    }

    private boolean hasLabel(JPanel panel) {
        Component[] components = panel.getComponents();
    
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                return true;
            }
        }
        return false;
    }
    
    private Component getLabel(JPanel panel)
    {
        if(hasLabel(panel))
        {
            Component[] components = panel.getComponents();
    
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    return comp;
                }
            }
        }
        return null;
    }

    public void redrawBoard()
    {
        board.removeAll();
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
    public static void main(String[] args) {
        Board game = new Board();
        BoardUI board = new BoardUI(800,800,30, game);
    }
}
