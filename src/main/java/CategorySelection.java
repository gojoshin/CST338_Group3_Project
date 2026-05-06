import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * [Category Selection Scene]
 *
 * @author Ruth Ramirez
 * @version 0.3.0
 * @since 04/26/2026
 */
public class CategorySelection {

    public static boolean isValidCategory(String category) {
        if (category == null) return false;

        return category.equals("Science")
                || category.equals("History")
                || category.equals("Movies");
    }

    public static Scene build(Stage stage) {
        Label title = new Label("Choose a Category");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button scienceBtn = new Button("Science");
        Button historyBtn = new Button("History");
        Button moviesBtn = new Button("Movies");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(SceneFactory.create(SceneType.USER_DASHBOARD, stage)));

        VBox layout = new VBox(15, title, scienceBtn, historyBtn, moviesBtn, backBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.CATEGORY_SELECTION));
        layout.setAlignment(Pos.CENTER);

        scienceBtn.setOnAction(e -> {
            GameScene.startNewGame("Science");
            stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        });

        historyBtn.setOnAction(e -> {
            GameScene.startNewGame("History");
            stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        });

        moviesBtn.setOnAction(e -> {
            GameScene.startNewGame("Movies");
            stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        });

        return new Scene(layout, 600, 430);
    }
}
