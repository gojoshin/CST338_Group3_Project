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
            case LOGIN -> loadScene("/fxml/login.fxml");
            case REGISTER -> loadScene("/fxml/register.fxml");
            case CATEGORY_SELECTION -> CategorySelection.build(stage);
            case GAME -> GameScene.build(stage);
        };
    }

    private static Scene loadScene(String fxmlPath){
        URL url = SceneFactory.class.getResource(fxmlPath);
        if(url == null){
            throw new IllegalArgumentException("FXML not found: " + fxmlPath);
        }
        try{
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            return new Scene(root);
        }catch (IOException e){
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }

}