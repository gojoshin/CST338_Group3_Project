import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class ThemeManager {
    private static final String DARK_THEME = "dark-theme";
    private static final String LIGHT_THEME = "light-theme";
    private static final String STYLESHEET = ThemeManager.class
            .getResource("/theme.css")
            .toExternalForm();

    public static void applyTheme(Scene scene) {
        Parent root = scene.getRoot();

        scene.getStylesheets().remove(STYLESHEET);
        scene.getStylesheets().add(STYLESHEET);

        root.getStyleClass().removeAll(DARK_THEME, LIGHT_THEME);
        root.getStyleClass().add(TriviaGame.darkMode ? DARK_THEME : LIGHT_THEME);
    }

    public static CheckBox createDarkModeToggle(Stage stage, SceneType currentScene) {
        CheckBox darkModeToggle = new CheckBox("Dark Mode");
        darkModeToggle.setSelected(TriviaGame.darkMode);
        darkModeToggle.getStyleClass().add("theme-toggle");

        darkModeToggle.setOnAction(e -> {
            TriviaGame.darkMode = darkModeToggle.isSelected();
            stage.setScene(SceneFactory.create(currentScene, stage));
        });

        return darkModeToggle;
    }
}
