import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * [Login Controller]
 *
 * @author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/29/2026
 */


public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        messageLabel.setText("");
    }

    @FXML
    private void handleLogin(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
            messageLabel.setText("Enter username and password");
            return;
        }

    }

    @FXML
    private void handleGoToRegister(){
        SceneManager.getInstance().navigateTo(SceneType.REGISTER);
    }
}
