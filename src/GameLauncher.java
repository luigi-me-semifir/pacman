import javax.swing.*;

public class GameLauncher {
    public static void launch() {
        JFrame frame = new JFrame("Pacman Game");
        PacmanGame game = new PacmanGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

