public class SessionManager {
    private static String currentUsername;
    private static boolean adminLoggedIn;

    private SessionManager() {
    }

    public static void loginUser(String username) {
        currentUsername = username == null ? null : username.trim();
        adminLoggedIn = false;
    }

    public static void loginAdmin() {
        currentUsername = null;
        adminLoggedIn = true;
    }

    public static void logout() {
        currentUsername = null;
        adminLoggedIn = false;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static boolean isUserLoggedIn() {
        return currentUsername != null && !currentUsername.isEmpty();
    }

    public static boolean isAdminLoggedIn() {
        return adminLoggedIn;
    }
}
