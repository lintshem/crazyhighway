package gora;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Driver extends Sprite {

    private static final Image EXPLOSION_PIC = new Image("file:resources/gora/explosion0.png");
    private ImageView explosion = new ImageView(EXPLOSION_PIC);
    private int health = 10;
    private boolean isDead = false;
    private boolean justHit = false;

    public Driver(Image i) {
        super(i);

        super.getChildren().add(explosion);
        explosion.setVisible(false);
        explosion.setLayoutX(10);
        explosion.setLayoutY(5);

    }

    public void driveLeft(double time) {
        setVelocityX(-250);
        update(time);
        setVelocityX(0);

        if (getPositionX() < 80) {
            setPositionX(80);
        }
    }

    public void driveRight(double time) {
        super.setVelocityX(250);
        super.update(time);
        super.setVelocityX(0);

        if (getPositionX() + getWidth() > 370) {
            setPositionX(370 - getWidth());
        }
    }

    public void driveUp(double time) {
        setVelocityY(-250);
        update(time);
        setVelocityY(0);

        if (getPositionY() < 80) {
            setPositionY(80);
        }
    }

    public void driveDown(double time) {
        setVelocityY(+250);
        update(time);
        setVelocityY(0);

        if (getPositionY() > 800) {
            setPositionY(800 - 80);
        }
    }

    public Bullet shoot() {
        Bullet b = new Bullet();
        b.setPositionX(getPositionX() + getWidth() / 2 - b.getWidth() / 2);
        b.setPositionY(getPositionY());
        b.setVelocityY(-500);
        return b;
    }

    public void hit() {
        if (!justHit) {
            health--;
            if (health <= 0) {
                health = 0;
                isDead = true;
                explosion.setVisible(true);
            }
            justHit = true;
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(2),
                    event -> {
                        // Your function or code to be executed after 1 second
                        justHit = false;
                    }
            ));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void revive() {
        isDead = false;
        explosion.setVisible(false);
        health = 10;
    }

    public int getHealth() {
        return health;
    }
}
