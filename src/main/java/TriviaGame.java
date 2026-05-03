import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TriviaGame extends Application {

    public static boolean darkMode = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Trivia Game");

        stage.setScene(SceneFactory.create(SceneType.GAME, stage));
        stage.show();
    }

    public static void applyLightMode(Scene scene) {
        scene.getRoot().setStyle("-fx-background-color: white; -fx-text-fill: black;");
    }

    public static void applyDarkMode(Scene scene) {
        scene.getRoot().setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
    }

    public static void applyCurrentTheme(Scene scene) {
        if (darkMode) {
            applyDarkMode(scene);
        } else {
            applyLightMode(scene);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}