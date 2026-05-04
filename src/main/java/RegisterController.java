import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * [Register Scene]
 * @author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/29/2026
 */

public class RegisterController {
    public static Scene build(Stage stage) {

        Label title = new Label("Register");
        title.setStyle("-fx-font-size: 26px;" +
                "-fx-font-weight: bold;");

//      username
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-font-size: 16px;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("eg. otter 123");
        usernameField.setMaxWidth(200);

//      password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);

//      Confirm password label
        Label confirmLabel = new Label("Confirm Password:");
        confirmLabel.setStyle("-fx-font-size: 16px;");

        PasswordField confirmField = new PasswordField();
        confirmField.setMaxWidth(200);

//        error messages
        Label messageLabel = new Label();

//      Register button
        Button registerBtn = new Button("Register");
        registerBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirm = confirmField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Enter username and password.");
                return;
            }

//          checks if both password and confirm password are same
            if (!password.equals(confirm)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }
//            another if we already have the username in the database
            if (DatabaseManager.getInstance().registerUser(username, password)) {
                messageLabel.setText("Account created.");
                usernameField.clear();
                passwordField.clear();
                confirmField.clear();
            } else {
                messageLabel.setText("Username already exists.");
            }
        });

//       back buttons takes back to the login page
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

        backBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
        });

        VBox root = new VBox(15, title, userLabel, usernameField,
                passwordLabel, passwordField, confirmLabel, confirmField, messageLabel,
                registerBtn, backBtn, ThemeManager.createDarkModeToggle(stage, SceneType.REGISTER)
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        return new Scene(root, 400, 470);
    }
}
