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

        Button scienceBtn = new Button("Science");
        scienceBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #ff7f50;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        Button historyBtn = new Button("History");
        historyBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #db7093;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        Button moviesBtn = new Button("Movies");
        moviesBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #9acd32;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

        backBtn.setOnAction(e -> stage.setScene(SceneFactory.create(SceneType.USER_DASHBOARD, stage)));

        VBox layout = new VBox(15, title, scienceBtn, historyBtn, moviesBtn, backBtn);
        layout.setAlignment(Pos.CENTER);

        scienceBtn.setOnAction(e -> {
            GameScene.selectedCategory = "Science";
            GameScene.score = 0;
            GameScene.currentIndex = 0;
            stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        });

        historyBtn.setOnAction(e -> {
            GameScene.selectedCategory = "History";
            GameScene.score = 0;
            GameScene.currentIndex = 0;
            stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        });

        moviesBtn.setOnAction(e -> {
            GameScene.selectedCategory = "Movies";
            GameScene.score = 0;
            GameScene.currentIndex = 0;
            stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        });

        return new Scene(layout, 600, 400);
    }
}