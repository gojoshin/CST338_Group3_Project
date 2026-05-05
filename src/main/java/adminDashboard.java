import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * [Admin Dashboard Scene]
 * @ author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/26/2026
 */

public class adminDashboard {
    public static Scene build(Stage stage) {
        Label title = new Label("Welcome Admin!");
        title.setStyle("-fx-font-size: 26px;" +
                "-fx-font-weight: bold;");

        Label usersTitle = new Label("Registered Users");
        usersTitle.setStyle("-fx-font-size: 18px;" +
                "-fx-font-weight: bold;");

        TableView<DatabaseManager.UserAccount> userTable = new TableView<>();
        userTable.setMaxWidth(320);
        userTable.setPrefHeight(160);

        TableColumn<DatabaseManager.UserAccount, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(160);

        TableColumn<DatabaseManager.UserAccount, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setPrefWidth(160);

        userTable.getColumns().add(usernameColumn);
        userTable.getColumns().add(passwordColumn);
        loadUsers(userTable);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(150);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(150);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 13px;");

        Button addUserBtn = new Button("Add User");
        addUserBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #2c9f45;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        addUserBtn.setOnAction(e -> {
            boolean wasAdded = DatabaseManager.getInstance().registerUser(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if (wasAdded) {
                messageLabel.setText("User added.");
                usernameField.clear();
                passwordField.clear();
                loadUsers(userTable);
            } else {
                messageLabel.setText("Enter a unique username and password.");
            }
        });

        Button deleteUserBtn = new Button("Delete Selected");
        deleteUserBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #e33437;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        deleteUserBtn.setOnAction(e -> {
            DatabaseManager.UserAccount selectedUser = userTable.getSelectionModel().getSelectedItem();

            if (selectedUser == null) {
                messageLabel.setText("Select a user to delete.");
                return;
            }

            if (DatabaseManager.getInstance().deleteUser(selectedUser.getUsername())) {
                messageLabel.setText("User deleted.");
                loadUsers(userTable);
            } else {
                messageLabel.setText("Could not delete selected user.");
            }
        });

        HBox addUserRow = new HBox(10, usernameField, passwordField, addUserBtn);
        addUserRow.setAlignment(Pos.CENTER);

        HBox userActionsRow = new HBox(10, deleteUserBtn);
        userActionsRow.setAlignment(Pos.CENTER);

        Button refreshUsersBtn = new Button("Refresh Users");
        refreshUsersBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        refreshUsersBtn.setOnAction(e -> loadUsers(userTable));

        Button leaderboardBtn = new Button("LeaderBoard");
        leaderboardBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

//        todo will take to the leaderboard scene

        Button historyBtn = new Button("Quiz History");
        historyBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");

//       TODO will take to the history scene

        Button manageQueBtn = new Button("Manage Quiz");
        manageQueBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #5ba4fc;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
//        TODO manage quiz scene where admin can edit quiz

        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-font-size: 14px;"+
                "-fx-background-color: #e33437;"+
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white ;" +
                "-fx-background-radius: 8");
        logoutBtn.setOnAction(e -> {
            stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
        });

        VBox root = new VBox(15, title, usersTitle, userTable, addUserRow, userActionsRow,
                messageLabel, refreshUsersBtn,
                leaderboardBtn, historyBtn, manageQueBtn, logoutBtn,
                ThemeManager.createDarkModeToggle(stage, SceneType.ADMIN_DASHBOARD));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        return new Scene(root, 560, 650);
    }

    private static void loadUsers(TableView<DatabaseManager.UserAccount> userTable) {
        userTable.setItems(FXCollections.observableArrayList(
                DatabaseManager.getInstance().getRegisteredUsers()
        ));
    }
}
