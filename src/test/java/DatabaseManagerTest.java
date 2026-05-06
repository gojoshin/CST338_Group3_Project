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
    void getCategoryNamesReturnsSeededCategories() {
        List<String> categories = manager.getCategoryNames();

        assertTrue(categories.contains("Science"));
        assertTrue(categories.contains("History"));
        assertTrue(categories.contains("Movies"));
    }

    @Test
    void saveQuizAttemptRecordsCompletedGameForUser() {
        assertTrue(manager.registerUser("scoreKeeper", "password123"));

        assertTrue(manager.saveQuizAttempt("scoreKeeper", "Science", 5, 7));
        assertTrue(manager.saveQuizAttempt("scoreKeeper", "History", 3, 5));

        List<DatabaseManager.QuizAttempt> attempts = manager.getQuizAttemptsForUser("scoreKeeper");

        assertEquals(2, attempts.size());
        assertEquals("History", attempts.get(0).getCategoryName());
        assertEquals("3 / 5", attempts.get(0).getScoreText());
        assertEquals("60%", attempts.get(0).getPercentageText());
        assertEquals("Science", attempts.get(1).getCategoryName());
        assertEquals("5 / 7", attempts.get(1).getScoreText());
        assertEquals("71%", attempts.get(1).getPercentageText());
    }

    @Test
    void saveQuizAttemptRejectsMissingDataAndImpossibleScores() {
        assertTrue(manager.registerUser("playerOne", "password123"));

        assertFalse(manager.saveQuizAttempt("missingUser", "Science", 1, 7));
        assertFalse(manager.saveQuizAttempt("playerOne", "Missing Category", 1, 7));
        assertFalse(manager.saveQuizAttempt("playerOne", "Science", 8, 7));
        assertTrue(manager.getQuizAttemptsForUser("playerOne").isEmpty());
    }

    @Test
    void addCategorySavesCategoryAndRejectsDuplicates() {
        assertTrue(manager.addCategory("Sports", "Sports trivia questions"));
        assertTrue(manager.getCategoryNames().contains("Sports"));
        assertFalse(manager.addCategory("Sports", "Duplicate sports category"));
    }

    @Test
    void addQuestionSavesQuestionAndDeleteQuestionRemovesIt() {
        assertTrue(manager.addCategory("Sports", "Sports trivia questions"));
        assertTrue(manager.addQuestion(
                "Sports",
                "How many points is a touchdown worth?",
                "3",
                "6",
                "7",
                "2",
                "6"
        ));

        List<Questions> sportsQuestions = manager.getQuestions("Sports");
        assertEquals(1, sportsQuestions.size());
        assertEquals("How many points is a touchdown worth?", sportsQuestions.get(0).getQuestion());

        int questionId = findQuestionId("Sports", "How many points is a touchdown worth?");
        assertTrue(manager.deleteQuestionById(questionId));
        assertTrue(manager.getQuestions("Sports").isEmpty());
    }

    @Test
    void addQuestionRejectsBlankFieldsAndCorrectAnswerOutsideOptions() {
        assertFalse(manager.addQuestion(
                "Science",
                "",
                "A",
                "B",
                "C",
                "D",
                "A"
        ));
        assertFalse(manager.addQuestion(
                "Science",
                "Which option is correct?",
                "A",
                "B",
                "C",
                "D",
                "E"
        ));
    }

    @Test
    void defaultQuestionsDoNotDuplicateAfterDatabaseReopens() {
        assertEquals(5, manager.getQuestions("History").size());

        manager.close();
        manager = DatabaseManager.getInstance();

        assertEquals(5, manager.getQuestions("History").size());
    }

    private int findQuestionId(String category, String questionText) {
        return manager.getQuestionsWithCategory().stream()
                .filter(row -> row.contains(" | " + category + " | " + questionText))
                .map(row -> row.split("\\|")[0].trim())
                .mapToInt(Integer::parseInt)
                .findFirst()
                .orElse(-1);
    }
}
