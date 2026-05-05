import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageQuestions {

    public static Scene build(Stage stage) {
        DatabaseManager db = DatabaseManager.getInstance();

        Label title = new Label("Manage Questions");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label l = new Label("Questions");
        Label messageLabel = new Label();

        ObservableList<String> questionItems = FXCollections.observableArrayList();
        ListView<String> questionList = new ListView<>(questionItems);
        questionList.setMaxWidth(550);
        questionList.setMaxHeight(300);

        questionItems.setAll(db.getQuestionsWithCategory());

        Button addQuestionBtn = new Button("Add");
        Button deleteQuestionBtn = new Button("Delete");
        Button backBtn = new Button("Back");

        addQuestionBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.ADD_QUESTIONS, stage));
        });

        deleteQuestionBtn.setOnAction(e -> {
            String selected = questionList.getSelectionModel().getSelectedItem();

            if (selected == null) {
                messageLabel.setText("Select a question to delete.");
                return;
            }

            int questionId = Integer.parseInt(selected.split("\\|")[0].trim());

            boolean deleted = db.deleteQuestionById(questionId);

            if (deleted) {
                messageLabel.setText("Question deleted.");
                questionItems.setAll(db.getQuestionsWithCategory());
            } else {
                messageLabel.setText("Question could not be deleted.");
            }
        });

        backBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.MANAGE_QUIZ, stage));
        });

        HBox btns = new HBox(8, addQuestionBtn, deleteQuestionBtn, backBtn);

        VBox root = new VBox(12, title, l, questionList, messageLabel, btns, ThemeManager.createDarkModeToggle(stage, SceneType.MANAGE_QUESTIONS));

        root.setAlignment(Pos.CENTER);
        btns.setPadding(new Insets(30));

        return new Scene(root, 650, 500);
    }
}