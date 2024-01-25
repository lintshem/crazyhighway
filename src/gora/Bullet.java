package gora;

import javafx.scene.image.Image;

public class Bullet extends Sprite {

	private static final Image BULLET_IMAGE = new Image("file:resources/gora/bullet.png");
	private boolean isDead = false;

	public Bullet() {
		super(BULLET_IMAGE);
	}

	public boolean isDead() {
		return isDead;
	}

	public boolean isReadyForCleanUp() {
		return isDead;
	}
	
	public void kill() {
		isDead = true;
	}
	public boolean intersect(Sprite s) {
		 if (isDead) {
			 return false;
		 }
		 else {
			 return super.intersect(s);
		 }
	}
}
