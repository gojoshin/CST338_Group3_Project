/**
 * [Session Manager]
 * @ author Ruth Ramirez
 * @version 0.1.0
 * @since 5/4/2026
 */
public class SessionManager {
    private static String currentUsername = "";
    private static int currentUserId = -1;

    public static void setCurrentUser(String username) {
        currentUsername = username;
        currentUserId = DatabaseManager.getInstance().getUserId(username);
    }

    public static String getCurrentUsername() { return currentUsername; }
    public static int getCurrentUserId() { return currentUserId; }

    public static void logout() {
        currentUsername = "";
        currentUserId = -1;
    }
}

