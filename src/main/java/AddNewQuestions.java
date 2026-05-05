import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddNewQuestions {

    public static Scene build(Stage stage) {
        DatabaseManager db = DatabaseManager.getInstance();

        Label title = new Label("Add New Question");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label categoryLabel = new Label("Choose a category: ");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setMaxWidth(300);
        categoryBox.getItems().setAll(db.getCategoryNames());

        Label questionLabel = new Label("Question: ");
        TextField questionField = new TextField();
        questionField.setMaxWidth(350);

        Label optionALabel = new Label("Option A: ");
        TextField optionAField = new TextField();
        optionAField.setMaxWidth(350);

        Label optionBLabel = new Label("Option B: ");
        TextField optionBField = new TextField();
        optionBField.setMaxWidth(350);

        Label optionCLabel = new Label("Option C: ");
        TextField optionCField = new TextField();
        optionCField.setMaxWidth(350);

        Label optionDLabel = new Label("Option D: ");
        TextField optionDField = new TextField();
        optionDField.setMaxWidth(350);

        Label correctLabel = new Label("Correct Answer: ");
        TextField correctField = new TextField();
        correctField.setMaxWidth(350);

        Button saveBtn = new Button("Save Question");
        Button cancelBtn = new Button("Cancel");

        Label messageLabel = new Label();

        saveBtn.setOnAction(e -> {
            String category = categoryBox.getValue();

            if (category == null) {
                messageLabel.setText("Choose a category.");
                return;
            }

            boolean success = db.addQuestion(category, questionField.getText(),
                    optionAField.getText(), optionBField.getText(), optionCField.getText(),
                    optionDField.getText(), correctField.getText());

            if (success) {
                stage.setScene(SceneFactory.create(SceneType.MANAGE_QUESTIONS, stage));
            } else {
                messageLabel.setText("Question could not be added.");
            }
        });

        cancelBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.MANAGE_QUESTIONS, stage));
        });

        HBox category = new HBox(8, categoryLabel, categoryBox);
        HBox question = new HBox(8, questionLabel, questionField);
        HBox optionA = new HBox(8, optionALabel, optionAField);
        HBox optionB = new HBox(8, optionBLabel, optionBField);
        HBox optionC = new HBox(8, optionCLabel, optionCField);
        HBox optionD = new HBox(8, optionDLabel, optionDField);
        HBox correctAns = new HBox(8, correctLabel, correctField);

        VBox root = new VBox(10, title, category, question, optionA, optionB, optionC, optionD, correctAns, saveBtn, cancelBtn, messageLabel, ThemeManager.createDarkModeToggle(stage, SceneType.ADD_QUESTIONS));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(16));

        return new Scene(root, 650, 650);
    }
}