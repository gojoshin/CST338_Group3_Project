/**
 * Timer.java
 * This class is for the countdown timer for trivia questions
 *
 * @author Jayson Jauregui
 * @version 1.0
 * @since 04/24/2026
 */
public class Timer {

    // stores how long the timer should be
    int timeLimit;

    // stores how much time is left
    int timeLeft;

    // constructor that sets the time limit
    public Timer(int timeLimit) {
        this.timeLimit = timeLimit;
        this.timeLeft = timeLimit;
    }

    // resets the timer back to full
    public void reset() {
        this.timeLeft = this.timeLimit;
    }

    // takes away one second
    public void tick() {
        if (this.timeLeft > 0) {
            this.timeLeft = this.timeLeft - 1;
        }
    }

    // checks if the time ran out
    public boolean isTimeUp() {
        if (this.timeLeft <= 0) {
            return true;
        } else {
            return false;
        }
    }

    // getter for time left
    public int getTimeLeft() {
        return this.timeLeft;
    }

    // getter for time limit
    public int getTimeLimit() {
        return this.timeLimit;
    }
}