/**
 * Represents a single character in a single guess, i.e. a cell in the grid.
 */
final class GuessItem {
    /**
     * One character of the guess.
     */
    public final char value;

    /**
     * The status of the value character in the guess.
     */
    public final GuessStatus status;

    public GuessItem(char value, GuessStatus status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        return "GuessItem(" + value + ", " + status + ")";
    }
}