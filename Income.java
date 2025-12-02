import java.util.Date;

/**
 * Represents money earned.
 */
public class Income extends Transaction {
    public Income(double amount, String description, String category, Date date) {
        super(amount, description, category, date);
    }

    @Override
    public String getType() {
        return "Income";
    }
}
