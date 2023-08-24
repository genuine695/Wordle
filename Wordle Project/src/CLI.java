import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The entry point to the CLI version of the game.
 */
public class CLI {
    public static void main(String[] args) {
        Model model = null;
        try {
             model = new Model(args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (model == null) return;

        CLI cli = new CLI(model);
        cli.run();
    }

    private Model model;
    private Scanner input;

    public CLI(Model model) {
        this.model = model;
        this.input = new Scanner(System.in);
    }

    /**
     * Runs a game of Wordle via the command line.
     */
    public void run() {
        System.out.println("WELCOME TO WORDLE (CLI VERSION)");
        if (model.shouldShowWord()) {
            System.out.println("[Debug] Word to guess: " + model.getTargetWord());
        }

        while (model.getRemainingGuesses() > 0) {
            List<List<GuessItem>> guesses = model.getGuesses();

            System.out.println("Guess #" + (guesses.size() + 1));
            System.out.println();
            System.out.println("CHARACTERS:");
            System.out.println("Unguessed: " + model.getAllCharactersWithStatus(null));
            System.out.println("Correct:   " + model.getAllCharactersWithStatus(GuessStatus.CORRECT));
            System.out.println("Misplaced: " + model.getAllCharactersWithStatus(GuessStatus.MISPLACED));
            System.out.println("Incorrect: " + model.getAllCharactersWithStatus(GuessStatus.INCORRECT));

            System.out.println();
            System.out.print("Enter your guess: ");

            String guess = input.nextLine();

            if (model.shouldValidateWord() && !model.isValidGuess(guess)) {
                System.out.println();
                System.out.println("*** INVALID GUESS ***");
                System.out.println();
            } else {
                model.makeGuess(guess);

                if (model.isGuessCorrect(guess)) {
                    System.out.println();
                    System.out.println("CORRECT GUESS: " + guess.toUpperCase());
                    System.out.println("You won the game in " + model.getGuesses().size() + " guesses");
                    System.out.println();
                    break;
                } else {
                    System.out.println();
                    System.out.println("You guessed: " + guess.toUpperCase());
                    System.out.println();
                }
            }
        }

        if (model.getRemainingGuesses() == 0) {
            System.out.println();
            System.out.println("You've run out of guesses. The word was: " + model.getTargetWord());
            System.out.println();
        }
    }
}
