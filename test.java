import javax.swing.*;
import java.awt.*;

public class test {
    
    public static void main( String[] args)
    {
        /* JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,500,500);
        ImageIcon image1 = new ImageIcon("./PieceSprites/new_knight_white.png");

        Image scaled1 = image1.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel piece = new JLabel(new ImageIcon(scaled1));
        piece.setOpaque(true);
        piece.setBounds(250,250,100,100);

        ImageIcon image2 = new ImageIcon("./PieceSprites/bombo.png");

        Image scaled2 = image2.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel bomb = new JLabel(new ImageIcon(scaled2));
        bomb.setOpaque(false);
        bomb.setBounds(200,200,50,50);
        

        JFrame frame = new JFrame("JlayeredPane");
        frame.add(layeredPane);

        layeredPane.add(piece, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(bomb, JLayeredPane.DRAG_LAYER); */

        int tileSize = 80;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(100,100));
        ImageIcon image1 = new ImageIcon("./PieceSprites/new_knight_white.png");

        Image scaled1 = image1.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        JLabel piece = new JLabel(new ImageIcon(scaled1));
        piece.setOpaque(true);
        piece.setBounds(0,0,tileSize,tileSize);

        ImageIcon image2 = new ImageIcon("./PieceSprites/bombo.png");

        Image scaled2 = image2.getImage().getScaledInstance(tileSize/2, tileSize/2, Image.SCALE_SMOOTH);
        JLabel bomb = new JLabel(new ImageIcon(scaled2));
        bomb.setOpaque(false);
        bomb.setBounds((int)(0.75 * tileSize),0,tileSize/2,tileSize/2);

        layeredPane.add(piece, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(bomb, JLayeredPane.DRAG_LAYER);

        JPanel sprite = new JPanel();
        sprite.add(layeredPane);

        JFrame frame = new JFrame("Test");
        frame.add(sprite);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        frame.setSize(new Dimension(500,500));
    }
}
