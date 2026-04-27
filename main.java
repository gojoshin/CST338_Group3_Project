import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TriviaGame extends Application {

    private boolean darkMode = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Trivia Game");

        Button toggleButton = new Button("Switch to Dark Mode");

        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().add(toggleButton);

        Scene scene = new Scene(layout, 400, 300);


        applyLightMode(scene);

        toggleButton.setOnAction(e -> {
            darkMode = !darkMode;

            if (darkMode) {
                applyDarkMode(scene);
                toggleButton.setText("Switch to Light Mode");
            } else {
                applyLightMode(scene);
                toggleButton.setText("Switch to Dark Mode");
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    private void applyLightMode(Scene scene) {
        scene.getRoot().setStyle("-fx-background-color: white; -fx-text-fill: black;");
    }

    private void applyDarkMode(Scene scene) {
        scene.getRoot().setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
