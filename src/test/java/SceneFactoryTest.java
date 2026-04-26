import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

class SceneFactoryTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createMainScene() {
        Stage stage = new Stage();

        Scene scene = SceneFactory.create(SceneType.MAIN, stage);

        assertNotNull(scene);
        assertEquals(600, scene.getWidth());
        assertEquals(400, scene.getHeight());
    }
}