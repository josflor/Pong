package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class PongMultiPlayer {

    @FXML
    private Pane main_pane;

    @FXML
    private Button menu_button;

    @FXML
    private Button pause_button;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    private boolean isPaused;

    private Circle ball;
    private Rectangle paddle1;
    private Rectangle paddle2;

    private double ball_speed_x;
    private double ball_speed_y;

    private double paddle_speed1;
    private double paddle_speed2;

    private int player1;
    private int player2;

    private AnimationTimer game_loop;

    private Set<KeyCode> pressed_keys;

    public PongMultiPlayer() {
        pressed_keys = new HashSet<>();

        this.label1 = label1;
        this.label2 = label2;
    }


    public void onResetGame(ActionEvent event) {
        if (game_loop != null) {
            game_loop.stop();
            game_loop = null;
        }

        if (ball != null) {
            main_pane.getChildren().remove(ball);
            ball = null;
        }

        if (paddle1 != null) {
            main_pane.getChildren().remove(paddle1);
            paddle1 = null;
        }

        if (paddle2 != null) {
            main_pane.getChildren().remove(paddle2);
            paddle2 = null;
        }


        this.ball = new Circle();
        ball.setRadius(3);
        ball.setFill(Paint.valueOf("white"));
        ball.setStroke(Paint.valueOf("white"));
        ball.setCenterX(200);
        ball.setCenterY(200);
        ball_speed_x = 2;
        ball_speed_y = 2;

        main_pane.getChildren().add(ball);

        createPaddle1();
        createPaddle2();

        this.player1 = 0;
        this.player2 = 0;

        label1.setText("0");
        label2.setText("0");

        main_pane.requestFocus();

        this.game_loop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                getUserInput();
                moveAnimation();
            }
        };
        game_loop.start();
    }


    private void createPaddle1() {
        paddle1 = new Rectangle();
        paddle1.setWidth(4);
        paddle1.setHeight(80);
        paddle1.setFill(Paint.valueOf("white"));
        paddle1.setStroke(Paint.valueOf("white"));
        paddle1.setX(20);
        paddle1.setY((main_pane.getHeight() / 2) - paddle1.getHeight() + 45);

        main_pane.getChildren().add(paddle1);
    }


    private void createPaddle2() {
        paddle2 = new Rectangle();
        paddle2.setWidth(4);
        paddle2.setHeight(80);
        paddle2.setFill(Paint.valueOf("white"));
        paddle2.setStroke(Paint.valueOf("white"));
        paddle2.setX(main_pane.getWidth() - 20);
        paddle2.setY((main_pane.getHeight() / 2) - paddle2.getHeight() + 45);

        main_pane.getChildren().add(paddle2);
    }


    private void moveAnimation() {
        checkWallCollision();
        checkPaddleCollision1();
        checkPaddleCollision2();

        movePaddle1();
        movePaddle2();
        moveBall();
    }


    private void checkWallCollision() {
        double width = main_pane.getWidth();
        double height = main_pane.getHeight();

        double ball_x = ball.getCenterX();
        double ball_y = ball.getCenterY();

        if (ball_x < 0) {
            player2++;
            label2.setText(String.valueOf(player2));
            ball.setCenterX(200);
            ball_speed_x = ball_speed_x * -1;
        }

        if (ball_x > width) {
            player1++;
            label1.setText(String.valueOf(player1));
            ball.setCenterX(width - 200);
            ball_speed_x = ball_speed_x * -1;
        }

        if (ball_y < 0) {
            ball_speed_y = ball_speed_y * -1;
            ball.setCenterY(0);
        }

        if (ball_y > height) {
            ball_speed_y = ball_speed_y * -1;
            ball.setCenterY(height);
        }
    }


    private void checkPaddleCollision1() {
        Shape paddle_ball_overlap1 = Shape.intersect(ball, paddle1);
        Bounds collision = paddle_ball_overlap1.getBoundsInParent();

        if (collision.getHeight() > 0 && collision.getWidth() > 0) {

        double center_of_ball = ball.getCenterY();

        double half_of_paddle_height = paddle1.getHeight() / 2;
        double center_of_paddle = paddle1.getY() - half_of_paddle_height;

        double hit_distance_from_center = center_of_paddle - center_of_ball;

        double bounce_direction = hit_distance_from_center / half_of_paddle_height;

        double total_speed = Math.sqrt(Math.pow(ball_speed_x, 2) + Math.pow(ball_speed_y, 2));

        ball_speed_x = -1 * total_speed * Math.sin(bounce_direction);
        ball_speed_y = -1 * total_speed * Math.cos(bounce_direction);
        }
    }


    private void checkPaddleCollision2() {
        Shape paddle_ball_overlap2 = Shape.intersect(ball, paddle2);
        Bounds collision = paddle_ball_overlap2.getBoundsInParent();

        if (collision.getHeight() > 0 && collision.getWidth() > 0) {

            double center_of_ball = ball.getCenterY();

            double half_of_paddle_height = paddle2.getHeight() / 2;
            double center_of_paddle = paddle2.getY() + half_of_paddle_height;

            double hit_distance_from_center = center_of_paddle - center_of_ball;

            double bounce_direction = hit_distance_from_center / half_of_paddle_height;

            double total_speed = Math.sqrt(Math.pow(ball_speed_x, 2) + Math.pow(ball_speed_y, 2));

            ball_speed_y = -1 * total_speed * Math.sin(bounce_direction);
            ball_speed_x = -1 * total_speed * Math.cos(bounce_direction);
        }
    }


    private void movePaddle1() {
        double paddle_position1 = paddle1.getY() + paddle_speed1;

        paddle_position1 = Math.max(paddle_position1, 0);

        paddle_position1 = Math.min(paddle_position1 + paddle1.getHeight(), main_pane.getHeight());
        paddle_position1 = paddle_position1 - paddle1.getHeight();

        paddle1.setY(paddle_position1);
    }


    private void movePaddle2() {
        double paddle_position2 = paddle2.getY() + paddle_speed2;

        paddle_position2 = Math.max(paddle_position2, 0);

        paddle_position2 = Math.min(paddle_position2 + paddle2.getHeight(), main_pane.getHeight());
        paddle_position2 = paddle_position2 - paddle2.getHeight();

        paddle2.setY(paddle_position2);

        main_pane.requestFocus();
    }


    private void moveBall() {
        double x;
        double y;

        x = ball.getCenterX();
        y = ball.getCenterY();

        ball.setCenterX(x + ball_speed_x);
        ball.setCenterY(y + ball_speed_y);
    }


    private void getUserInput() {
        paddle_speed1 = 0;
        paddle_speed2 = 0;

        if (pressed_keys.contains(KeyCode.S)) {
            paddle_speed1 = 4;
        } else if (pressed_keys.contains(KeyCode.W)) {
            paddle_speed1 = -4;
        } else if (pressed_keys.contains(KeyCode.DOWN)) {
            paddle_speed2 = 4;
        } else if (pressed_keys.contains(KeyCode.UP)) {
            paddle_speed2 = -4;
        }
    }


    public void onLoad() {
        Scene main_scene;
        main_scene = main_pane.getScene();
        main_pane.requestFocus();

        main_scene.setOnKeyPressed((KeyEvent event) -> {
            pressed_keys.add(event.getCode());
        });

        main_scene.setOnKeyReleased((KeyEvent event) -> {
            pressed_keys.remove(event.getCode());
        });
    }


    private void setPaused(boolean make_paused) {
        if (make_paused) {
            game_loop.stop();
            isPaused = true;
            pause_button.setText("Unpause");
        } else {
            game_loop.start();
            isPaused = false;
            pause_button.setText("Pause");
        }
    }


    public void onTogglePause() {
        setPaused(!isPaused);
    }


    public Scene getScene(){
        return menu_button.getScene();
    }


    public void onMainMenu(Event event){
        Stage stage;
        stage = (Stage) getScene().getWindow();
        Menu getMenu = Main.getMenu();
        stage.setScene(getMenu.getScene());
    }
}


