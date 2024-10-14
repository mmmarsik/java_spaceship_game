import java.awt.*;

public class Ship extends Unit implements HealthPoint {
    public Weapon weapon;
    public int hp = 1;
    private int panelWidth;
    private int panelHeight;

    public Ship(int x, int y, int speed, double angle, Image image, Weapon weapon, int hp) {
        super(x, y, speed, angle, image);
        this.weapon = weapon;
        this.hp = hp;
    }

    public void setPanelSize(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    @Override
    public void move() {
        move(panelWidth, panelHeight);
    }

    public void move(int panelWidth, int panelHeight) {
        int dx = (int) (Math.cos(angle) * speed);
        int dy = (int) (Math.sin(angle) * speed);

        if (direction.down) {
            x -= dx;
            y -= dy;
        }
        if (direction.up) {
            x += dx;
            y += dy;
        }
        if (direction.left) {
            angle -= 0.1;
        }
        if (direction.right) {
            angle += 0.1;
        }

        x = Math.max(0, Math.min(x, panelWidth - image.getWidth(null)));
        y = Math.max(0, Math.min(y, panelHeight - image.getHeight(null)));
    }

    @Override
    public void draw(Graphics g) {
        System.out.println("Отрисовка корабля: x=" + x + ", y=" + y + ", angle=" + angle);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.rotate(angle, x + image.getWidth(null) / 2, y + image.getHeight(null) / 2);
        g.drawImage(image, x, y, null);
        graphics2D.rotate(-angle, x + image.getWidth(null) / 2, y + image.getHeight(null) / 2);
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public void setHP(int healthPoint) {
        hp = healthPoint;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }
}