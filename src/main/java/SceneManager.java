import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

/**
 * [Scene Manager Class]
 *
 * @author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/29/2026
 */


public class SceneManager {
    private static SceneManager instance;

    private final Stage stage;
    private final Map<SceneType, Scene> cache = new EnumMap<>(SceneType.class);

    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    public static void init(Stage stage) {
        if (instance == null) instance = new SceneManager(stage);
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException
                    ("SceneManager not initialised");
        }
        return instance;
    }

    public void navigateTo(SceneType type) {
        if (type == SceneType.LOGIN || type == SceneType.REGISTER) {
            stage.setScene(SceneFactory.create(type, stage));
            return;
        }
        Scene scene = cache.computeIfAbsent(type, t -> SceneFactory.create(t, stage));
        stage.setScene(scene);
    }

}
