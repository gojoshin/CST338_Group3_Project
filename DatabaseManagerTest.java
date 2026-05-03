import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseManagerTest {
    @TempDir
    Path tempDir;

    private DatabaseManager manager;

    @BeforeEach
    void setUp() {
        System.setProperty("app.db.url", "jdbc:sqlite:" + tempDir.resolve("test.db"));
        manager = DatabaseManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        manager.close();
        System.clearProperty("app.db.url");
    }

    @Test
    void registerUserSavesUsernameAndPassword() {
        assertTrue(manager.registerUser("newUser", "secret123"));
        assertTrue(manager.validateLogin("newUser", "secret123"));
        assertFalse(manager.validateLogin("newUser", "wrongPassword"));
    }

    @Test
    void registerUserPersistsAfterDatabaseReopens() {
        assertTrue(manager.registerUser("savedUser", "password123"));

        manager.close();
        manager = DatabaseManager.getInstance();

        assertTrue(manager.validateLogin("savedUser", "password123"));
    }

    @Test
    void registerUserRejectsDuplicateUsername() {
        assertTrue(manager.registerUser("duplicateUser", "firstPassword"));
        assertFalse(manager.registerUser("duplicateUser", "secondPassword"));
    }
}
