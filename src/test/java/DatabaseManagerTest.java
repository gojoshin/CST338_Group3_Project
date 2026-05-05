import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void getRegisteredUsersReturnsSavedUsernamesAndPasswords() {
        assertTrue(manager.registerUser("cooper", "bt7274"));
        assertTrue(manager.registerUser("bt", "titan"));

        List<DatabaseManager.UserAccount> users = manager.getRegisteredUsers();

        assertEquals(2, users.size());
        assertEquals("cooper", users.get(0).getUsername());
        assertEquals("bt7274", users.get(0).getPassword());
        assertEquals("bt", users.get(1).getUsername());
        assertEquals("titan", users.get(1).getPassword());
    }

    @Test
    void addCategorySavesCategoryName() {
        assertTrue(manager.addCategory("Science", "Science questions"));

        List<String> categories = manager.getCategoryNames();

        assertTrue(categories.contains("Science"));
    }

    @Test
    void addQuestionStoresQuestionCorrectly() {
        manager.addCategory("Science", "Science questions");

        assertTrue(manager.addQuestion(
                "Science",
                "What part of the plant conducts photosynthesis?",
                "Root", "Stem", "Leaf", "Flower",
                "Leaf"
        ));

        List<Questions> questions = manager.getQuestionsByCategory("Science");

        assertEquals(1, questions.size());
        assertEquals("What part of the plant conducts photosynthesis?", questions.get(0).getQuestion());
        assertEquals("Leaf", questions.get(0).getCorrectAnswer());
    }
}
