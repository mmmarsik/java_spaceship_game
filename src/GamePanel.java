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

    public GamePanel() {
        addKeyListener(this);
        requestFocusInWindow();
        timer = new Timer(50, this);
        timer.start();

        bullets = new ArrayList<>();
        dogBullets = new ArrayList<>();

        try {
            Image makakaImage = ImageIO.read(new File("makaka.png"));
            makaka = new Ship(50, 50, 10, 0, makakaImage, null, 1);

            BufferedImage dogImage = ImageIO.read(new File("dog.png"));
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-dogImage.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            dogImage = op.filter(dogImage, null);

            dog = new Ship(0, 0, 10, 0, dogImage, null, 1);

            backgroundImage = ImageIO.read(new File("space.jpg"));
            bananaImage = ImageIO.read(new File("banana.png"));
            boneImage = ImageIO.read(new File("bone.png"));
        } catch (IOException e) {
            System.out.println("Ошибка загрузки изображения.");
            throw new RuntimeException(e);
        }

        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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
        makaka.draw(g);
        dog.draw(g);
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
            case KeyEvent.VK_UP -> makaka.direction.up = true;
            case KeyEvent.VK_DOWN -> makaka.direction.down = true;
            case KeyEvent.VK_RIGHT -> makaka.direction.right = true;
            case KeyEvent.VK_LEFT -> makaka.direction.left = true;
            case KeyEvent.VK_CONTROL -> {
                Bullet bullet = new Bullet(makaka.x + makaka.image.getWidth(null) / 2, makaka.y + makaka.image.getHeight(null) / 2, 10, makaka.angle, bananaImage, 10);
                bullets.add(bullet);
            }
            case KeyEvent.VK_W -> dog.direction.up = true;
            case KeyEvent.VK_S -> dog.direction.down = true;
            case KeyEvent.VK_D -> dog.direction.right = true;
            case KeyEvent.VK_A -> dog.direction.left = true;
            case KeyEvent.VK_SPACE -> {
                double invertedAngle = dog.angle + Math.PI;
                Bullet bullet = new Bullet(dog.x + dog.image.getWidth(null) / 2, dog.y + dog.image.getHeight(null) / 2, 10, invertedAngle, boneImage, 10);
                dogBullets.add(bullet);
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
            case KeyEvent.VK_W -> dog.direction.up = false;
            case KeyEvent.VK_S -> dog.direction.down = false;
            case KeyEvent.VK_D -> dog.direction.right = false;
            case KeyEvent.VK_A -> dog.direction.left = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        makaka.move(getWidth(), getHeight());
        dog.move(getWidth(), getHeight());
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();
            if (bullet.x < 0 || bullet.x > getWidth() || bullet.y < 0 || bullet.y > getHeight()) {
                bullets.remove(i);
                i--;
            }
        }
        for (int i = 0; i < dogBullets.size(); i++) {
            Bullet bullet = dogBullets.get(i);
            bullet.move();
            if (bullet.x < 0 || bullet.x > getWidth() || bullet.y < 0 || bullet.y > getHeight()) {
                dogBullets.remove(i);
                i--;
            }
        }
        repaint();
    }
}