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
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123";

    public static Scene build(Stage stage) {

        Label title = new Label("Trivia Game Login");
        title.setStyle("-fx-font-size: 26px;" +
                "-fx-font-weight: bold;");

//      Username
        Label userLabel = new Label("Username: ");
        userLabel.setStyle("-fx-font-size: 16px;");

        TextField usernameField = new TextField();
        usernameField.setMaxWidth(200);

//        Password
        Label passwordLabel = new Label("Password: ");
        passwordLabel.setStyle("-fx-font-size: 16px;");

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
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Enter username and password.");
                return;
            }

            if (isAdminLogin(username, password)) {
                SessionManager.loginAdmin();
                stage.setScene(SceneFactory.create(SceneType.ADMIN_DASHBOARD, stage));
            } else if (DatabaseManager.getInstance().validateLogin(username, password)) {
                SessionManager.loginUser(username);
                stage.setScene(SceneFactory.create(SceneType.USER_DASHBOARD, stage));
            } else {
                SessionManager.logout();
                messageLabel.setText("Invalid login.");
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

        VBox root = new VBox(15, title,userLabel, usernameField, passwordLabel,
                passwordField, messageLabel, loginBtn, registerBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.LOGIN)
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 400, 430);
    }

    private static boolean isAdminLogin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
