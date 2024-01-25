package gora;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class CrazyHighway extends Application {


    public final static int GAME_WIDTH = 450;
    public final static int GAME_HEIGHT = 800;


    public static final int TITLE_SCREEN = 0;
    public static final int PLAYING = 1;
    public static final int GAME_OVER = 2;

    private int gameState = TITLE_SCREEN;


    private final HashSet<KeyCode> keyboard = new HashSet<KeyCode>();

    private GameTimer gameTimer = new GameTimer(time -> updateGame(time));

    private Sprite[] background = {
            new Sprite(new Image("file:resources/gora/background.png", GAME_WIDTH, 0, true, true), GAME_WIDTH, GAME_HEIGHT),
            new Sprite(new Image("file:resources/gora/background.png", GAME_WIDTH, 0, true, true), GAME_WIDTH, GAME_HEIGHT),
    };
    private Group backgroundDisplay = new Group(background[0], background[1]);

    private Driver player = new Driver(new Image("file:resources/gora/plane.png"));

    private Group otherCars = new Group();
    private double newCarTimer = 1;

    private Group bullets = new Group();
    private double gunCoolDown = 0.1;


    private Text title = new Text("");
    private Text score = new Text("Score: ");
    private Text subtitle = new Text("");

    private Text endTitle = new Text("game\nOver");
    private Text endSubtitle = new Text("Press Space");

    private Sprite startBackground = new Sprite(new Image("file:resources/gora/homescreen.png", GAME_WIDTH, 0, true, true), GAME_WIDTH, GAME_HEIGHT);
    private Sprite gameOverSprite = new Sprite(new Image("file:resources/gora/gameOver.png", GAME_WIDTH, 0, true, true), 100, 100);
    private Sprite healthBar = new Sprite(new Image("file:resources/gora/health10.png", GAME_WIDTH, 0, true, true), 80, 20);
    private int totalScore = 0;
    private Group[] gameScreens = {
            new Group(startBackground, title, subtitle),
            new Group(backgroundDisplay, player, otherCars, bullets, healthBar, score),
            new Group(gameOverSprite, endSubtitle)
    };


    public CrazyHighway() {
        newGame();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene gameScene = new Scene(root, GAME_WIDTH, GAME_HEIGHT, Color.DARKGRAY);
        primaryStage.setScene(gameScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("1942!");
        primaryStage.show();

        gameScene.setOnKeyPressed(key -> keyPressed(key));
        gameScene.setOnKeyReleased(key -> keyReleased(key));
        root.getChildren().addAll(gameScreens[PLAYING], gameScreens[TITLE_SCREEN], gameScreens[GAME_OVER]);
        gameScreens[TITLE_SCREEN].setVisible(true);
        gameScreens[PLAYING].setVisible(true);
        gameScreens[GAME_OVER].setVisible(false);

        var customFont = Font.loadFont(new FileInputStream("./resources/gora/ArcadeClassic.ttf"), 50);
        //Styling for the Text on Game Screens
        title.setFont(Font.font("Harlow Solid Italic", 50));
        title.setFill(Color.RED);
        title.setEffect(new DropShadow());
        title.setStroke(Color.WHITE);
        title.setStrokeWidth(2);
        title.setLayoutX(GAME_WIDTH / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setLayoutY(300);

        subtitle.setFont(Font.font("Harlow Solid Italic", 30));
        subtitle.setFill(Color.RED);
        subtitle.setEffect(new DropShadow());
        subtitle.setStroke(Color.WHITE);
        subtitle.setStrokeWidth(2);
        subtitle.setLayoutX(GAME_WIDTH / 2 - subtitle.getLayoutBounds().getWidth() / 2);
        subtitle.setLayoutY(GAME_HEIGHT / 2 + 200);

        endTitle.setFont(Font.font("Harlow Solid Italic", 85));
        endTitle.setFill(Color.RED);
        endTitle.setEffect(new DropShadow());
        endTitle.setStroke(Color.WHITE);
        endTitle.setStrokeWidth(3);
        endTitle.setTextAlignment(TextAlignment.CENTER);
        endTitle.setLayoutX(GAME_WIDTH / 2 - endTitle.getLayoutBounds().getWidth() / 2);
        endTitle.setLayoutY(GAME_HEIGHT / 2 - endTitle.getLayoutBounds().getWidth() / 2);

        endSubtitle.setFont(Font.font("Harlow Solid Italic", 30));
        endSubtitle.setFill(Color.RED);
        endSubtitle.setEffect(new DropShadow());
        endSubtitle.setStroke(Color.WHITE);
        endSubtitle.setStrokeWidth(2);
        endSubtitle.setLayoutX(GAME_WIDTH / 2 - endSubtitle.getLayoutBounds().getWidth() / 2);
        endSubtitle.setLayoutY(GAME_HEIGHT / 2 + 200);

        gameOverSprite.setLayoutX(GAME_WIDTH / 2 - endTitle.getLayoutBounds().getWidth() / 2 + 70);
        gameOverSprite.setLayoutY(GAME_HEIGHT / 2 - endTitle.getLayoutBounds().getWidth() / 2);

        healthBar.setLayoutX(5);
        healthBar.setLayoutY(GAME_HEIGHT - 30);

        score.setFont(customFont);
        score.setFill(Color.RED);
        score.setEffect(new DropShadow());
        score.setStroke(Color.WHITE);
        score.setStrokeWidth(2);
        score.setLayoutX(GAME_WIDTH / 2 - score.getLayoutBounds().getWidth() / 2);
        score.setLayoutY(50);


    }

    public void newGame() {

        background[0].setPositionY(0);
        background[1].setPositionY(-background[1].getHeight());
        background[0].setVelocityY(200);
        background[1].setVelocityY(200);

        player.setPosition(GAME_WIDTH / 2 - player.getWidth() / 2, GAME_HEIGHT - 2 * player.getHeight());
        player.setVelocity(0, 0);
        player.revive();

        otherCars.getChildren().clear();
        newCarTimer = 1;

        bullets.getChildren().clear();
        gunCoolDown = 0.1;

        updateHealth();
        updateScore();
        totalScore = 0;
    }


    public void updateGame(double elapsedTime) {
        updateBackground(elapsedTime);
        if (gameState == PLAYING) updatePlayer(elapsedTime);
        updateOtherCars(elapsedTime);
        updateBullets(elapsedTime);
        if (gameState == PLAYING) checkBulletCollisions();
        if (gameState == PLAYING) checkPlayerCollisions();

        cleanUp();

        if (gameState == GAME_OVER) {
            if (otherCars.getChildren().size() == 0) {
                backtoTitleScreen();
            }
        }
    }


    public void checkPlayerCollisions() {
        for (int j = 0; j < otherCars.getChildren().size(); j++) {
            OtherCar car = (OtherCar) otherCars.getChildren().get(j);
            if (car.intersect(player)) {
                car.kill();
                car.setVelocityY(0);
                player.hit();
                updateHealth();
                if (player.isDead()) {
                    gameOver();
                }
            }
        }
    }

    private void updateScore() {
        score.setText(String.format("Score: %d", totalScore));
    }

    private void updateHealth() {
        int h = player.getHealth();
        String src = String.format("file:resources/gora/health%d.png", h);
        healthBar.setImage(new Image(src));
    }

    public void checkBulletCollisions() {
        for (int i = 0; i < bullets.getChildren().size(); i++) {
            Bullet b = (Bullet) bullets.getChildren().get(i);

            for (int j = 0; j < otherCars.getChildren().size(); j++) {
                OtherCar car = (OtherCar) otherCars.getChildren().get(j);

                if (car.intersect(b)) {
                    b.kill();
                    if (!car.isDead()) {
                        totalScore++;
                        updateScore();
                    }
                    car.kill();
                }
            }
        }
    }

    public void cleanUp() {
        //Clean up for cars that are ready to be deleted
        for (int i = 0; i < otherCars.getChildren().size(); i++) {
            OtherCar car = (OtherCar) otherCars.getChildren().get(i);
            if (car.isReadyForCleanUp()) {
                otherCars.getChildren().remove(car);
                i--;
            }
        }
        //clean up bullets that are ready to be deleted
        for (int i = 0; i < bullets.getChildren().size(); i++) {
            Bullet b = (Bullet) bullets.getChildren().get(i);
            if (b.isReadyForCleanUp()) {
                bullets.getChildren().remove(b);
                i--;
            }
        }

    }

    public void updateBullets(double elapsedTime) {
        for (int i = 0; i < bullets.getChildren().size(); i++) {
            Bullet b = (Bullet) bullets.getChildren().get(i);
            b.update(elapsedTime);

            if (b.getPositionY() < -b.getHeight()) {
                b.kill();
            }
        }
    }

    public void updateOtherCars(double elapsedTime) {
        //adds a new car
        if (gameState == PLAYING && newCarTimer < 0) {
            OtherCar newCar = new OtherCar();
            newCar.setPosition((335 - newCar.getWidth()) * Math.random() + 80, -newCar.getHeight());
            newCar.setVelocityY(200 * Math.random() + 300);
            otherCars.getChildren().add(newCar);

            newCarTimer = Math.random() * 5;
        } else {
            newCarTimer -= elapsedTime;
        }
        for (int i = 0; i < otherCars.getChildren().size(); i++) {
            OtherCar car = (OtherCar) otherCars.getChildren().get(i);
            car.update(elapsedTime);

            if (car.getPositionY() > GAME_HEIGHT) {
                car.kill();

            }
        }
    }


    public void updatePlayer(double elapsedTime) {
        if (keyboard.contains(KeyCode.LEFT)) {
            player.driveLeft(elapsedTime);
        }

        if (keyboard.contains(KeyCode.RIGHT)) {
            player.driveRight(elapsedTime);
        }
        if (keyboard.contains(KeyCode.UP)) {
            player.driveUp(elapsedTime);
        }
        if (keyboard.contains(KeyCode.DOWN)) {
            player.driveDown(elapsedTime);
        }

        if (gunCoolDown < 0) {
            if (keyboard.contains(KeyCode.SPACE)) {
                bullets.getChildren().add(player.shoot());
            }
            gunCoolDown = 0.1;
        } else {
            gunCoolDown -= elapsedTime;
        }
    }

    public void updateBackground(double elapsedTime) {
        background[0].update(elapsedTime);
        background[1].update(elapsedTime);

        if (background[0].getPositionY() > GAME_HEIGHT) {
            background[0].setPositionY(background[1].getPositionY() - background[0].getHeight());

        }
        if (background[1].getPositionY() > GAME_HEIGHT) {
            background[1].setPositionY(background[0].getPositionY() - background[1].getHeight());
        }
    }


    public void keyPressed(KeyEvent key) {
        if (gameState == PLAYING) {
            if (!keyboard.contains(KeyCode.P)) {
                if (key.getCode() == KeyCode.P) {
                    pause();
                }
            }
        }

        if (gameState == TITLE_SCREEN) {
            if (!keyboard.contains(KeyCode.SPACE)) {
                if (key.getCode() == KeyCode.SPACE) {
                    startGame();
                }
            }
        }

        if (gameState == GAME_OVER) {
            if (!keyboard.contains(KeyCode.SPACE)) {
                if (key.getCode() == KeyCode.SPACE) {
                    backtoTitleScreen();
                }
            }
        }

        if (!keyboard.contains(KeyCode.ESCAPE)) {
            if (key.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        }

        //record this particular key has been pressed:
        keyboard.add(key.getCode());
    }


    public void keyReleased(KeyEvent key) {
        //remove the record of the key being pressed:
        keyboard.remove(key.getCode());
    }


    public void startGame() {
        gameScreens[TITLE_SCREEN].setVisible(false);
        gameScreens[GAME_OVER].setVisible(false);
        gameState = PLAYING;
        gameTimer.start();
        updateScore();
    }


    public void gameOver() {
        gameTimer.stop();
        gameScreens[TITLE_SCREEN].setVisible(false);
        gameScreens[GAME_OVER].setVisible(true);
        gameState = GAME_OVER;
    }

    public void backtoTitleScreen() {
        gameScreens[TITLE_SCREEN].setVisible(true);
        gameScreens[GAME_OVER].setVisible(false);
        gameState = TITLE_SCREEN;
        gameTimer.stop();
        newGame();

    }


    public void pause() {
        if (gameTimer.isPaused()) {
            gameTimer.start();
        } else {
            gameTimer.stop();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}




