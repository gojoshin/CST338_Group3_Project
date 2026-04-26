import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos ;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
            case MAIN -> buildMainScene(stage);
            case LOGIN -> buildLoginScene(stage);
            case REGISTER -> buildRegisterScene(stage);
        };
    }

    private static Scene buildMainScene(Stage stage) {
        Label title = new Label("Trivia Game");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button loginBtn = new Button("Log In");
        loginBtn.setOnAction(e -> stage.setScene(create(SceneType.LOGIN, stage)));

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> stage.setScene(create(SceneType.REGISTER, stage)));

        VBox layout = new VBox(15, title, loginBtn, registerBtn);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 600, 400);
    }

    private static Scene buildLoginScene(Stage stage) { /* TODO */
        return null;
    }

    private static Scene buildRegisterScene(Stage stage) { /* TODO */
        return null;
    }

}