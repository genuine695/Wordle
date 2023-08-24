import javax.swing.*;

/**
 * The Controller in the GUI version of the game. This class sends messages
 * from the view to the model and implements some logic specific to the GUI
 * version of the game.
 */
public class Controller {
    private Model model;
    private RootView view;

    public JFrame getView() {
        return view;
    }

    public Controller(Model model) {
        this.model = model;
        this.view = new RootView(this::startNewGame, this::entryReceived);
        model.addObserver(view);
        
        if (model.shouldShowWord()) {
            System.out.println("Target word = " + model.getTargetWord());
        }
    }

    private void startNewGame() {
        model.reset();

        if (model.shouldShowWord()) {
            System.out.println("Target word = " + model.getTargetWord());
        }
    }

    private void entryReceived(Entry entry) {
        assert entry != null : "Entry must be non-null";

        System.out.println(entry);

        if (model.isGameFinished()) {
            return;
        }

        String guessInProgress = model.getGuessInProgress();

        if (entry == Entry.BACKSPACE && !guessInProgress.isEmpty()) {
            guessInProgress = guessInProgress.substring(0, guessInProgress.length() - 1);
        }
        else if (entry == Entry.ENTER && guessInProgress.length() == 5) {
            view.showWarning(null);

            if (model.shouldValidateWord()) {
                if (model.isValidGuess(guessInProgress)) {
                    model.makeGuess(guessInProgress);
                } else {
                    view.showWarning("Word not in word list.");
                }
            } else {
                model.makeGuess(guessInProgress);
            }

            guessInProgress = "";
        }
        else if (entry != Entry.ENTER && entry != Entry.BACKSPACE) {
            guessInProgress += entry.name();
        }

        model.setGuessInProgress(guessInProgress);
    }
}
