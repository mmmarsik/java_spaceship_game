import java.awt.*;

public abstract class Unit implements Moving, Drawing {
    public int x = 0;
    public int y = 0;
    public int speed = 0;
    public double angle = 0;
    public Image image;
    public Direction direction;

    public Unit(int x, int y, int speed, double angle, Image image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
        this.image = image;
        direction = new Direction();
    }
}
