import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for GameLogic - checks answer and timer behavior.
 *
 * @author Jayson Jauregui
 * @version 0.1.0
 * @since 04/26/26
 */
class GameLogicTest {

    @Test
    void correctAnswerReturnsTrue() {
        // player picks the right answer
        assertTrue(GameLogic.isCorrect("Paris", "Paris"));
    }

    @Test
    void wrongAnswerReturnsFalse() {
        // player picks the wrong answer
        assertFalse(GameLogic.isCorrect("Berlin", "Paris"));
    }

    @Test
    void nullAnswerReturnsFalse() {
        // no answer selected (like if time runs out before clicking)
        assertFalse(GameLogic.isCorrect(null, "Paris"));
    }

    @Test
    void timerUpAtZero() {
        // time hits 0, should be up
        assertTrue(GameLogic.isTimeUp(0));
    }
}