import java.io.IOException;
import java.awt.EventQueue;

/**
 * The entry point to the GUI version of the game.
 */
public class GUI {
    public static void main(String[] args) {
        Model model = null;

        try {
             model = new Model(args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (model == null) return;

        Controller controller = new Controller(model);

        EventQueue.invokeLater(() -> {
            controller.getView().setVisible(true);
        });
    }
}
