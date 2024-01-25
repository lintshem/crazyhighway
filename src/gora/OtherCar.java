package gora;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OtherCar extends Sprite {
    List<Bullet> bullets = new ArrayList<>();
    double bulletCoolDown=0.5;
    public static final Image[] OTHER_CARS = {
            new Image("file:resources/gora/enemy0.png"),
            new Image("file:resources/gora/enemy1.png"),
            new Image("file:resources/gora/enemy2.png"),
            new Image("file:resources/gora/enemy3.png"),
    };

    private static final Image EXPLOSION_PIC = new Image("file:resources/gora/explosion0.png");
    private ImageView explosion = new ImageView(EXPLOSION_PIC);


    private double deathDelay = 0.3;
    public static final Random RNG = new Random();

    private int type = 0;

    private boolean isDead = false;

    public OtherCar() {
        super();

        type = RNG.nextInt(4);
        super.getChildren().add(explosion);
        explosion.setVisible(false);
        explosion.setLayoutX(10);
        explosion.setLayoutY(0);

        setImage(OTHER_CARS[type]);
    }

    public void kill() {
        isDead = true;
        explosion.setVisible(true);
        setVelocity(0, 200);
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isReadyForCleanUp() {
        return deathDelay < 0;
    }

    public void update(double time) {
        super.update(time);

        if (isDead) {
            deathDelay -= time;
        }
        if(bulletCoolDown<0) {
            shootBullet();
        }else{
            bulletCoolDown -= time;
        }

        for(Bullet b:bullets){
            b.update(time);
        }

    }
    private void shootBullet(){
        var b=new Bullet();
        b.setPositionX(getPositionX()+getWidth()/2);
        b.setPositionY(getPositionY()+getHeight());
        b.setVelocityY(300);
        bullets.add(b);
    }
}
