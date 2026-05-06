import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * [Scene Factory Class]
 *
 * @author Jasmeen Hothi
 * @version 0.1.0
 * @since 04/25/2026
 */

public class SceneFactory {
    public static Scene create(SceneType type, Stage stage) {
        Scene scene = switch (type) {
            case LOGIN -> LoginController.build(stage);
            case REGISTER -> RegisterController.build(stage);
            case USER_DASHBOARD -> userDashboard.build(stage);
            case ADMIN_DASHBOARD -> adminDashboard.build(stage);
            case MANAGE_QUIZ -> ManageQuizDashboard.build(stage);
            case MANAGE_CATEGORY -> ManageCategory.build(stage);
            case MANAGE_QUESTIONS -> ManageQuestions.build(stage);
            case ADD_QUESTIONS -> AddNewQuestions.build(stage);
            case CATEGORY_SELECTION -> CategorySelection.build(stage);
            case GAME -> GameScene.build(stage);
            case LEADERBOARD -> LeaderboardScene.build(stage);
        };

        ThemeManager.applyTheme(scene);
        return scene;
    }
}
