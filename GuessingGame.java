import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GuessingGame {

    static String[] highscoreNames = {"---", "---", "---"};
    static int[] highscorePoints = {0, 0, 0};

    static int lastScore = 0;
    static int personalBest = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        loadHighscores();

        System.out.println("Welcome to the Guessing Game!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        boolean running = true;

        while (running) {
            int maxNumber = 50;
            int maxAttempts = 5;
            String difficultyName = "Medium";
            int basePointsPerTry = 20;
            int difficultyBonus = 0;

            System.out.println("\nChoose difficulty:");
            System.out.println("1 = Easy (1-30, 7 attempts)");
            System.out.println("2 = Medium (1-50, 5 attempts)");
            System.out.println("3 = Hard (1-100, 4 attempts)");
            System.out.print("Your choice: ");

            int difficulty = 2;

            if (scanner.hasNextInt()) {
                difficulty = scanner.nextInt();
            } else {
                scanner.next();
            }

            if (difficulty == 1) {
                maxNumber = 30;
                maxAttempts = 7;
                difficultyName = "Easy";
                basePointsPerTry = 10;
            } else if (difficulty == 3) {
                maxNumber = 100;
                maxAttempts = 4;
                difficultyName = "Hard";
                basePointsPerTry = 25;
                difficultyBonus = 30;
            }

            int numberToGuess = random.nextInt(maxNumber) + 1;
            int attempts = 0;
            boolean guessedCorrectly = false;

            long startTime = System.currentTimeMillis();

            System.out.println("\nHello, " + playerName + "!");
            System.out.println("Difficulty: " + difficultyName);
            System.out.println("Guess a number between 1 and " + maxNumber);

            while (attempts < maxAttempts) {
                System.out.print("Enter your guess: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a number.");
                    scanner.next();
                    continue;
                }

                int guess = scanner.nextInt();
                attempts++;

                int diff = Math.abs(guess - numberToGuess);

                if (guess < numberToGuess) {
                    System.out.println("Too low!");
                } else if (guess > numberToGuess) {
                    System.out.println("Too high!");
                } else {
                    guessedCorrectly = true;

                    long time = (System.currentTimeMillis() - startTime) / 1000;

                    int points = (maxAttempts - attempts + 1) * basePointsPerTry;

                    if (time <= 5) points += 50;
                    else if (time <= 10) points += 30;
                    else if (time <= 15) points += 15;

                    points += difficultyBonus;

                    lastScore = points;

                    if (points > personalBest) {
                        personalBest = points;
                    }

                    updateHighscores(playerName, points);
                    saveHighscores();

                    System.out.println("Correct!");
                    System.out.println("Points: " + points);
                    System.out.println("Time: " + time + " seconds");

                    break;
                }

                if (diff <= 2) System.out.println("🔥 Very close!");
                else if (diff <= 5) System.out.println("🙂 Close!");
                else System.out.println("❄️ Far away!");

                System.out.println("Attempts left: " + (maxAttempts - attempts));
            }

            if (!guessedCorrectly) {
                System.out.println("Game Over! Number was: " + numberToGuess);
            }

            scanner.nextLine();

            boolean valid = false;
            while (!valid) {
                System.out.println("\nMenu:");
                System.out.println("continue = play again");
                System.out.println("score = top 3 highscores");
                System.out.println("my = my highscore");
                System.out.println("exit = quit");

                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("continue")) {
                    valid = true;
                } else if (choice.equalsIgnoreCase("score")) {
                    showHighscores();
                } else if (choice.equalsIgnoreCase("my")) {
                    System.out.println(playerName + " stats:");
                    System.out.println("Last score: " + lastScore);
                    System.out.println("Personal best: " + personalBest);
                } else if (choice.equalsIgnoreCase("exit")) {
                    valid = true;
                    running = false;
                } else {
                    System.out.println("Invalid input.");
                }
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    public static void updateHighscores(String name, int points) {
        for (int i = 0; i < 3; i++) {
            if (points > highscorePoints[i]) {
                for (int j = 2; j > i; j--) {
                    highscorePoints[j] = highscorePoints[j - 1];
                    highscoreNames[j] = highscoreNames[j - 1];
                }
                highscorePoints[i] = points;
                highscoreNames[i] = name;
                break;
            }
        }
    }

    public static void showHighscores() {
        System.out.println("\nTop 3 Highscores:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ". " + highscoreNames[i] + " - " + highscorePoints[i]);
        }
    }

    public static void loadHighscores() {
        try {
            File file = new File("highscores.txt");
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                int i = 0;
                while (sc.hasNextLine() && i < 3) {
                    String[] parts = sc.nextLine().split(",");
                    highscoreNames[i] = parts[0];
                    highscorePoints[i] = Integer.parseInt(parts[1]);
                    i++;
                }
                sc.close();
            }
        } catch (Exception e) {
            System.out.println("Load error.");
        }
    }

    public static void saveHighscores() {
        try {
            FileWriter writer = new FileWriter("highscores.txt");
            for (int i = 0; i < 3; i++) {
                writer.write(highscoreNames[i] + "," + highscorePoints[i] + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Save error.");
        }
    }
}