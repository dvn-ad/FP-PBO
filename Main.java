import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinanceTrackerGUI app = new FinanceTrackerGUI();
            app.setVisible(true);
        });
    }
}
