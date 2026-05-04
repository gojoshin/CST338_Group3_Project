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
                ORDER BY username
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

    private static String cleanUsername(String username) {
        return username == null ? "" : username.trim();
    }

    private static boolean isUniqueUsernameError(SQLException e) {
        String message = e.getMessage();
        return message != null && message.toLowerCase().contains("unique");
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored) {
        } finally {
            instance = null;
        }
    }

}
