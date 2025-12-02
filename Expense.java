import java.util.Date;


public class Expense extends Transaction {
    public Expense(double amount, String description, String category, Date date) {
        super(amount, description, category, date);
    }

    @Override
    public String getType() {
        return "Expense";
    }

    @Override
    public double getSignedAmount() {
        return -getAmount();
    }
}
