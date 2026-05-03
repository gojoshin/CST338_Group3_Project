import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TriviaGame extends Application {

    public static boolean darkMode = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Trivia Game");

        stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
        stage.show();
    }

    private static void changeLabelStyle(Parent parent, String color) {
        for (Node node : parent.getChildrenUnmodifiable()) {

            if (node instanceof Label label) {
                label.setStyle("-fx-text-fill: " + color + ";" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;");
            }
        }
    }

    public static void applyDarkMode(Scene scene) {
        Parent root = scene.getRoot();
        root.setStyle("-fx-background-color: #2b2b2b;");
        changeLabelStyle(root, "white");
    }

    public static void applyLightMode(Scene scene) {
        Parent root = scene.getRoot();
        root.setStyle("-fx-background-color: white;");
        changeLabelStyle(root, "black");
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