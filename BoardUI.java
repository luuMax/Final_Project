import javax.swing.*;
import java.awt.*;

public class BoardUI extends JFrame{
    
    private int windowWidth;
    private int windowLength;
    private int tileSize;

    public static final Color VERY_LIGHT_BROWN = new Color(254,228,187);
    public static final Color DARK_BROWN = new Color(205,154,117);



    //BoardUI will eventually need a reference to game, to update board visually and stuff
    public BoardUI(int windowW, int windowL, int tileS)
    {
        windowWidth = windowW;
        windowLength = windowL;
        tileSize = tileS;
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



        JPanel board = new JPanel(new GridLayout(8,8));
        board.setBounds(windowLength/2, windowWidth/2, tileSize * 8, tileSize*8);

        // Fill our board JPanel with white and gray tiles to represent a chess board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel();
                if ((i + j) % 2 == 0) {
                    square.setBackground(VERY_LIGHT_BROWN);
                    square.setPreferredSize(new Dimension(tileSize, tileSize));
                } else {
                    square.setBackground(DARK_BROWN);
                    square.setPreferredSize(new Dimension(tileSize, tileSize));
                }
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
    public static void main(String[] args) {
        BoardUI board = new BoardUI(600,600,20);
        board.initialize();
        Board imagineNamingYourSonBoard2 = new Board();

        
        
    }

}
