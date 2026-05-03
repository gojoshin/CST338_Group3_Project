import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos ;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;

/**
 * [Scene Factory Class]
 *
 * @author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/25/2026
 */

public class SceneFactory {
    public static Scene create(SceneType type, Stage stage) {
        return switch (type) {
            case LOGIN -> LoginController.build(stage);
            case REGISTER -> RegisterController.build(stage);
            case USER_DASHBOARD -> userDashboard.build(stage);
            case ADMIN_DASHBOARD -> adminDashboard.build(stage);
            case CATEGORY_SELECTION -> CategorySelection.build(stage);
            case GAME -> GameScene.build(stage);
            case LEADERBOARD -> leaderboard.build(stage);
        };
    }

}