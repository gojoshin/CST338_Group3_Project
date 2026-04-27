import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * [Category Selection Scene]
 *
 * @author Ruth Ramirez
 * @version 0.1.0
 * @since 04/26/2026
 */

public class CategorySelection {

    public static boolean isValidCategory(String category) {
        if (category == null) return false;

        return category.equals("Science")
                || category.equals("History")
                || category.equals("Movies");
    }

    public static Scene build(Stage stage) {
        Label title = new Label("Choose a Category");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button scienceBtn = new Button("Science");
        Button historyBtn = new Button("History");
        Button moviesBtn = new Button("Movies");

        VBox layout = new VBox(15, title, scienceBtn, historyBtn, moviesBtn);
        layout.setAlignment(Pos.CENTER);

        scienceBtn.setOnAction(e -> {
            System.out.println("Selected Science");
        });

        historyBtn.setOnAction(e -> {
            System.out.println("Selected History");
        });

        moviesBtn.setOnAction(e -> {
            System.out.println("Selected Movies");
        });

        return new Scene(layout, 400, 400);
    }

}