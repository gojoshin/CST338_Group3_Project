import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * GameScene.java
 * This is the screen where you actually play the trivia game
 * It shows the question, 4 buttons to pick from, and a timer
 *
 * @author Jayson Jauregui
 * @version 1.0
 * @since 04/29/2026
 */
public class GameScene {

    public static Scene build(Stage stage) {

        // make a timer that gives 15 seconds per question
        Timer timer = new Timer(15);

        // placeholder question - we will swap this with the database later
        Questions question = new Questions(
                "What is the capital of Japan?",
                "Seoul",
                "Tokyo",
                "Beijing",
                "Bangkok",
                "Tokyo"
        );

        // label that shows the timer counting down
        Label timerLabel = new Label("Time: " + timer.getTimeLeft());
        timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // label that shows the question
        Label questionLabel = new Label(question.getQuestion());
        questionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        questionLabel.setWrapText(true);

        // label that shows correct or wrong after you pick
        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 16px;");

        // make the 4 answer buttons
        Button btnA = new Button(question.getChoiceA());
        Button btnB = new Button(question.getChoiceB());
        Button btnC = new Button(question.getChoiceC());
        Button btnD = new Button(question.getChoiceD());

        // set the width so they all look the same size
        btnA.setPrefWidth(200);
        btnB.setPrefWidth(200);
        btnC.setPrefWidth(200);
        btnD.setPrefWidth(200);

        // put A and B in one row
        HBox topRow = new HBox(10, btnA, btnB);
        topRow.setAlignment(Pos.CENTER);

        // put C and D in another row
        HBox bottomRow = new HBox(10, btnC, btnD);
        bottomRow.setAlignment(Pos.CENTER);

        // array so I can loop through all of them later
        Button[] allButtons = {btnA, btnB, btnC, btnD};

        // button to go back to categories
        Button backBtn = new Button("Back to Categories");

        // using an array so we can use countdown inside its own lambda
        // (java wont let you use a regular variable before its done being made)
        final Timeline[] countdownHolder = new Timeline[1];

        // this is the actual countdown timer that ticks every second
        countdownHolder[0] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

            // subtract one second
            timer.tick();

            // update the label
            timerLabel.setText("Time: " + timer.getTimeLeft());

            // check if time ran out
            if (timer.isTimeUp() == true) {
                // stop the timer
                countdownHolder[0].stop();

                // show that they ran out of time
                feedbackLabel.setText("Time's up! The answer was: " + question.getCorrectAnswer());
                feedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

                // turn off the buttons so they cant click anymore
                for (int i = 0; i < allButtons.length; i++) {
                    allButtons[i].setDisable(true);
                }
            }
        }));

        // keep it going until we stop it ourselves
        countdownHolder[0].setCycleCount(Timeline.INDEFINITE);
        countdownHolder[0].play();

        // set up what happens when you click each answer button
        for (int i = 0; i < allButtons.length; i++) {
            // need this because you cant use i inside a lambda directly
            Button currentBtn = allButtons[i];

            currentBtn.setOnAction(e -> {
                // stop the timer
                countdownHolder[0].stop();

                // check if the answer is right
                String picked = currentBtn.getText();
                if (question.isCorrect(picked) == true) {
                    feedbackLabel.setText("Correct!");
                    feedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
                } else {
                    feedbackLabel.setText("Wrong! The answer was: " + question.getCorrectAnswer());
                    feedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
                }

                // turn off all buttons after picking
                for (int j = 0; j < allButtons.length; j++) {
                    allButtons[j].setDisable(true);
                }
            });
        }

        // if they click back, stop the timer and go to categories
        backBtn.setOnAction(e -> {
            countdownHolder[0].stop();
            stage.setScene(SceneFactory.create(SceneType.CATEGORY_SELECTION, stage));
        });

        // put everything in a VBox
        VBox root = new VBox(15, timerLabel, questionLabel, topRow, bottomRow, feedbackLabel, backBtn);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        return new Scene(root, 600, 400);
    }
}