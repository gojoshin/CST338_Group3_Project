/*
 * [Leaderboard Scene]
 *
 * @author Ruth Ramirez
 * @version 0.3.0
 * @since 05/02/2026
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class LeaderboardScene {

    public static Scene build(Stage stage) {
        boolean isAdmin = SessionManager.isAdminLoggedIn();

        Label title = new Label(isAdmin ? "Leaderboard" : "Your Leaderboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        List<DatabaseManager.QuizAttempt> scores = isAdmin
                ? DatabaseManager.getInstance().getAllQuizAttempts()
                : DatabaseManager.getInstance()
                        .getQuizAttemptsForUser(SessionManager.getCurrentUsername());

        // layout for leaderboard rows
        VBox scoreList = new VBox(8);
        scoreList.setAlignment(Pos.CENTER);

        if (scores.isEmpty()) {
            Label emptyLabel = new Label("No scores yet.");
            emptyLabel.setStyle("-fx-font-size: 16px;");
            scoreList.getChildren().add(emptyLabel);
        } else {
            int rank = 1;
            for (DatabaseManager.QuizAttempt entry : scores) {
                Label row = new Label(rank + ". " + entry.getUsername()
                        + " | " + entry.getCategoryName()
                        + " | " + entry.getScoreText());
                row.setStyle("-fx-font-size: 16px;");
                scoreList.getChildren().add(row);
                rank++;
            }
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e ->
                stage.setScene(SceneFactory.create(
                        isAdmin ? SceneType.ADMIN_DASHBOARD : SceneType.USER_DASHBOARD,
                        stage
                ))
        );

        VBox layout = new VBox(15, title, scoreList, backBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.LEADERBOARD));
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 600, 400);
    }
}
