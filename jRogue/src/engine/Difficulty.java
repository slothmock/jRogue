package engine;

public class Difficulty {
    public static final String EASY_DIFFICULTY = "Easy";
    public static final String NORMAL_DIFFICULTY = "Normal";

    public static String currentDifficulty = EASY_DIFFICULTY;

    public static String getDifficulty() {
        return currentDifficulty;
    }

    public static String setDifficulty() {
        if (currentDifficulty == EASY_DIFFICULTY) {
            return currentDifficulty = NORMAL_DIFFICULTY;
        } else {
            return currentDifficulty = EASY_DIFFICULTY;
        }
    }
}
