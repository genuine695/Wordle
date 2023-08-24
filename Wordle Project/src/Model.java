import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/**
 * Contains the logic for a game of Wordle. Subscribe to this model to receive
 * updates when the state of the game changes.
 */
public class Model extends Observable {
    private static final String TARGET_POOL_FILENAME = "common.txt";
    private static final String VALID_POOL_FILENAME = "words.txt";
    private static final String FIXED_TARGET_WORD = "AUDIO";
    private static final int GUESSES_ALLOWED = 6;
    private static final List<Character> CHARACTER_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().mapToObj(i -> (char)i).collect(Collectors.toList());

    private String targetWord = FIXED_TARGET_WORD; // an uppercase word
    private Set<String> validWords = new HashSet<>();
    private List<String> targetWords = new ArrayList<>();
    private List<List<GuessItem>> guesses = new ArrayList<>();
    private String guessInProgress = "";
    private Boolean hasWon = null;

    private boolean flag_randomise;
    private boolean flag_showWord;
    private boolean flag_validateWord;

    public String getTargetWord() {
        return targetWord;
    }

    public boolean shouldShowWord() {
        return flag_showWord;
    }

    public boolean shouldValidateWord() {
        return flag_showWord;
    }

    public int getRemainingGuesses() {
        return GUESSES_ALLOWED - guesses.size();
    }

    public List<List<GuessItem>> getGuesses() {
        return guesses;
    }

    public String getGuessInProgress() {
        return guessInProgress;
    }

    public boolean isGameFinished() {
        return hasWon != null;
    }

    public void reset() {
        guesses.clear();
        guessInProgress = "";
        hasWon = null;

        if (flag_randomise) {
            targetWord = targetWords.get(new Random().nextInt(targetWords.size())).toUpperCase();
        }

        setChanged();
        notifyObservers();
    }

    public void setGuessInProgress(String guess) {
        assert guess != null : "Guess must be non-null";
        assert guess.length() <= 5 : "Guesses cannot be larger than 5 characters";
        assert guess.trim().matches("[a-zA-Z]+") : "Guess must only contain alphabetic characters";

        this.guessInProgress = guess;
        setChanged();
        notifyObservers();
    }

    public List<Character> getAllCharactersWithStatus(GuessStatus status) {
        List<GuessItem> allGuesses = guesses.stream().flatMap(Collection::stream).collect(Collectors.toList());

        if (status == null) {
            return CHARACTER_POOL.stream()
                .filter(c -> allGuesses.stream()
                .noneMatch(g -> g.value == c))
                .collect(Collectors.toList());
        }

        return allGuesses.stream()
            .filter(g -> g.status == status)
            .map(g -> g.value)
            .distinct()
            .collect(Collectors.toList());
    }

    public Map<Character, GuessStatus> getAllCharacterStatuses() {
        List<GuessItem> allGuesses = guesses.stream().flatMap(Collection::stream).collect(Collectors.toList());
        Map<Character, GuessStatus> result = new HashMap<>();

        for (GuessItem guess: allGuesses) {
            if (!result.containsKey(guess.value)) {
                result.put(guess.value, guess.status);
            }
            else if (result.get(guess.value).compareTo(guess.status) > 0) {
                result.put(guess.value, guess.status);
            }
        }

        for (Character c: CHARACTER_POOL) {
            if (!result.containsKey(c)) {
                result.put(c, null);
            }
        }

        return result;
    }

    // -r [--random] -s [--show] -v [--validate]
    public Model(String[] commandLineArgs) throws IOException {
        // Interpret the command line flags
        List<String> args = Arrays.asList(commandLineArgs);
        this.flag_randomise = args.contains("-r");
        this.flag_showWord = args.contains("-s");
        this.flag_validateWord = args.contains("-v");

        // Load the possible target words
        BufferedReader reader = new BufferedReader(new FileReader(Model.TARGET_POOL_FILENAME));
        String line;
        while ((line = reader.readLine()) != null) {
            targetWords.add(line.trim());
        }

        if (this.flag_randomise) {
            this.targetWord = targetWords.get(new Random().nextInt(targetWords.size())).toUpperCase();
        }

        if (this.flag_validateWord) {
            // Load the valid English words
            reader = new BufferedReader(new FileReader(Model.VALID_POOL_FILENAME));
            while ((line = reader.readLine()) != null) {
                validWords.add(line.trim());
            }

            validWords.addAll(targetWords);
        }
    }

    public boolean isValidGuess(String guess) {
        assert guess != null : "Guess must be non-null";
        assert guess.trim().length() == 5 : "Guess must be of length 5";
        assert guess.trim().matches("[a-zA-Z]+") : "Guess must only contain alphabetic characters";

        return validWords.contains(guess.toLowerCase());
    }

    public boolean isGuessCorrect(String guess) {
        assert guess != null : "Guess must be non-null";
        assert guess.trim().length() == 5 : "Guess must be of length 5";
        assert guess.trim().matches("[a-zA-Z]+") : "Guess must only contain alphabetic characters";

        return guess.toUpperCase().equals(targetWord);
    }

    public void makeGuess(String guess) {
        assert guess != null : "Guess must be non-null";
        assert guess.trim().length() == 5 : "Guess must be of length 5";
        assert guess.trim().matches("[a-zA-Z]+") : "Guess must only contain alphabetic characters";

        guesses.add(generateGuessData(guess.toUpperCase()));

        if (guess.toUpperCase().equals(targetWord)) {
            hasWon = true;
        }
        else if (guesses.size() == GUESSES_ALLOWED) {
            hasWon = false;
        }

        setChanged();
        notifyObservers();
    }

    // =============== PRIVATE ==================

    public List<GuessItem> generateGuessData(String guess) {
        assert guess != null : "Guess must be non-null";
        assert guess.trim().length() == 5 : "Guess must be of length 5";
        assert guess.trim().matches("[a-zA-Z]+") : "Guess must only contain alphabetic characters";

        List<GuessItem> items = new ArrayList<>();

        for (int i = 0; i < targetWord.length(); i++) {
            GuessItem item;

            if (targetWord.charAt(i) == guess.charAt(i)) {
                item = new GuessItem(guess.charAt(i), GuessStatus.CORRECT);
            }
            else if (targetWord.chars().boxed().collect(Collectors.toSet()).contains((int)guess.charAt(i))) {
                item = new GuessItem(guess.charAt(i), GuessStatus.MISPLACED);
            }
            else {
                item = new GuessItem(guess.charAt(i), GuessStatus.INCORRECT);
            }

            items.add(item);
        }

        return items;
    }
}
