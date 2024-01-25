package gora;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends Group {

    @SuppressWarnings("unused")
    private Image image;
    private ImageView imgView = new ImageView();
    private double positionX = 0;
    private double positionY = 0;
    private double velocityX = 0;
    private double velocityY = 0;
    private double width = 0;
    private double height = 0;

    public Sprite() {
        this.getChildren().add(imgView);
    }

    public Sprite(Image i) {
        image = i;
        width = (i != null) ? i.getWidth() : 0;
        height = (i != null) ? i.getHeight() : 0;
        imgView.setImage(i);
        this.getChildren().add(imgView);
    }

    public Sprite(Image i, int w, int h) {
        this(i);
        setSize(w, h);
    }

    public void setImage(Image i) {
        image = i;
        width = (i != null) ? i.getWidth() : 0;
        height = (i != null) ? i.getHeight() : 0;
        imgView.setImage(i);
    }

    public void setSize(int w, int h) {
        imgView.setFitWidth(w);
        imgView.setFitHeight(h);
    }

    public double getPositionX() {
        return positionX;
    }


    public void setPositionX(double positionX) {
        this.positionX = positionX;
        this.setLayoutX(positionX);
    }


    public double getPositionY() {
        return positionY;
    }


    public void setPositionY(double positionY) {
        this.positionY = positionY;
        this.setLayoutY(positionY);
    }


    public double getVelocityX() {
        return velocityX;
    }


    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }


    public double getVelocityY() {
        return velocityY;
    }


    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }


    public double getWidth() {
        return width;
    }


    public double getHeight() {
        return height;
    }


    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
        this.relocate(positionX, positionY);
    }


    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }


    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }


    public void update(double elapsedTime) {
        positionX = positionX + velocityX * elapsedTime;
        positionY = positionY + velocityY * elapsedTime;

        this.relocate(positionX, positionY);
    }


    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }


    public boolean intersect(Sprite s) {
        return this.getBoundary().intersects(s.getBoundary());
    }
}
             