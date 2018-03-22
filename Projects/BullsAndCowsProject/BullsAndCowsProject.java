import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BullsAndCowsProject {

    public static void main(String[] args) throws IOException {
        Path allScoresFilePath = Paths.get("scores.txt");
        List<String> allScores = LoadAllScores(allScoresFilePath);
        DoMenuLoop(allScores);
        SaveAllScores(allScoresFilePath, allScores);
    }

    private static void DoMenuLoop(List<String> allScores) {
        System.out.println("Welcome to the Bulls and Cows game!");
        while (true) {
            System.out.println("Press 1 for instructions, press 2 to see all scores, press 3 to start new game or press 4 for exit.");
            System.out.print("Please enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextLine()) {
                case "1":
                    instructions();
                    break;
                case "2":
                    PrintAllScores(allScores);
                    break;
                case "3":
                    PlayOneGame(allScores);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid command!");
            }
        }
    }
    private static void PlayOneGame(List<String> allScores) {
        String nameAndGuesses = playGame();
        allScores.add(nameAndGuesses);
    }
    private static void PrintAllScores(List<String> allScores) {
        for (String score : allScores)
            System.out.println(score);
    }
    private static void SaveAllScores(Path allScoresFilePath, List<String> allScores) throws IOException {
        Files.write(allScoresFilePath, allScores);
    }
    private static String playGame() {
        int[] SecretNum = generateRandomDigits();
        System.out.println("Secret num = " + Arrays.toString(SecretNum)); // Секретното число
        System.out.print("Please enter a number with no duplicate digits: ");
        int guessesCount = 1;

        Scanner scanner = new Scanner(System.in);

        while (!playingGame(scanner.nextLine(), SecretNum)) {
            System.out.print("Please enter your next guess: ");
            guessesCount++;
        }
        System.out.println("Congratulations! You won after " + guessesCount + " guesses!");

        System.out.print("Please enter your name: ");
        String name = scanner.nextLine();

        return name + " - " + guessesCount + " guesses.";
    }
    private static List<String> LoadAllScores(Path allScoresFilePath) throws IOException {
        if (Files.exists(allScoresFilePath))
            return Files.readAllLines(allScoresFilePath);
        else
            return new ArrayList<String>();
    }
    public static void instructions() {
        System.out.println("The computer will generate a hidden number exactly 4 digits long. ");
        System.out.println("No digits are allowed to be repeated!");
        System.out.println("The number of Bulls - digits correct in the right position.");
        System.out.println("The number of Cows - digits correct but in the wrong position.");
        System.out.println("The game will end if you have four Bulls.");
        System.out.println("Have fun!");
        System.out.println();
    }
    public static int[] generateRandomDigits() {
        int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random generator = new Random();
        for (int i = 0; i < digits.length; i++) {
            int j = generator.nextInt(digits.length);
            int temp = digits[i];
            digits[i] = digits[j];
            digits[j] = temp;
        }
        return Arrays.copyOf(digits, 4);
    }
    public static boolean playingGame(String guess, int[] array) {
        char[] guessed = guess.toCharArray();
        int bullsCount = 0;
        int cowsCount = 0;

        if (guessed.length != 4) {
            System.out.println("Please enter a number exactly 4 digits long! ");
            return false;
        }
        for (int i = 0; i < guessed.length - 1; i++) {
            for (int j = i + 1; j < guessed.length; j++) {
                if (guessed[i] == guessed[j] && (i != j)) {
                    System.out.println("Please enter a number with no duplicate digits! ");
                    return false;
                }
            }
        }
        for (int i = 0; i < guessed.length; i++) {
            int currentGuess = Character.getNumericValue(guessed[i]);
            if (currentGuess < 1 || currentGuess > 9) {
                System.out.println("Please enter a number from the digits 1 to 9, without duplication.");
                return false;
            }
            if (currentGuess == array[i]) {
                bullsCount++;
            } else {
                for (int j = 0; j < 4; j++) {
                    if (currentGuess == array[j])
                        cowsCount++;
                }
            }
        }
        System.out.printf("Your Score is %d Bulls and %d Cows. ", bullsCount, cowsCount);
        return bullsCount == 4;
    }
}