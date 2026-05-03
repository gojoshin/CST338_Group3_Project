import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * [Admin Dashboard Scene]
 * @ author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/26/2026
 */

public class AdminDashboard {
    public static Scene build(Stage stage) {
        Label title = new Label("Welcome Admin!");
        title.setStyle("-fx-font-size: 26px;" +
                "-fx-font-weight: bold;");

        Button leaderboardBtn = new Button("LeaderBoard");
        leaderboardBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

//        todo will take to the leaderboard scene

        Button historyBtn = new Button("Quiz History");
        historyBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

//       TODO will take to the history scene

        Button manageQueBtn = new Button("Manage Quiz");
        manageQueBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
//        TODO manage quiz scene where admin can edit quiz

        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #e33437;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        logoutBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
        });

        VBox root = new VBox(15, title, leaderboardBtn, historyBtn, manageQueBtn, logoutBtn);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 400, 400);
    }
}
