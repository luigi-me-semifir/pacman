import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pacman Game");
        // Créer l'instance du jeu
        // PacmanGame game = new PacmanGame();
        PacmangameGhost game = new PacmangameGhost();

        frame.add(game); // Ajouter le panneau du jeu
        frame.setSize(500, 500); // Taille de la fenêtre (modifiable selon les besoins)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Assurer la fermeture correcte
        frame.setVisible(true); // Afficher la fenêtre

    }
}