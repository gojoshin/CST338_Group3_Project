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
import java.util.ArrayList;

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

    // stores which category was picked so we know what questions to load
    public static String selectedCategory = "Science";

    // keeps track of score
    static int score = 0;

    // keeps track of which question we are on
    static int currentIndex = 0;

    // makes sure the completed score is only saved once
    static boolean scoreSaved = false;

    public static void startNewGame(String category) {
        selectedCategory = category;
        resetGame();
    }

    private static void resetGame() {
        score = 0;
        currentIndex = 0;
        scoreSaved = false;
    }

    public static Scene build(Stage stage) {

        // get the questions for the selected category
        ArrayList<Questions> questionList = DatabaseManager.getInstance().getQuestions(selectedCategory);

        // if we went through all the questions, show the score
        if (currentIndex >= questionList.size()) {
            if (SessionManager.isUserLoggedIn() && scoreSaved == false) {
                DatabaseManager.getInstance().saveQuizAttempt(
                        SessionManager.getCurrentUsername(),
                        selectedCategory,
                        score,
                        questionList.size()
                );
                scoreSaved = true;
            }

            Label doneLabel = new Label("Game Over!");
            doneLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            Label scoreLabel = new Label("You got " + score + " out of " + questionList.size() + " correct!");
            scoreLabel.setStyle("-fx-font-size: 18px;");

            Button backBtn = new Button("Back to Categories");
            backBtn.setOnAction(e -> {
                // reset for next game
                resetGame();
                stage.setScene(SceneFactory.create(SceneType.CATEGORY_SELECTION, stage));
            });

            Button leaderboardBtn = new Button("View Leaderboard");
            leaderboardBtn.setOnAction(e -> {
                resetGame();
                stage.setScene(SceneFactory.create(SceneType.LEADERBOARD, stage));
            });

            VBox endRoot = new VBox(15, doneLabel, scoreLabel, leaderboardBtn, backBtn);
            endRoot.setPadding(new Insets(30));
            endRoot.setAlignment(Pos.CENTER);

            return new Scene(endRoot, 600, 400);
        }

        // grab the current question
        Questions question = questionList.get(currentIndex);

        // make a timer that gives 15 seconds per question
        Timer timer = new Timer(15);

        // label that shows the timer counting down
        Label timerLabel = new Label("Time: " + timer.getTimeLeft());
        timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // label that shows which question number we are on
        Label questionNumLabel = new Label("Question " + (currentIndex + 1) + " of " + questionList.size());
        questionNumLabel.setStyle("-fx-font-size: 14px;");

        // label that shows the question
        Label questionLabel = new Label(question.getQuestion());
        questionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        questionLabel.setWrapText(true);

        // label that shows correct or wrong feedback after you pick an answer
        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 16px;");

        // label that shows the score
        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 14px;");

        // the 4 answer buttons
        Button btnA = new Button(question.getChoiceA());
        Button btnB = new Button(question.getChoiceB());
        Button btnC = new Button(question.getChoiceC());
        Button btnD = new Button(question.getChoiceD());

        // Width all at the same size.
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

                // wait 3 seconds then go to next question
                Timeline pause = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                    currentIndex++;
                    stage.setScene(SceneFactory.create(SceneType.GAME, stage));
                }));
                pause.setCycleCount(1);
                pause.play();
            }
        }));

        // keeps it going until we stop it ourselves
        countdownHolder[0].setCycleCount(Timeline.INDEFINITE);
        countdownHolder[0].play();

        // set up what happens when you click each answer button
        for (int i = 0; i < allButtons.length; i++) {
            Button currentBtn = allButtons[i];

            currentBtn.setOnAction(e -> {
                // stop the timer
                countdownHolder[0].stop();

                // check if the answer is right
                String picked = currentBtn.getText();
                if (question.isCorrect(picked) == true) {
                    score++;
                    feedbackLabel.setText("Correct!");
                    feedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
                } else {
                    feedbackLabel.setText("Wrong! The answer was: " + question.getCorrectAnswer());
                    feedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
                }

                // update score label
                scoreLabel.setText("Score: " + score);

                // turn off all buttons after picking
                for (int j = 0; j < allButtons.length; j++) {
                    allButtons[j].setDisable(true);
                }

                // wait 2 seconds then go to next question
                Timeline pause = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                    currentIndex++;
                    stage.setScene(SceneFactory.create(SceneType.GAME, stage));
                }));
                pause.setCycleCount(1);
                pause.play();
            });
        }

        // if they click back, stop the timer and go to categories
        backBtn.setOnAction(e -> {
            countdownHolder[0].stop();
            resetGame();
            stage.setScene(SceneFactory.create(SceneType.CATEGORY_SELECTION, stage));
        });

        // put everything in a VBox
        VBox root = new VBox(15, timerLabel, questionNumLabel, scoreLabel, questionLabel,
                topRow, bottomRow, feedbackLabel, backBtn);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        return new Scene(root, 600, 400);
    }
}
