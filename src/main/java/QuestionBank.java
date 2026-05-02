import java.util.ArrayList;

/**
 * QuestionBank.java
 * Holds all the trivia questions sorted by category.
 * Just add more questions to the lists below.
 *
 * @author Jayson Jauregui
 * @version 1.0
 * @since 04/30/2026
 */
public class QuestionBank {

    // returns a list of questions for the given category
    public static ArrayList<Questions> getQuestions(String category) {

        ArrayList<Questions> list = new ArrayList<Questions>();

        if (category.equals("Science")) {

            list.add(new Questions(
                    "What planet is closest to the Sun?",
                    "Venus", "Mercury", "Earth", "Mars",
                    "Mercury"
            ));

            list.add(new Questions(
                    "What gas do plants absorb from the atmosphere?",
                    "Oxygen", "Nitrogen", "Carbon Dioxide", "Helium",
                    "Carbon Dioxide"
            ));

            list.add(new Questions(
                    "What is the chemical symbol for water?",
                    "H2O", "CO2", "O2", "NaCl",
                    "H2O"
            ));

            list.add(new Questions(
                    "How many bones are in the adult human body?",
                    "106", "206", "306", "186",
                    "206"
            ));

            list.add(new Questions(
                    "What is the largest organ in the human body?",
                    "Heart", "Liver", "Skin", "Brain",
                    "Skin"
            ));

            list.add(new Questions(
                    "What planet is the largest in the solar system?",
                    "Jupiter", "Neptune", "Saturn", "Uranus",
                    "Jupiter"
            ));

            list.add(new Questions(
                    "What is the process by which light bends through water droplets forming a ranbow?",
                    "Reflection", "Absorption","Refraction", "Emission",
                    "Refraction"
            ));


        } else if (category.equals("History")) {

            list.add(new Questions(
                    "In what year did World War II end?",
                    "1943", "1944", "1945", "1946",
                    "1945"
            ));

            list.add(new Questions(
                    "Who was the first President of the United States?",
                    "John Adams", "Thomas Jefferson", "George Washington", "Ben Franklin",
                    "George Washington"
            ));

            list.add(new Questions(
                    "What ancient civilization built the pyramids?",
                    "Romans", "Greeks", "Egyptians", "Persians",
                    "Egyptians"
            ));

            list.add(new Questions(
                    "What year did the Titanic sink?",
                    "1905", "1912", "1920", "1898",
                    "1912"
            ));

            list.add(new Questions(
                    "Which country gifted the Statue of Liberty to the US?",
                    "England", "Spain", "France", "Germany",
                    "France"
            ));

        } else if (category.equals("Movies")) {

            list.add(new Questions(
                    "What movie features a character named Forrest Gump?",
                    "Cast Away", "Forrest Gump", "The Green Mile", "Big",
                    "Forrest Gump"
            ));

            list.add(new Questions(
                    "Who directed Jurassic Park?",
                    "James Cameron", "Ridley Scott", "Steven Spielberg", "George Lucas",
                    "Steven Spielberg"
            ));

            list.add(new Questions(
                    "What is the highest-grossing film of all time?",
                    "Avengers: Endgame", "Avatar", "Titanic", "Star Wars",
                    "Avatar"
            ));

            list.add(new Questions(
                    "In The Lion King, what is Simba's father's name?",
                    "Scar", "Mufasa", "Rafiki", "Zazu",
                    "Mufasa"
            ));

            list.add(new Questions(
                    "What year was the first Toy Story released?",
                    "1993", "1995", "1997", "1999",
                    "1995"
            ));

        }

        return list;
    }
}