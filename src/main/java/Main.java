import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main {
    public void start(Stage stage) {
        stage.setTitle("Trivia Game");

        stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
