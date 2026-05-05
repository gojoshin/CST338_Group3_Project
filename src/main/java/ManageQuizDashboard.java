import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageQuizDashboard {
    public static Scene build(Stage stage) {
        Label title = new Label("Manage Quizzes");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button addCategoryBtn = new Button("Add Category");
        addCategoryBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.MANAGE_CATEGORY, stage));
        });

        Button manageQueBtn = new Button("Manage Questions");
        manageQueBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.MANAGE_QUESTIONS, stage));
        });

        VBox root = new VBox(15, title, addCategoryBtn, manageQueBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.MANAGE_QUIZ));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 500, 560);
    }
}
