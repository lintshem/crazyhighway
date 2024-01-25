package gora;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
	
	private long previousTime = -1;
	private GameTick myHandler;

	
	public GameTimer() { }
	
	
	public GameTimer(GameTick handler) {
		myHandler = handler;		
	}
	
	
	public void setOnTick(GameTick handler) {
		myHandler = handler;
	}
	
	
	@Override
	public void handle(long currentTime) {
		//calculate the elapsed time
		double elapsedTime = (currentTime - previousTime) / 1000000000.0;
		//call the handling class' gameTick event
		if (myHandler != null) myHandler.gameTick(elapsedTime);		
		//save the current time
		previousTime = currentTime;
	}
	
	public void start() {
		previousTime = System.nanoTime();
		super.start();
	}
	
	
	public void stop() {
		previousTime = -1;
		super.stop();
	}
	
	
	public boolean isPaused() { return previousTime == -1; }
}
