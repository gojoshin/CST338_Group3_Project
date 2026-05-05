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
    void deleteUserRemovesUserFromLoginAndRegisteredUsers() {
        assertTrue(manager.registerUser("deleteMe", "password123"));
        assertTrue(manager.validateLogin("deleteMe", "password123"));

        assertTrue(manager.deleteUser("deleteMe"));

        assertFalse(manager.validateLogin("deleteMe", "password123"));
        assertTrue(manager.getRegisteredUsers().isEmpty());
    }

    @Test
    void deleteUserReturnsFalseForMissingUser() {
        assertFalse(manager.deleteUser("missingUser"));
        assertFalse(manager.deleteUser(""));
        assertFalse(manager.deleteUser(null));
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
    void getQuestionsReturnsSeededQuestionsForCategory() {
        List<Questions> scienceQuestions = manager.getQuestions("Science");

        assertEquals(7, scienceQuestions.size());

        Questions firstQuestion = scienceQuestions.get(0);
        assertEquals("What planet is closest to the Sun?", firstQuestion.getQuestion());
        assertEquals("Venus", firstQuestion.getChoiceA());
        assertEquals("Mercury", firstQuestion.getChoiceB());
        assertEquals("Earth", firstQuestion.getChoiceC());
        assertEquals("Mars", firstQuestion.getChoiceD());
        assertEquals("Mercury", firstQuestion.getCorrectAnswer());
        assertTrue(firstQuestion.isCorrect("Mercury"));
    }

    @Test
    void defaultQuestionsDoNotDuplicateAfterDatabaseReopens() {
        assertEquals(5, manager.getQuestions("History").size());

        manager.close();
        manager = DatabaseManager.getInstance();

        assertEquals(5, manager.getQuestions("History").size());
    }
}
