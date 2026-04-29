/**
 * Questions.java
 * This class holds one trivia question and the 4 answer choices
 * and also knows which one is the right answer
 *
 * @author Jayson Jauregui
 * @version 1.0
 * @since 04/29/2026
 */
public class Questions {

    // the question
    String question;

    // the four answer choices
    String choiceA;
    String choiceB;
    String choiceC;
    String choiceD;

    // the correct answer
    String correctAnswer;

    // constructor
    public Questions(String question, String choiceA, String choiceB,
                     String choiceC, String choiceD, String correctAnswer) {
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correctAnswer = correctAnswer;
    }

    // checks if the answer the player picked is correct
    public boolean isCorrect(String playerAnswer) {
        if (playerAnswer == null) {
            return false;
        }
        if (playerAnswer.equals(this.correctAnswer)) {
            return true;
        } else {
            return false;
        }
    }

    // getters for everything

    public String getQuestion() {
        return this.question;
    }

    public String getChoiceA() {
        return this.choiceA;
    }

    public String getChoiceB() {
        return this.choiceB;
    }

    public String getChoiceC() {
        return this.choiceC;
    }

    public String getChoiceD() {
        return this.choiceD;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }
}