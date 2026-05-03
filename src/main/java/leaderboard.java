

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

public class leaderboard {
    public static Scene build(Stage stage) {
        Label title = new Label("Leaderboard");

        Label p1 = new Label("1. User1 — 96 pts");
        Label p2 = new Label("2. User2 — 87 pts");
        Label p3 = new Label("3. User3 — 80 pts");
        Label p4 = new Label("4. User4 — 75 pts");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e ->
                stage.setScene(SceneFactory.create(SceneType.USER_DASHBOARD, stage))
        );
        VBox layout = new VBox(15, title, p1, p2, p3, p4, backBtn);
        layout.setAlignment(Pos.CENTER);
        return new Scene(layout, 600, 400);
    }
}

