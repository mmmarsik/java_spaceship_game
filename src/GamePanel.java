import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    public Ship makaka;
    public Ship dog;
    public Timer timer;
    private Image backgroundImage;
    private List<Bullet> bullets;
    private List<Bullet> dogBullets;
    private Image bananaImage;
    private Image boneImage;
    private boolean initialized = false;
    private long lastMakakaShotTime = 0;
    private final long shotCooldown = 500;
    private long lastDogShotTime = 0;
    private final long dogShotCooldown = 500;

    public GamePanel() {
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();
        timer = new Timer(50, this);
        timer.start();

        bullets = new ArrayList<>();
        dogBullets = new ArrayList<>();

        try {
            Image makakaImage = ImageIO.read(new File("makaka.png"));
            makaka = new Ship(50, 50, 10, 0, makakaImage, null, 10);

            BufferedImage dogImage = ImageIO.read(new File("dog.png"));
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-dogImage.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            dogImage = op.filter(dogImage, null);

            dog = new Ship(0, 0, 10, 0, dogImage, null, 13);

            backgroundImage = ImageIO.read(new File("space.jpg"));
            bananaImage = ImageIO.read(new File("banana.png"));
            boneImage = ImageIO.read(new File("bone.png"));
        } catch (IOException e) {
            System.out.println("Ошибка загрузки изображения.");
            throw new RuntimeException(e);
        }
    }

    private boolean checkCollision(Bullet bullet, Ship ship) {
        int targetWidth = ship.image.getWidth(null) / 2;
        int targetHeight = ship.image.getHeight(null) / 2;

        if (ship == makaka) {
            targetWidth *= 0.6;
            targetHeight *= 2;
        }

        int offsetX = (ship.image.getWidth(null) - targetWidth) / 2;
        int offsetY = (ship.image.getHeight(null) - targetHeight) / 2;

        Rectangle bulletRect = new Rectangle(bullet.x, bullet.y, bullet.image.getWidth(null),
                bullet.image.getHeight(null));
        Rectangle shipRect = new Rectangle(ship.x + offsetX, ship.y + offsetY, targetWidth, targetHeight);

        return bulletRect.intersects(shipRect);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        requestFocusInWindow();
        if (!initialized) {
            int dogStartX = getWidth() - dog.image.getWidth(null) - 10;
            int dogStartY = getHeight() - dog.image.getHeight(null) - 10;
            dog.x = dogStartX;
            dog.y = dogStartY;
            initialized = true;
        }
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (makaka.isAlive()) {
            makaka.draw(g);
        }
        if (dog.isAlive()) {
            dog.draw(g);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for (Bullet bullet : dogBullets) {
            bullet.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (makaka.isAlive())
                    makaka.direction.up = true;
            }
            case KeyEvent.VK_DOWN -> {
                if (makaka.isAlive())
                    makaka.direction.down = true;
            }
            case KeyEvent.VK_RIGHT -> {
                if (makaka.isAlive())
                    makaka.direction.right = true;
            }
            case KeyEvent.VK_LEFT -> {
                if (makaka.isAlive())
                    makaka.direction.left = true;
            }
            case KeyEvent.VK_CONTROL -> {
                if (makaka.isAlive()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastMakakaShotTime >= shotCooldown) {
                        Bullet bullet = new Bullet(
                                makaka.x + makaka.image.getWidth(null) / 2,
                                makaka.y + makaka.image.getHeight(null) / 2,
                                10,
                                makaka.angle,
                                bananaImage,
                                2);
                        bullets.add(bullet);
                        lastMakakaShotTime = currentTime;
                    }
                }
            }
            case KeyEvent.VK_W -> {
                if (dog.isAlive())
                    dog.direction.down = true;
            }
            case KeyEvent.VK_S -> {
                if (dog.isAlive())
                    dog.direction.up = true;
            }
            case KeyEvent.VK_D -> {
                if (dog.isAlive())
                    dog.direction.right = true;
            }
            case KeyEvent.VK_A -> {
                if (dog.isAlive())
                    dog.direction.left = true;
            }
            case KeyEvent.VK_SPACE -> {
                if (dog.isAlive()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastDogShotTime >= dogShotCooldown) {
                        double invertedAngle = dog.angle + Math.PI;
                        Bullet bullet = new Bullet(
                                dog.x + dog.image.getWidth(null) / 2,
                                dog.y + dog.image.getHeight(null) / 2,
                                10,
                                invertedAngle,
                                boneImage,
                                1);
                        dogBullets.add(bullet);
                        lastDogShotTime = currentTime;
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> makaka.direction.up = false;
            case KeyEvent.VK_DOWN -> makaka.direction.down = false;
            case KeyEvent.VK_RIGHT -> makaka.direction.right = false;
            case KeyEvent.VK_LEFT -> makaka.direction.left = false;
            case KeyEvent.VK_W -> dog.direction.down = false;
            case KeyEvent.VK_S -> dog.direction.up = false;
            case KeyEvent.VK_D -> dog.direction.right = false;
            case KeyEvent.VK_A -> dog.direction.left = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (makaka.isAlive()) {
            makaka.move(getWidth(), getHeight());
        }
        if (dog.isAlive()) {
            dog.move(getWidth(), getHeight());
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();
            if (bullet.x < 0 || bullet.x > getWidth() || bullet.y < 0 || bullet.y > getHeight()) {
                bullets.remove(i);
                i--;
                continue;
            }
            if (dog.isAlive() && checkCollision(bullet, dog)) {
                dog.setHP(dog.getHP() - bullet.damage);
                System.out.println("Dog HP: " + dog.getHP());
                bullets.remove(i);
                i--;
                if (!dog.isAlive()) {
                    System.out.println("Dog has been destroyed!");
                }
            }
        }

        for (int i = 0; i < dogBullets.size(); i++) {
            Bullet bullet = dogBullets.get(i);
            bullet.move();
            if (bullet.x < 0 || bullet.x > getWidth() || bullet.y < 0 || bullet.y > getHeight()) {
                dogBullets.remove(i);
                i--;
                continue;
            }
            if (makaka.isAlive() && checkCollision(bullet, makaka)) {
                makaka.setHP(makaka.getHP() - bullet.damage);
                System.out.println("Makaka HP: " + makaka.getHP());
                dogBullets.remove(i);
                i--;
                if (!makaka.isAlive()) {
                    System.out.println("Makaka has been destroyed!");
                }
            }
        }

        repaint();
    }
}