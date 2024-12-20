import java.awt.*;

public class Pacman {
    private int x, y; // Position de Pacman
    private int dx, dy; // Direction de déplacement

    public Pacman(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.dx = 0;
        this.dy = 0;
    }

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void move(Maze maze, int tileSize) {
        int newX = x + dx * tileSize;
        int newY = y + dy * tileSize;

        // Vérifier si la prochaine tuile est valide (pas un mur)
        if (maze.isWalkable(newX / tileSize, newY / tileSize)) {
            x = newX;
            y = newY;
        }
    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.dx = 0;
        this.dy = 0;
    }

    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, tileSize, tileSize);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

