import javax.swing.SwingUtilities;

/**
 * Entry point for the Finance Tracker application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinanceTrackerGUI app = new FinanceTrackerGUI();
            app.setVisible(true);
        });
    }
}
