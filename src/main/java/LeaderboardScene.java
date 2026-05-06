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

        Label title = new Label("Leaderboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // get top scores from database
        List<DatabaseManager.LeaderboardEntry> scores =
                DatabaseManager.getInstance().getTopScores();

        // layout that will hold all leaderboard rows
        VBox scoreList = new VBox(8);
        scoreList.setAlignment(Pos.CENTER);

        // loop through each score and add a label
        int rank = 1;
        for (DatabaseManager.LeaderboardEntry entry : scores) {
            Label row = new Label(rank + ". " + entry.getUsername() + " — " + entry.getScore() + " pts");
            row.setStyle("-fx-font-size: 16px;");
            scoreList.getChildren().add(row);
            rank++;
        }

        // back button
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e ->
                stage.setScene(SceneFactory.create(SceneType.USER_DASHBOARD, stage))
        );

        VBox layout = new VBox(15, title, scoreList, backBtn);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 600, 400);
    }
}

