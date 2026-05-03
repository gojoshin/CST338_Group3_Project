import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * [Login Scene]
 * @ author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/26/2026
 */
public class LoginController{

    public static Scene build(Stage stage) {

        Label title = new Label("Trivia Game Login");

//      Username
        Label userLabel = new Label("Username: ");

        TextField usernameField = new TextField();
        usernameField.setMaxWidth(200);

//        Password
        Label passwordLabel = new Label("Password: ");

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);

//      This one for error messages
        Label messageLabel = new Label();

//      Buttons
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8"
        );

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Enter username and password.");
                return;
            }

            // only to go to next scene till we work on database
            if (username.equals("user") && password.equals("123")) {
                stage.setScene(SceneFactory.create(SceneType.USER_DASHBOARD, stage));
            } else if (username.equals("admin") && password.equals("456")){
                stage.setScene(SceneFactory.create(SceneType.ADMIN_DASHBOARD, stage));
            } else{
                messageLabel.setText("Invalid username or password");
            }

//             if the user is admin it will take to the admin dashboard or the user
        });
        Button registerBtn = new Button("Create Account");
        registerBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

        registerBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.REGISTER, stage));
        });

        // dark mode toggle
        Button toggleBtn = new Button("Switch to Dark Mode");
        toggleBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

        toggleBtn.setOnAction(e -> {
            TriviaGame.darkMode = !TriviaGame.darkMode;

            if (TriviaGame.darkMode) {
                TriviaGame.applyDarkMode(stage.getScene());
                toggleBtn.setText("Switch to Light Mode");

            } else {
                TriviaGame.applyLightMode(stage.getScene());
                toggleBtn.setText("Switch to Dark Mode");
            }
        });

        VBox root = new VBox(15, title,userLabel, usernameField, passwordLabel,
                passwordField, messageLabel, loginBtn, registerBtn, toggleBtn
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 400, 400);
    }


}