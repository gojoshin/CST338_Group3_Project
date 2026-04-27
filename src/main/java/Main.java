import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Barebones trivia game. Just a timer and feedback label for now.
 *
 * @author Jayson Jauregui
 * @version 0.1.0
 * @since 04/24/26
 */
public class Main extends Application {

  // window size
  private static final int SCENE_WIDTH = 500;
  private static final int SCENE_HEIGHT = 400;

  // how many seconds the player gets per question
  private static final int TIME_LIMIT = 15;

  // keeps track of how many seconds are left
  private int timeLeft;

  // the timer that ticks every second
  private Timeline timer;

  // label that shows the countdown on screen
  private Label timerLabel;

  // label that shows correct or incorrect
  private Label feedbackLabel;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {

    // TIMER LABEL
    timerLabel = new Label();
    timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    // FEEDBACK LABEL
    // shows "Correct!" or "Incorrect!" after an answer
    feedbackLabel = new Label();
    feedbackLabel.setStyle("-fx-font-size: 18px;");

    // LAYOUT
    VBox root = new VBox(15, timerLabel, feedbackLabel);
    root.setPadding(new Insets(30));
    root.setAlignment(Pos.CENTER);

    // start the timer
    startTimer();

    Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    stage.setTitle("Trivia Game");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Starts a countdown timer that counts down from 15
   * When it hits 0, it shows "Incorrect!" in the feedback label. (Can be something different for the case of running out)
   */
  public void startTimer() {
    timeLeft = TIME_LIMIT;
    timerLabel.setText("Time: " + timeLeft);

    // stop the old timer if one is already running
    if (timer != null) {
      timer.stop();
    }

    timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
      timeLeft--;
      timerLabel.setText("Time: " + timeLeft);

      if (timeLeft <= 0) {
        timer.stop();
        showFeedback(false);
      }
    }));

    // keeps looping until we call stop()
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
  }

  /**
   * Stops the timer and shows correct or incorrect.
   * Call this when the player picks an answer.
   *
   * @param correct true if they got it right, false if wrong or time ran out
   */
  public void showFeedback(boolean correct) {
    timer.stop();

    if (correct) {
      feedbackLabel.setText("Correct!");
      feedbackLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
    } else {
      feedbackLabel.setText("Incorrect!");
      feedbackLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");
    }
  }
}