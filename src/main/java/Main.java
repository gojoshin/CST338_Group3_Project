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

  // Window dimensions in pixels
  private static final int SCENE_WIDTH = 400;
  private static final int SCENE_HEIGHT = 300;
  private static final String F_TO_C_LABEL = "Fahrenheit -> Celcius";
  private static final String S1_PROMPT = "Enter F: ";
  private static final String CONVERT = "Convert";
private static final String ERROR_MSG = "Error";
  // Text used for both the window title bar and the on-screen label
  private static final String TITLE = "Hello There: ";

  /**
   * Application entry point. JavaFX requires calling launch(), which
   * internally creates the JavaFX runtime and calls start().
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Called by the JavaFX runtime after the application is initialized.
   * Build your scene graph here and show the primary Stage (window).
   *
   * @param stage the primary window provided by the JavaFX runtime
   */
  @Override
  public void start(Stage stage) {

    Label s1Label = new Label(F_TO_C_LABEL);
    TextField s1Input = new TextField();
    s1Input.setPromptText(S1_PROMPT);
    s1Input.setPrefWidth(200);

    Button s1Convert = new Button(CONVERT);
    Label s1Result = new Label();

    s1Convert.setOnAction(e ->{
      String input = s1Input.getText();
      try{

        double value =  Double.parseDouble(input);
        s1Result.setText(String.format("%.2f",TemperatureConverters.FtoC(value)));
      } catch (NumberFormatException ex) {
        s1Result.setText(ERROR_MSG + input);
      }
    });

    VBox root1 = new VBox(12,s1Label, s1Input, s1Convert, s1Result);

    root1.setPadding(new Insets(30));
    root1.setAlignment(Pos.CENTER);


    // Scene holds the layout and defines the window size
    Scene scene = new Scene(root1, SCENE_WIDTH, SCENE_HEIGHT);

    stage.setTitle(F_TO_C_LABEL); // text shown in the OS title bar
    stage.setScene(scene);
    stage.show();                 // make the window visible
  }
}