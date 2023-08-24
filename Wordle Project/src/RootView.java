import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

/**
 * A view containing one horizontal row of the keyboard.
 */
class KeyboardRowView extends JPanel {
    Map<Character, JButton> buttons = new HashMap<>();

    public KeyboardRowView(char[] chars, boolean hasAux, Consumer<Entry> onClicked) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        if (hasAux) {
            JButton button = new JButton("Enter");
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            button.setMinimumSize(new Dimension(70, 80));
            button.setMaximumSize(new Dimension(70, 80));
            button.addActionListener(e -> onClicked.accept(Entry.ENTER));
            add(button);
        }

        for (char c: chars) {
            JButton button = new JButton("" + c);
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            button.setMinimumSize(new Dimension(50, 80));
            button.setMaximumSize(new Dimension(50, 80));
            button.addActionListener(e -> onClicked.accept(Entry.fromCharacter(c)));
            buttons.put(c, button);
            add(button);
        }

        if (hasAux) {
            JButton button = new JButton("<=");
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            button.setMinimumSize(new Dimension(50, 80));
            button.setMaximumSize(new Dimension(50, 80));
            button.addActionListener(e -> onClicked.accept(Entry.BACKSPACE));
            add(button);
        }
    }

    public void update(Map<Character, GuessStatus> characterStatuses) {
        for (char character: buttons.keySet()) {
            GuessStatus status = characterStatuses.get(character);
            JButton button = buttons.get(character);
            button.setBackground(RootView.colorForStatus(status));
            button.setForeground(RootView.textColorForStatus(status));
        }
    }
}

/**
 * A view containing the virtual keyboard.
 */
class KeyboardView extends JPanel {
    List<KeyboardRowView> rows = new ArrayList<>();

    public KeyboardView(Consumer<Entry> onKeyClicked) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        rows.add(new KeyboardRowView("QWERTYUIOP".toCharArray(), false, onKeyClicked));
        rows.add(new KeyboardRowView("ASDFGHJKL".toCharArray(), false, onKeyClicked));
        rows.add(new KeyboardRowView("ZXCVBNM".toCharArray(), true, onKeyClicked));

        for (KeyboardRowView row: rows) {
            add(row);
        }
    }

    public void update(Map<Character, GuessStatus> characterStatuses) {
        for (KeyboardRowView row: rows) {
            row.update(characterStatuses);
        }
    }
}

/**
 * A view containing the 5x6 grid of letters displaying previous
 * and current guesses.
 */
class GridView extends JPanel {
    List<JLabel> labels = new ArrayList<>();

    public GridView() {
        setLayout(new GridLayout(0, 5, 5, 5));

        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();

        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();

        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();

        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();

        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();

        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
        makeLabel();
    }

    public void update(List<List<GuessItem>> guesses, String guessInProgress) {
        for (JLabel label: labels) {
            label.setText("-");
            label.setBackground(RootView.colorForStatus(null));
            label.setForeground(RootView.textColorForStatus(null));
        }

        int i = 0;
        for (List<GuessItem> guess: guesses) {
            for (GuessItem item: guess) {
                labels.get(i).setText(String.valueOf(item.value));
                labels.get(i).setBackground(RootView.colorForStatus(item.status));
                labels.get(i).setForeground(RootView.textColorForStatus(item.status));
                i++;
            }
        }

        for (char c: guessInProgress.toCharArray()) {
            labels.get(i).setText(String.valueOf(c));
            labels.get(i).setBackground(RootView.colorForStatus(null));
            i++;
        }
    }

    private void makeLabel() {
        JLabel label = new JLabel("-", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(RootView.colorForStatus(null));
        label.setForeground(RootView.textColorForStatus(null));
        label.setFont(new Font("Helvetica", Font.BOLD, 20));
        add(label);

        labels.add(label);
    }
}

/**
 * A command that may be sent from the view to the controller originating from
 * the virtual or physical keyboard.
 */
enum Entry {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, ENTER, BACKSPACE;

    static Entry fromKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) return ENTER;
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) return BACKSPACE;
        if (Character.isAlphabetic(e.getKeyChar())) {
            return fromCharacter(e.getKeyChar());
        }

        return null;
    }

    static Entry fromCharacter(char c) {
        return valueOf(String.valueOf(Character.toUpperCase(c)));
    }
}

/**
 * The main view that contains all the components of the GUI version of Wordle.
 */
public class RootView extends JFrame implements Observer {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 60 * 6 + 300;

    private GridView grid = new GridView();
    private JButton newGameButton = new JButton("New Game");
    private KeyboardView keyboard;
    private JLabel warningLabel;

    public static Color colorForStatus(GuessStatus status) {
        if (status == null) return Color.lightGray;

        switch (status) {
            case CORRECT: return Color.green;
            case MISPLACED: return Color.yellow;
            case INCORRECT: return Color.darkGray;
            default: return Color.lightGray;
        }
    }

    public static Color textColorForStatus(GuessStatus status) {
        return status == GuessStatus.INCORRECT ? Color.white : Color.black;
    }

    public RootView(Runnable onNewGameClicked, Consumer<Entry> onEntryChosen) {
        setTitle("WORDLE");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        newGameButton.addActionListener((e) -> {
            requestFocusInWindow();
            onNewGameClicked.run();
        });
        newGameButton.setEnabled(false);
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(newGameButton);

        warningLabel = new JLabel("");
        warningLabel.setForeground(Color.red);
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(warningLabel);

        grid.setSize(60*5, 60*6);
        grid.setMaximumSize(new Dimension(60 * 5, 60 * 6));
        add(grid);

        this.keyboard = new KeyboardView((e) -> {
            requestFocusInWindow(); // Stop the button stealing focus from the window and stopping keyboard entry.
            onEntryChosen.accept(e);
        });
        add(keyboard);

        setFocusable(true);
        requestFocusInWindow();
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                onEntryChosen.accept(Entry.fromKeyEvent(keyEvent));
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        if (!(observable instanceof Model)) {
            System.out.println("[WARNING] update of RootView with non-Model observable");
            return;
        }

        System.out.println("[RootView] update");

        Model model = (Model)observable;
        grid.update(model.getGuesses(), model.getGuessInProgress());
        newGameButton.setEnabled(!model.getGuesses().isEmpty());
        keyboard.update(model.getAllCharacterStatuses());
    }

    public void showWarning(String warning) {
        warningLabel.setText(warning == null ? "" : warning);
    }
}