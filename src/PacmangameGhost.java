import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacmangameGhost extends JPanel implements ActionListener, KeyListener {

    // Constantes pour la taille des tuiles et de l'écran
    private final int TILE_SIZE = 24;
    private final int NUM_TILES = 20;
    private final int SCREEN_SIZE = TILE_SIZE * NUM_TILES;

    // Variables du jeu
    private Timer timer; // Timer pour contrôler les mises à jour du jeu
    private int pacmanX, pacmanY, pacmanDX, pacmanDY; // Position et direction de Pacman
    private int score; // Score du joueur
    private boolean running; // État du jeu

    private List<Ghost> ghosts; // Liste des fantômes
    private Random random; // Pour les mouvements aléatoires des fantômes

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
    public PacmangameGhost() {
        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE)); // Définir la taille du panneau
        this.setBackground(Color.BLACK); // Définir la couleur de fond
        this.setFocusable(true); // Permettre au panneau de recevoir le focus pour les entrées clavier
        this.addKeyListener(this); // Ajouter un écouteur de touches pour les déplacements

        random = new Random();
        ghosts = new ArrayList<>(); // Initialiser la liste des fantômes

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

        // Ajouter des fantômes uniquement dans des positions valides
        ghosts.clear();
        ghosts.add(createValidGhostPosition());
        ghosts.add(createValidGhostPosition());
    }

    // Générer une position valide pour un fantôme
    private Ghost createValidGhostPosition() {
        int ghostX, ghostY;
        do {
            ghostX = random.nextInt(NUM_TILES) * TILE_SIZE;
            ghostY = random.nextInt(maze.length) * TILE_SIZE;
        } while (maze[ghostY / TILE_SIZE][ghostX / TILE_SIZE] == 1); // Répéter jusqu'à trouver une case vide
        return new Ghost(ghostX, ghostY);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g); // Dessiner le labyrinthe
        drawPacman(g); // Dessiner Pacman
        drawGhosts(g); // Dessiner les fantômes
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

    // Dessiner les fantômes comme des cercles rouges
    private void drawGhosts(Graphics g) {
        g.setColor(Color.RED);
        for (Ghost ghost : ghosts) {
            g.fillOval(ghost.x, ghost.y, TILE_SIZE, TILE_SIZE);
        }
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
            moveGhosts(); // Déplacer les fantômes
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

    // Déplacer les fantômes de manière aléatoire
    private void moveGhosts() {
        for (Ghost ghost : ghosts) {
            int direction = random.nextInt(4); // Choisir une direction aléatoire
            int newX = ghost.x;
            int newY = ghost.y;

            switch (direction) {
                case 0 -> newY -= TILE_SIZE; // Haut
                case 1 -> newY += TILE_SIZE; // Bas
                case 2 -> newX -= TILE_SIZE; // Gauche
                case 3 -> newX += TILE_SIZE; // Droite
            }

            // Vérifier si la nouvelle position est dans les limites du labyrinthe
            if (newX >= 0 && newX < NUM_TILES * TILE_SIZE &&
                    newY >= 0 && newY < maze.length * TILE_SIZE &&
                    maze[newY / TILE_SIZE][newX / TILE_SIZE] != 1) {
                ghost.x = newX;
                ghost.y = newY;
            }
            System.out.println("Ghost Position: (" + newX + ", " + newY + ")");
            System.out.println("Maze Index: (" + (newY / TILE_SIZE) + ", " + (newX / TILE_SIZE) + ")");

        }
    }

    // Vérifier les collisions ou autres conditions de fin de jeu
    private void checkCollision() {
        for (Ghost ghost : ghosts) {
            if (pacmanX == ghost.x && pacmanY == ghost.y) {
                running = false; // Arrêter le jeu si Pacman est attrapé
                JOptionPane.showMessageDialog(this, "Game Over!", "Pacman", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
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

    // Classe interne pour représenter un fantôme
    private static class Ghost {
        int x, y; // Position du fantôme

        public Ghost(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

