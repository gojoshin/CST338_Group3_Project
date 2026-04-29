import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL =
            System.getProperty("app.db.url", "jdbc:sqlite:trivia.db");

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
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

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored) {
        } finally {
            instance = null;
        }
    }

}
