import java.awt.*;

public class Bullet extends Unit {
    public int damage;
    public Image image;

    public Bullet(int x, int y, int speed, double angle, Image image, int damage) {
        super(x, y, speed, angle, image);
        this.damage = damage;
        this.image = image;
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, 5, 5);
        }
    }

    @Override
    public void move() {
        x += (int) (Math.cos(angle) * speed);
        y += (int) (Math.sin(angle) * speed);
    }
}