import java.awt.*;
import java.util.Scanner;

public class Test {

    static int[] parseSquare(String square) {
        int col = square.charAt(0) - 'a'; // 'a'=0, 'b'=1, ... 'h'=7
        int row = 8 - (square.charAt(1) - '0'); // '1'=7, '8'=0
        return new int[]{row, col};
    }

    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);

        while (!game.checkGameOver()) {
            game.getBoard().printBoard();
            System.out.println(game.getCurrentTurn() + "'s turn");
            System.out.print("Enter move (e.g. e2 e4): ");

            int[] from = parseSquare(scanner.next());
            int[] to   = parseSquare(scanner.next());

            game.makeMove(from[0], from[1], to[0], to[1]);
        }

        game.getBoard().printBoard();
        System.out.println("Game over!");
        scanner.close();
    }
}