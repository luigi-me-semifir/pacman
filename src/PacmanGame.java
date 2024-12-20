import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PacmanGame extends JPanel implements ActionListener, KeyListener {

    // Constantes pour la taille des tuiles et de l'écran
    private final int TILE_SIZE = 24;
    private final int NUM_TILES = 20;
    private final int SCREEN_SIZE = TILE_SIZE * NUM_TILES;

    // Variables du jeu
    private Timer timer; // Timer pour contrôler les mises à jour du jeu
    private int pacmanX, pacmanY, pacmanDX, pacmanDY; // Position et direction de Pacman
    private int score; // Score du joueur
    private boolean running; // État du jeu

    // Représentation du labyrinthe : 1 pour les murs, 0 pour les espaces vides
    private final int[][] maze = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    // Constructeur pour initialiser le panneau du jeu
    public PacmanGame() {
        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE)); // Définir la taille du panneau
        this.setBackground(Color.BLACK); // Définir la couleur de fond
        this.setFocusable(true); // Permettre au panneau de recevoir le focus pour les entrées clavier
        this.addKeyListener(this); // Ajouter un écouteur de touches pour les déplacements

        initGame(); // Initialiser les variables du jeu
        timer = new Timer(100, this); // Définir un timer pour la boucle de jeu (intervalle de 100ms)
        timer.start(); // Démarrer le timer
    }

    // Initialiser les variables du jeu
    private void initGame() {
        pacmanX = TILE_SIZE; // Position X de départ de Pacman
        pacmanY = TILE_SIZE; // Position Y de départ de Pacman
        pacmanDX = 0; // Direction horizontale initiale
        pacmanDY = 0; // Direction verticale initiale
        score = 0; // Score initial
        running = true; // Définir le jeu comme actif
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g); // Dessiner le labyrinthe
        drawPacman(g); // Dessiner Pacman
        drawScore(g); // Dessiner le score
    }

    // Dessiner le labyrinthe à l'écran
    private void drawMaze(Graphics g) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 1) { // Si la tuile est un mur
                    g.setColor(Color.BLUE); // Définir la couleur des murs
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Dessiner un mur
                }
            }
        }
    }

    // Dessiner Pacman comme un cercle jaune
    private void drawPacman(Graphics g) {
        g.setColor(Color.YELLOW); // Définir la couleur de Pacman
        g.fillOval(pacmanX, pacmanY, TILE_SIZE, TILE_SIZE); // Dessiner Pacman
    }

    // Dessiner le score actuel à l'écran
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE); // Définir la couleur du texte
        g.drawString("Score: " + score, 10, SCREEN_SIZE + 20); // Afficher le score sous le jeu
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            movePacman(); // Mettre à jour la position de Pacman
            checkCollision(); // Vérifier les collisions ou les conditions de fin de jeu
            repaint(); // Repeindre l'écran
        }
    }

    // Mettre à jour la position de Pacman en fonction de la direction
    private void movePacman() {
        int newX = pacmanX + pacmanDX * TILE_SIZE; // Calculer la nouvelle position X
        int newY = pacmanY + pacmanDY * TILE_SIZE; // Calculer la nouvelle position Y
        if (maze[newY / TILE_SIZE][newX / TILE_SIZE] != 1) { // Vérifier si la prochaine tuile n'est pas un mur
            pacmanX = newX; // Mettre à jour la position X
            pacmanY = newY; // Mettre à jour la position Y
        }
    }

    // Vérifier les collisions ou autres conditions de fin de jeu
    private void checkCollision() {
        // Actuellement vide - peut ajouter des conditions pour terminer le jeu ou mettre à jour le score
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Mettre à jour la direction de Pacman en fonction de la touche appuyée
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> { pacmanDX = 0; pacmanDY = -1; } // Déplacer vers le haut
            case KeyEvent.VK_DOWN -> { pacmanDX = 0; pacmanDY = 1; } // Déplacer vers le bas
            case KeyEvent.VK_LEFT -> { pacmanDX = -1; pacmanDY = 0; } // Déplacer vers la gauche
            case KeyEvent.VK_RIGHT -> { pacmanDX = 1; pacmanDY = 0; } // Déplacer vers la droite
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Aucune action nécessaire lors du relâchement des touches
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Aucune action nécessaire lors de la frappe des touches
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pacman Game"); // Créer la fenêtre du jeu
        PacmanGame game = new PacmanGame(); // Créer le panneau du jeu

        frame.add(game); // Ajouter le panneau du jeu à la fenêtre
        frame.pack(); // Ajuster la taille de la fenêtre pour s'adapter au panneau
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermer l'application à la fermeture de la fenêtre
        frame.setVisible(true); // Afficher la fenêtre
    }
}
