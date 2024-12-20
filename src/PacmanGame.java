import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacmanGame extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 24;
    private final int NUM_TILES = 20;
    private final int SCREEN_SIZE = TILE_SIZE * NUM_TILES;

    private Timer timer; // Timer pour contrôler les mises à jour du jeu
    private Pacman pacman; // Instance de Pacman
    private int score; // Score du joueur
    private boolean running; // État du jeu

    private List<Ghost> ghosts; // Liste des fantômes
    private Random random; // Générateur de mouvements aléatoires pour les fantômes
    private Maze maze; // Instance du labyrinthe

    public PacmanGame() {
        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        random = new Random();
        ghosts = new ArrayList<>();
        maze = new Maze();
        pacman = new Pacman(TILE_SIZE, TILE_SIZE);

        initGame();
        timer = new Timer(100, this);
        timer.start();
    }

    // Initialiser les variables du jeu
    private void initGame() {
        pacman.resetPosition(TILE_SIZE, TILE_SIZE); // Position X et Y de départ de Pacman
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
            ghostX = random.nextInt(maze.getWidth()) * TILE_SIZE; // Générer une position X aléatoire
            ghostY = random.nextInt(maze.getHeight()) * TILE_SIZE; // Générer une position Y aléatoire
        } while (!maze.isWalkable(ghostX / TILE_SIZE, ghostY / TILE_SIZE)); // Vérifier si la case est accessible
        return new Ghost(ghostX, ghostY);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        maze.draw(g, TILE_SIZE); // Dessiner le labyrinthe
        pacman.draw(g, TILE_SIZE); // Dessiner Pacman
        drawGhosts(g); // Dessiner les fantômes
        drawScore(g); // Dessiner le score
    }

    private void drawGhosts(Graphics g) {
        g.setColor(Color.RED);
        for (Ghost ghost : ghosts) {
            g.fillOval(ghost.getX(), ghost.getY(), TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, SCREEN_SIZE + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            pacman.move(maze, TILE_SIZE);
            moveGhosts();
            checkCollision();
            repaint();
        }
    }

    private void moveGhosts() {
        for (Ghost ghost : ghosts) {
            int direction = random.nextInt(4);
            int newX = ghost.getX();
            int newY = ghost.getY();

            switch (direction) {
                case 0 -> newY -= TILE_SIZE; // Haut
                case 1 -> newY += TILE_SIZE; // Bas
                case 2 -> newX -= TILE_SIZE; // Gauche
                case 3 -> newX += TILE_SIZE; // Droite
            }

            if (maze.isWalkable(newX / TILE_SIZE, newY / TILE_SIZE)) {
                ghost.setPosition(newX, newY);
            }
        }
    }

    private void checkCollision() {
        for (Ghost ghost : ghosts) {
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
                running = false;
                JOptionPane.showMessageDialog(this, "Game Over!", "Pacman", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> pacman.setDirection(0, -1);
            case KeyEvent.VK_DOWN -> pacman.setDirection(0, 1);
            case KeyEvent.VK_LEFT -> pacman.setDirection(-1, 0);
            case KeyEvent.VK_RIGHT -> pacman.setDirection(1, 0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}

