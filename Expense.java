import java.util.Date;

/**
 * Represents money spent.
 */
public class Expense extends Transaction {
    public Expense(double amount, String description, String category, Date date) {
        super(amount, description, category, date);
    }

    @Override
    public String getType() {
        return "Expense";
    }
}
