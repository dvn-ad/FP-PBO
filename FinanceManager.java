import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceManager {
    private ArrayList<Transaction> transactions;
    private final String DATA_FILE = "finance_data.ser";

    public FinanceManager() {
        transactions = new ArrayList<>();
        loadData();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        saveData();
    }

    public void deleteTransaction(String id) {
        transactions.removeIf(t -> t.getId().equals(id));
        saveData();
    }

    public void updateTransaction(String id, Transaction newTransaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(id)) {
                transactions.set(i, newTransaction);
                saveData();
                return;
            }
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getFilteredTransactions(String type, String category, int month, int year) {
        List<Transaction> filtered = new ArrayList<>();
        
        for (Transaction t : transactions) {
            boolean matchesType = type.equals("All") || t.getType().equalsIgnoreCase(type);
            boolean matchesCategory = category.equals("All") || t.getCategory().equalsIgnoreCase(category);
        
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(t.getDate());
            int tMonth = cal.get(java.util.Calendar.MONTH); // 0-11
            int tYear = cal.get(java.util.Calendar.YEAR);

            boolean matchesMonth = (month == -1) || (tMonth == month);
            boolean matchesYear = (year == -1) || (tYear == year);

            if (matchesType && matchesCategory && matchesMonth && matchesYear) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            balance += t.getSignedAmount();
        }
        return balance;
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(transactions);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                transactions = (ArrayList<Transaction>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
                transactions = new ArrayList<>();
            }
        }
    }
}
