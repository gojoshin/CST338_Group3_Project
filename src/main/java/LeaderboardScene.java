import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardScene {
    public static Scene build(Stage stage) {
        boolean isAdmin = SessionManager.isAdminLoggedIn();

        Label title = new Label(isAdmin ? "Leaderboard" : "Your Leaderboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label(isAdmin
                ? "All completed quiz scores"
                : "Scores from each completed trivia game");
        subtitle.setStyle("-fx-font-size: 14px;");

        TableView<DatabaseManager.QuizAttempt> leaderboardTable = new TableView<>();
        leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        leaderboardTable.setPrefWidth(560);
        leaderboardTable.setPrefHeight(300);
        leaderboardTable.setPlaceholder(new Label(isAdmin
                ? "No quiz attempts have been completed yet."
                : "Finish a trivia game to see your scores here."));

        TableColumn<DatabaseManager.QuizAttempt, Integer> attemptColumn = new TableColumn<>("Attempt");
        attemptColumn.setCellValueFactory(new PropertyValueFactory<>("attemptId"));
        attemptColumn.setPrefWidth(80);

        TableColumn<DatabaseManager.QuizAttempt, String> usernameColumn = new TableColumn<>("Player");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(120);

        TableColumn<DatabaseManager.QuizAttempt, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryColumn.setPrefWidth(120);

        TableColumn<DatabaseManager.QuizAttempt, String> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoreText"));
        scoreColumn.setPrefWidth(100);

        leaderboardTable.getColumns().add(attemptColumn);
        leaderboardTable.getColumns().add(usernameColumn);
        leaderboardTable.getColumns().add(categoryColumn);
        leaderboardTable.getColumns().add(scoreColumn);

        loadScores(leaderboardTable, isAdmin);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            SceneType backScene = isAdmin ? SceneType.ADMIN_DASHBOARD : SceneType.USER_DASHBOARD;
            stage.setScene(SceneFactory.create(backScene, stage));
        });

        VBox root = new VBox(15, title, subtitle, leaderboardTable, backBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.LEADERBOARD));
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 650, 520);
    }

    private static void loadScores(TableView<DatabaseManager.QuizAttempt> leaderboardTable,
                                   boolean isAdmin) {
        List<DatabaseManager.QuizAttempt> attempts;

        if (isAdmin) {
            attempts = DatabaseManager.getInstance().getAllQuizAttempts();
        } else {
            attempts = DatabaseManager.getInstance()
                    .getQuizAttemptsForUser(SessionManager.getCurrentUsername());
        }

        leaderboardTable.setItems(FXCollections.observableArrayList(attempts));
    }
}
