import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageCategory {
    public static Scene build(Stage stage) {
        DatabaseManager db = DatabaseManager.getInstance();

        Label title = new Label("Add New Category");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label categoryLabel = new Label("Category Name:");
        TextField categoryField = new TextField();
        categoryField.setMaxWidth(250);

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        descriptionField.setMaxWidth(250);

        Label messageLabel = new Label();

        Button addCategoryBtn = new Button("Add Category");
        Button backBtn = new Button("Back");

        addCategoryBtn.setOnAction(e -> {
            boolean success = db.addCategory(
                    categoryField.getText(),
                    descriptionField.getText()
            );

            if (success) {
                messageLabel.setText("Category added.");
                categoryField.clear();
                descriptionField.clear();
            } else {
                messageLabel.setText("Category could not be added.");
            }
        });

        backBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.MANAGE_QUIZ, stage));
        });

        VBox root = new VBox(15, title, categoryLabel, categoryField,
                descriptionLabel, descriptionField, addCategoryBtn,
                messageLabel, backBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.MANAGE_CATEGORY)
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 500, 560);
    }
}