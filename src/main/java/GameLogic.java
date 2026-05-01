/**
 * GameLogic
 *
 * @author Jayson Jauregui
 * @version 1.0
 * @since 4/26/26
 * Description: Tests true and false answers / gamer logic
 */
public class GameLogic {

    // how many seconds the player gets per question
    private static final int TIME_LIMIT = 15;

    /**
     * Checks if the player's answer matches the correct answer.
     * returns true if they match, false otherwise
     */
    public static boolean isCorrect(String playerAnswer, String correctAnswer) {
        if (playerAnswer == null || correctAnswer == null) {
            return false;
        }
        return playerAnswer.equals(correctAnswer);
    }

    /**
     * Checks if the timer has run out.
     */
    public static boolean isTimeUp(int timeLeft) {
        return timeLeft <= 0;
    }

    /**
     * Returns the time limit per question.
     *
     * return the time limit in seconds
     */
    public static int getTimeLimit() {
        return TIME_LIMIT;
    }
}
