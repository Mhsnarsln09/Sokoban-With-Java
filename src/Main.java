import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {
    private final int OFFSET = 100;

    public Main() {

        initUI();
    }

    private void initUI() {

        Board board = new Board();
        add(board);

        setTitle("Muhsin ARSLAN Y230240067 Sokoban Game");

        setSize(board.getBoardWidth() + OFFSET,
                board.getBoardHeight() + 2 * OFFSET);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            Main game = new Main();
            game.setVisible(true);
        });
    }

}