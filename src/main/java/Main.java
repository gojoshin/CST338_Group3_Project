import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * [Brief one-sentence description of what this class does.]
 *
 * @author Jayson Jauregui
 * @version 0.1.0
 * @since 3/11/26
 */
public class Main extends Application {
  @Override
  public void start(Stage stage) {
    stage.setTitle("Trivia Game");
    stage.setScene(SceneFactory.create(SceneType.MAIN, stage));
    stage.show();
  }

  public static void main(String[] args){
    launch(args);
  }
}