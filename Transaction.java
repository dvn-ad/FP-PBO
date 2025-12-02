import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Transaction implements Serializable {
    private String id;
    private double amount;
    private String description;
    private Date date;
    private String category;

    public Transaction(double amount, String description, String category, Date date) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public abstract String getType();
    public abstract double getSignedAmount();
    
    @Override
    public String toString() {
        return String.format("%s: $%.2f (%s) - %s", getType(), amount, category, description);
    }
}
