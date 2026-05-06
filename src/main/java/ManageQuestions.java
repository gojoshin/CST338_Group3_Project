import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageQuestions {
    public static Scene build(Stage stage) {
        DatabaseManager db = DatabaseManager.getInstance();

        Label title = new Label("Manage Questions");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label questionsLabel = new Label("Questions");
        Label messageLabel = new Label();

        ObservableList<String> questionItems = FXCollections.observableArrayList(db.getQuestionsWithCategory());
        ListView<String> questionList = new ListView<>(questionItems);
        questionList.setMaxWidth(550);
        questionList.setMaxHeight(300);

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

            int questionId = getQuestionId(selected);
            if (questionId < 1) {
                messageLabel.setText("Question could not be deleted.");
                return;
            }

            if (db.deleteQuestionById(questionId)) {
                messageLabel.setText("Question deleted.");
                questionItems.setAll(db.getQuestionsWithCategory());
            } else {
                messageLabel.setText("Question could not be deleted.");
            }
        });

        backBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.MANAGE_QUIZ, stage));
        });

        HBox buttons = new HBox(8, addQuestionBtn, deleteQuestionBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));

        VBox root = new VBox(12, title, questionsLabel, questionList, messageLabel, buttons,
                ThemeManager.createDarkModeToggle(stage, SceneType.MANAGE_QUESTIONS));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        return new Scene(root, 650, 500);
    }

    private static int getQuestionId(String questionRow) {
        try {
            return Integer.parseInt(questionRow.split("\\|")[0].trim());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }
}
