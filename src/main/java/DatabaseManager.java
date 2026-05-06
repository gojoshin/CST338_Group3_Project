/**
 * DatabaseManager.java
 * This class manages the database connection and operations for the trivia application
 *
 * @author Joshua Shin
 * @version 1.0
 * @since 04/24/2026
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:app.db";
    private static final String DEFAULT_USER_ROLE = "user";

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(getDatabaseUrl());
            createTables();
            seedDefaultQuestions();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createTables() throws SQLException {
        try (Statement s = connection.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                )
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS categories (
                    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    description TEXT
                )
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS questions (
                    question_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_id INTEGER NOT NULL,
                    question_text TEXT NOT NULL,
                    option_a TEXT NOT NULL,
                    option_b TEXT NOT NULL,
                    option_c TEXT NOT NULL,
                    option_d TEXT NOT NULL,
                    correct_answer TEXT NOT NULL,
                    FOREIGN KEY (category_id) REFERENCES categories(category_id)
                )
            """);
            s.execute("""
                CREATE TABLE IF NOT EXISTS quiz_attempts (
                    attempt_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    category_id INTEGER NOT NULL,
                    score INTEGER NOT NULL,
                    total_questions INTEGER NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id),
                    FOREIGN KEY (category_id) REFERENCES categories(category_id)
                )
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS user_answers (
                    answer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    attempt_id INTEGER NOT NULL,
                    question_id INTEGER NOT NULL,
                    selected_answer TEXT NOT NULL,
                    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(attempt_id),
                    FOREIGN KEY (question_id) REFERENCES questions(question_id)
                )
                 
            """);
        }
    }

    private static String getDatabaseUrl() {
        return System.getProperty("app.db.url", DEFAULT_DB_URL);
    }

    public boolean registerUser(String username, String password) {
        String cleanedUsername = cleanUsername(username);

        if (cleanedUsername.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        String sql = """
                INSERT INTO users (username, password, role)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedUsername);
            ps.setString(2, password);
            ps.setString(3, DEFAULT_USER_ROLE);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (isUniqueUsernameError(e)) {
                return false;
            }
            throw new RuntimeException("User registration failed", e);
        }
    }

    public boolean deleteUser(String username) {
        String cleanedUsername = cleanUsername(username);

        if (cleanedUsername.isEmpty()) {
            return false;
        }

        String sql = """
                DELETE FROM users
                WHERE username = ? AND role = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedUsername);
            ps.setString(2, DEFAULT_USER_ROLE);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("User deletion failed", e);
        }
    }

    public boolean validateLogin(String username, String password) {
        String cleanedUsername = cleanUsername(username);

        if (cleanedUsername.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        String sql = """
                SELECT 1
                FROM users
                WHERE username = ? AND password = ?
                LIMIT 1
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedUsername);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Login validation failed", e);
        }
    }

    public List<UserAccount> getRegisteredUsers() {
        List<UserAccount> users = new ArrayList<>();
        String sql = """
                SELECT username, password
                FROM users
                WHERE role = ?
                ORDER BY user_id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, DEFAULT_USER_ROLE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new UserAccount(
                            rs.getString("username"),
                            rs.getString("password")
                    ));
                }
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Could not load registered users", e);
        }
    }

    public boolean saveQuizAttempt(String username, String categoryName, int score, int totalQuestions) {
        int userId = getUserIdByUsername(username);
        int categoryId = getCategoryIdByName(categoryName);

        if (userId < 1 || categoryId < 1 || score < 0 || totalQuestions < 0 || score > totalQuestions) {
            return false;
        }

        String sql = """
                INSERT INTO quiz_attempts (user_id, category_id, score, total_questions)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, categoryId);
            ps.setInt(3, score);
            ps.setInt(4, totalQuestions);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Could not save quiz attempt", e);
        }
    }

    public List<QuizAttempt> getQuizAttemptsForUser(String username) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String cleanedUsername = cleanUsername(username);

        if (cleanedUsername.isEmpty()) {
            return attempts;
        }

        String sql = """
                SELECT qa.attempt_id,
                       u.username,
                       c.name AS category_name,
                       qa.score,
                       qa.total_questions
                FROM quiz_attempts qa
                JOIN users u ON u.user_id = qa.user_id
                JOIN categories c ON c.category_id = qa.category_id
                WHERE u.username = ?
                ORDER BY qa.attempt_id DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedUsername);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    attempts.add(buildQuizAttempt(rs));
                }
            }
            return attempts;
        } catch (SQLException e) {
            throw new RuntimeException("Could not load quiz attempts", e);
        }
    }

    public List<QuizAttempt> getAllQuizAttempts() {
        List<QuizAttempt> attempts = new ArrayList<>();
        String sql = """
                SELECT qa.attempt_id,
                       u.username,
                       c.name AS category_name,
                       qa.score,
                       qa.total_questions
                FROM quiz_attempts qa
                JOIN users u ON u.user_id = qa.user_id
                JOIN categories c ON c.category_id = qa.category_id
                ORDER BY qa.score DESC, qa.total_questions DESC, qa.attempt_id DESC
                """;

        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                attempts.add(buildQuizAttempt(rs));
            }
            return attempts;
        } catch (SQLException e) {
            throw new RuntimeException("Could not load leaderboard", e);
        }
    }

    public ArrayList<Questions> getQuestions(String category) {
        ArrayList<Questions> questions = new ArrayList<>();
        String cleanedCategory = cleanCategory(category);

        if (cleanedCategory.isEmpty()) {
            return questions;
        }

        String sql = """
                SELECT q.question_text,
                       q.option_a,
                       q.option_b,
                       q.option_c,
                       q.option_d,
                       q.correct_answer
                FROM questions q
                JOIN categories c ON c.category_id = q.category_id
                WHERE c.name = ?
                ORDER BY q.question_id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedCategory);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    questions.add(new Questions(
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_answer")
                    ));
                }
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException("Could not load questions", e);
        }
    }

    public boolean addCategory(String name, String description) {
        String cleanedName = cleanCategory(name);

        if (cleanedName.isEmpty()) {
            return false;
        }

        String sql = """
                INSERT INTO categories (name, description)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedName);
            ps.setString(2, description == null ? "" : description.trim());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (isUniqueUsernameError(e)) {
                return false;
            }
            throw new RuntimeException("Could not add category", e);
        }
    }

    public List<String> getCategoryNames() {
        List<String> categories = new ArrayList<>();
        String sql = """
                SELECT name
                FROM categories
                ORDER BY name
                """;

        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("Could not load categories", e);
        }
    }

    public boolean addQuestion(String categoryName, String questionText,
                               String optionA, String optionB,
                               String optionC, String optionD,
                               String correctAnswer) {
        int categoryId = getCategoryIdByName(categoryName);
        String cleanedQuestion = cleanRequiredText(questionText);
        String cleanedOptionA = cleanRequiredText(optionA);
        String cleanedOptionB = cleanRequiredText(optionB);
        String cleanedOptionC = cleanRequiredText(optionC);
        String cleanedOptionD = cleanRequiredText(optionD);
        String cleanedCorrectAnswer = cleanRequiredText(correctAnswer);

        if (categoryId < 1
                || cleanedQuestion.isEmpty()
                || cleanedOptionA.isEmpty()
                || cleanedOptionB.isEmpty()
                || cleanedOptionC.isEmpty()
                || cleanedOptionD.isEmpty()
                || cleanedCorrectAnswer.isEmpty()
                || !matchesAnswerOption(cleanedCorrectAnswer, cleanedOptionA, cleanedOptionB,
                        cleanedOptionC, cleanedOptionD)) {
            return false;
        }

        String sql = """
                INSERT INTO questions (
                    category_id,
                    question_text,
                    option_a,
                    option_b,
                    option_c,
                    option_d,
                    correct_answer
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setString(2, cleanedQuestion);
            ps.setString(3, cleanedOptionA);
            ps.setString(4, cleanedOptionB);
            ps.setString(5, cleanedOptionC);
            ps.setString(6, cleanedOptionD);
            ps.setString(7, cleanedCorrectAnswer);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Could not add question", e);
        }
    }

    public List<String> getQuestionsWithCategory() {
        List<String> questions = new ArrayList<>();
        String sql = """
                SELECT q.question_id, c.name, q.question_text
                FROM questions q
                JOIN categories c ON c.category_id = q.category_id
                ORDER BY q.question_id DESC
                """;

        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                questions.add(
                        rs.getInt("question_id") + " | "
                                + rs.getString("name") + " | "
                                + rs.getString("question_text")
                );
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException("Could not load question summaries", e);
        }
    }

    public boolean deleteQuestionById(int questionId) {
        if (questionId < 1) {
            return false;
        }

        String sql = """
                DELETE FROM questions
                WHERE question_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete question", e);
        }
    }

    public ArrayList<Questions> getQuestionsByCategory(String categoryName) {
        return getQuestions(categoryName);
    }

    public static class UserAccount {
        private final String username;
        private final String password;

        public UserAccount(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class QuizAttempt {
        private final int attemptId;
        private final String username;
        private final String categoryName;
        private final int score;
        private final int totalQuestions;

        public QuizAttempt(int attemptId, String username, String categoryName,
                           int score, int totalQuestions) {
            this.attemptId = attemptId;
            this.username = username;
            this.categoryName = categoryName;
            this.score = score;
            this.totalQuestions = totalQuestions;
        }

        public int getAttemptId() {
            return attemptId;
        }

        public String getUsername() {
            return username;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public int getScore() {
            return score;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public String getScoreText() {
            return score + " / " + totalQuestions;
        }

        public String getPercentageText() {
            if (totalQuestions == 0) {
                return "0%";
            }
            return Math.round((score * 100.0) / totalQuestions) + "%";
        }
    }

    private static String cleanUsername(String username) {
        return username == null ? "" : username.trim();
    }

    private static String cleanCategory(String category) {
        return category == null ? "" : category.trim();
    }

    private static String cleanRequiredText(String text) {
        return text == null ? "" : text.trim();
    }

    private static boolean isUniqueUsernameError(SQLException e) {
        String message = e.getMessage();
        return message != null && message.toLowerCase().contains("unique");
    }

    private int getCategoryIdByName(String categoryName) {
        String cleanedCategory = cleanCategory(categoryName);

        if (cleanedCategory.isEmpty()) {
            return -1;
        }

        String sql = """
                SELECT category_id
                FROM categories
                WHERE name = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedCategory);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find category", e);
        }
    }

    private int getUserIdByUsername(String username) {
        String cleanedUsername = cleanUsername(username);

        if (cleanedUsername.isEmpty()) {
            return -1;
        }

        String sql = """
                SELECT user_id
                FROM users
                WHERE username = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cleanedUsername);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find user", e);
        }
    }

    private QuizAttempt buildQuizAttempt(ResultSet rs) throws SQLException {
        return new QuizAttempt(
                rs.getInt("attempt_id"),
                rs.getString("username"),
                rs.getString("category_name"),
                rs.getInt("score"),
                rs.getInt("total_questions")
        );
    }

    private static boolean matchesAnswerOption(String correctAnswer, String optionA, String optionB,
                                               String optionC, String optionD) {
        return correctAnswer.equals(optionA)
                || correctAnswer.equals(optionB)
                || correctAnswer.equals(optionC)
                || correctAnswer.equals(optionD);
    }

    private void seedDefaultQuestions() throws SQLException {
        for (DefaultQuestion question : DEFAULT_QUESTIONS) {
            int categoryId = getOrCreateCategoryId(question.category());
            insertQuestionIfMissing(categoryId, question);
        }
    }

    private int getOrCreateCategoryId(String category) throws SQLException {
        String insertSql = """
                INSERT OR IGNORE INTO categories (name, description)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            ps.setString(1, category);
            ps.setString(2, category + " trivia questions");
            ps.executeUpdate();
        }

        String selectSql = """
                SELECT category_id
                FROM categories
                WHERE name = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
        }

        throw new SQLException("Could not find category: " + category);
    }

    private void insertQuestionIfMissing(int categoryId, DefaultQuestion question) throws SQLException {
        String sql = """
                INSERT INTO questions (
                    category_id,
                    question_text,
                    option_a,
                    option_b,
                    option_c,
                    option_d,
                    correct_answer
                )
                SELECT ?, ?, ?, ?, ?, ?, ?
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM questions
                    WHERE category_id = ? AND question_text = ?
                )
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setString(2, question.question());
            ps.setString(3, question.optionA());
            ps.setString(4, question.optionB());
            ps.setString(5, question.optionC());
            ps.setString(6, question.optionD());
            ps.setString(7, question.correctOption());
            ps.setInt(8, categoryId);
            ps.setString(9, question.question());
            ps.executeUpdate();
        }
    }

    private record DefaultQuestion(
            String category,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctOption
    ) {
    }

    private static final List<DefaultQuestion> DEFAULT_QUESTIONS = List.of(
            new DefaultQuestion("Science", "What planet is closest to the Sun?",
                    "Venus", "Mercury", "Earth", "Mars", "Mercury"),
            new DefaultQuestion("Science", "What gas do plants absorb from the atmosphere?",
                    "Oxygen", "Nitrogen", "Carbon Dioxide", "Helium", "Carbon Dioxide"),
            new DefaultQuestion("Science", "What is the chemical symbol for water?",
                    "H2O", "CO2", "O2", "NaCl", "H2O"),
            new DefaultQuestion("Science", "How many bones are in the adult human body?",
                    "106", "206", "306", "186", "206"),
            new DefaultQuestion("Science", "What is the largest organ in the human body?",
                    "Heart", "Liver", "Skin", "Brain", "Skin"),
            new DefaultQuestion("Science", "What planet is the largest in the solar system?",
                    "Jupiter", "Neptune", "Saturn", "Uranus", "Jupiter"),
            new DefaultQuestion("Science", "What is the process by which light bends through water droplets forming a rainbow?",
                    "Reflection", "Absorption", "Refraction", "Emission", "Refraction"),
            new DefaultQuestion("History", "In what year did World War II end?",
                    "1943", "1944", "1945", "1946", "1945"),
            new DefaultQuestion("History", "Who was the first President of the United States?",
                    "John Adams", "Thomas Jefferson", "George Washington", "Ben Franklin", "George Washington"),
            new DefaultQuestion("History", "What ancient civilization built the pyramids?",
                    "Romans", "Greeks", "Egyptians", "Persians", "Egyptians"),
            new DefaultQuestion("History", "What year did the Titanic sink?",
                    "1905", "1912", "1920", "1898", "1912"),
            new DefaultQuestion("History", "Which country gifted the Statue of Liberty to the US?",
                    "England", "Spain", "France", "Germany", "France"),
            new DefaultQuestion("Movies", "What movie features a character named Forrest Gump?",
                    "Cast Away", "Forrest Gump", "The Green Mile", "Big", "Forrest Gump"),
            new DefaultQuestion("Movies", "Who directed Jurassic Park?",
                    "James Cameron", "Ridley Scott", "Steven Spielberg", "George Lucas", "Steven Spielberg"),
            new DefaultQuestion("Movies", "What is the highest-grossing film of all time?",
                    "Avengers: Endgame", "Avatar", "Titanic", "Star Wars", "Avatar"),
            new DefaultQuestion("Movies", "In The Lion King, what is Simba's father's name?",
                    "Scar", "Mufasa", "Rafiki", "Zazu", "Mufasa"),
            new DefaultQuestion("Movies", "What year was the first Toy Story released?",
                    "1993", "1995", "1997", "1999", "1995")
    );

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored) {
        } finally {
            instance = null;
        }
    }

}
