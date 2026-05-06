import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddNewQuestions {
    public static Scene build(Stage stage) {
        DatabaseManager db = DatabaseManager.getInstance();

        Label title = new Label("Add New Question");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label categoryLabel = new Label("Choose a category:");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setMaxWidth(300);
        categoryBox.getItems().setAll(db.getCategoryNames());

        TextField questionField = createTextField();
        TextField optionAField = createTextField();
        TextField optionBField = createTextField();
        TextField optionCField = createTextField();
        TextField optionDField = createTextField();
        TextField correctField = createTextField();

        Button saveBtn = new Button("Save Question");
        Button cancelBtn = new Button("Cancel");
        Label messageLabel = new Label();

        saveBtn.setOnAction(e -> {
            String category = categoryBox.getValue();

            if (category == null) {
                messageLabel.setText("Choose a category.");
                return;
            }

            boolean success = db.addQuestion(
                    category,
                    questionField.getText(),
                    optionAField.getText(),
                    optionBField.getText(),
                    optionCField.getText(),
                    optionDField.getText(),
                    correctField.getText()
            );

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
        HBox question = new HBox(8, new Label("Question:"), questionField);
        HBox optionA = new HBox(8, new Label("Option A:"), optionAField);
        HBox optionB = new HBox(8, new Label("Option B:"), optionBField);
        HBox optionC = new HBox(8, new Label("Option C:"), optionCField);
        HBox optionD = new HBox(8, new Label("Option D:"), optionDField);
        HBox correctAns = new HBox(8, new Label("Correct Answer:"), correctField);
        HBox buttons = new HBox(8, saveBtn, cancelBtn);

        category.setAlignment(Pos.CENTER);
        question.setAlignment(Pos.CENTER);
        optionA.setAlignment(Pos.CENTER);
        optionB.setAlignment(Pos.CENTER);
        optionC.setAlignment(Pos.CENTER);
        optionD.setAlignment(Pos.CENTER);
        correctAns.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, title, category, question, optionA, optionB, optionC,
                optionD, correctAns, buttons, messageLabel,
                ThemeManager.createDarkModeToggle(stage, SceneType.ADD_QUESTIONS));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(16));

        return new Scene(root, 650, 650);
    }

    private static TextField createTextField() {
        TextField field = new TextField();
        field.setMaxWidth(350);
        return field;
    }
}
