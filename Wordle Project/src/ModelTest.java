import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the Model class.
 */
class ModelTest {
    /**
     * Ensures that the model only allows guesses of valid English words.
     * This assumes that the program flag has been set to validate words.
     * @throws IOException
     */
    @Test
    public void testModelValidatesWord() throws IOException {
        Model model = new Model(new String[] { "-v" });
        assertTrue(model.isValidGuess("AUDIO"));
        assertFalse(model.isValidGuess("AAAAA"));
    }

    /**
     * Ensures that the model correctly reports that the game is finished
     * when the correct word has been guessed.
     * @throws IOException
     */
    @Test
    public void testModelReportsWhenFinished() throws IOException {
        // Default word is "AUDIO"
        // isGameFinished() should become true once we guess the correct word

        Model model = new Model(new String[] {  });
        assertFalse(model.isGameFinished());
        model.makeGuess("SPADE");
        assertFalse(model.isGameFinished());
        model.makeGuess("AUDIO");
        assertTrue(model.isGameFinished());
    }

    /**
     * Ensures that the model correctly calculates the hints (tile colours)
     * for a given guess).
     * @throws IOException
     */
    @Test
    public void testGeneratesCorrectHintsForGuess() throws IOException {
        // Default word is "AUDIO"

        Model model = new Model(new String[] {  });
        List<GuessItem> guessItems = model.generateGuessData("ATONE");
        assertEquals(guessItems.get(0).status, GuessStatus.CORRECT);
        assertEquals(guessItems.get(1).status, GuessStatus.INCORRECT);
        assertEquals(guessItems.get(2).status, GuessStatus.MISPLACED);
        assertEquals(guessItems.get(3).status, GuessStatus.INCORRECT);
        assertEquals(guessItems.get(4).status, GuessStatus.INCORRECT);
    }
}