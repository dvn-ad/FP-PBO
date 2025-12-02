import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FinanceTrackerGUI extends JFrame {
    private FinanceManager manager;
    private JLabel balanceLabel;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    private JTextField amountField;
    private JTextField descField;
    private JTextField dateField; 
    private JComboBox<String> categoryBox;

    private JComboBox<String> filterType;
    private JComboBox<String> filterCategory;
    private JComboBox<String> filterMonth;
    private JComboBox<String> filterYear;

    private List<Transaction> currentDisplayedTransactions;
    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton addIncomeBtn;
    private JButton addExpenseBtn;
    private String selectedTransactionId;

    public FinanceTrackerGUI() {
        manager = new FinanceManager();
        
        setTitle("BlueJ Finance Tracker");
        setSize(900, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topContainer = new JPanel(new BorderLayout());
        

        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(new Color(60, 179, 113));
        balanceLabel = new JLabel("Total Balance: $0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        balanceLabel.setForeground(Color.WHITE);
        balancePanel.add(balanceLabel);
        topContainer.add(balancePanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        filterType = new JComboBox<>(new String[]{"All", "Income", "Expense"});
        
        String[] allCategories = {"All", "Salary", "Food", "Transport", "Utilities", "Entertainment", "Other"};
        filterCategory = new JComboBox<>(allCategories);

        String[] months = {"All", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        filterMonth = new JComboBox<>(months);


        String[] years = new String[11];
        years[0] = "All";
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=1; i<11; i++) {
            years[i] = String.valueOf(currentYear - (i-1));
        }
        filterYear = new JComboBox<>(years);

        JButton applyFilterBtn = new JButton("Apply Filter");
        applyFilterBtn.addActionListener(e -> refreshTable());

        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(filterType);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(filterCategory);
        filterPanel.add(new JLabel("Month:"));
        filterPanel.add(filterMonth);
        filterPanel.add(new JLabel("Year:"));
        filterPanel.add(filterYear);
        filterPanel.add(applyFilterBtn);

        topContainer.add(filterPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);


        String[] columnNames = {"Date", "Type", "Category", "Description", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && transactionTable.getSelectedRow() != -1) {
                int row = transactionTable.getSelectedRow();
                if (row < currentDisplayedTransactions.size()) {
                    Transaction t = currentDisplayedTransactions.get(row);
                    selectedTransactionId = t.getId();
                    
                    amountField.setText(String.valueOf(t.getAmount()));
                    descField.setText(t.getDescription());
                    categoryBox.setSelectedItem(t.getCategory());
                    dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(t.getDate()));
                    
                    updateBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                    addIncomeBtn.setEnabled(false);
                    addExpenseBtn.setEnabled(false);
                }
            }
        });
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3, 1));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Add New Transaction"));

        JPanel inputPanel = new JPanel(new FlowLayout());
        
        amountField = new JTextField(8);
        descField = new JTextField(12);
        
        String[] inputCategories = {"Salary", "Food", "Transport", "Utilities", "Entertainment", "Other"};
        categoryBox = new JComboBox<>(inputCategories);

        dateField = new JTextField(8);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        inputPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Desc:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Cat:"));
        inputPanel.add(categoryBox);
        
        bottomPanel.add(inputPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addIncomeBtn = new JButton("Add Income (+)");
        addExpenseBtn = new JButton("Add Expense (-)");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        
        addIncomeBtn.setBackground(new Color(144, 238, 144));
        addExpenseBtn.setBackground(new Color(255, 182, 193));
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        addIncomeBtn.addActionListener(e -> addTransaction(true));
        addExpenseBtn.addActionListener(e -> addTransaction(false));
        updateBtn.addActionListener(e -> updateTransaction());
        deleteBtn.addActionListener(e -> deleteTransaction());
        clearBtn.addActionListener(e -> clearSelection());

        buttonPanel.add(addIncomeBtn);
        buttonPanel.add(addExpenseBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
        updateBalance();
    }

    private void addTransaction(boolean isIncome) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String desc = descField.getText();
            String category = (String) categoryBox.getSelectedItem();
            String dateStr = dateField.getText();

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description.");
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                 JOptionPane.showMessageDialog(this, "Invalid Date Format. Use yyyy-MM-dd");
                 return;
            }

            Transaction t;
            if (isIncome) {
                t = new Income(amount, desc, category, date);
            } else {
                t = new Expense(amount, desc, category, date);
            }

            manager.addTransaction(t);
            refreshTable();
            updateBalance();
            
            amountField.setText("");
            descField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount. Please enter a number.");
        }
    }

    private void updateTransaction() {
        if (selectedTransactionId == null) return;
        
        try {
            double amount = Double.parseDouble(amountField.getText());
            String desc = descField.getText();
            String category = (String) categoryBox.getSelectedItem();
            String dateStr = dateField.getText();
            
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                 JOptionPane.showMessageDialog(this, "Invalid Date Format. Use yyyy-MM-dd");
                 return;
            }
            
            Transaction existingT = null;
            for(Transaction t : currentDisplayedTransactions) {
                if(t.getId().equals(selectedTransactionId)) {
                    existingT = t;
                    break;
                }
            }
            
            if(existingT != null) {
                Transaction newT;
                if (existingT instanceof Income) {
                    newT = new Income(amount, desc, category, date);
                } else {
                    newT = new Expense(amount, desc, category, date);
                }
                
                manager.updateTransaction(selectedTransactionId, newT);
                refreshTable();
                updateBalance();
                clearSelection();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount. Please enter a number.");
        }
    }

    private void deleteTransaction() {
        if (selectedTransactionId != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this transaction?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteTransaction(selectedTransactionId);
                refreshTable();
                updateBalance();
                clearSelection();
            }
        }
    }

    private void clearSelection() {
        transactionTable.clearSelection();
        selectedTransactionId = null;
        amountField.setText("");
        descField.setText("");
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        addIncomeBtn.setEnabled(true);
        addExpenseBtn.setEnabled(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0); 
        
        String type = (String) filterType.getSelectedItem();
        String category = (String) filterCategory.getSelectedItem();
        
        int month = filterMonth.getSelectedIndex() - 1;
        
        int year = -1;
        String yearStr = (String) filterYear.getSelectedItem();
        if (!"All".equals(yearStr)) {
            year = Integer.parseInt(yearStr);
        }

        currentDisplayedTransactions = manager.getFilteredTransactions(type, category, month, year);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Transaction t : currentDisplayedTransactions) {
            Object[] row = {
                sdf.format(t.getDate()),
                t.getType(),
                t.getCategory(),
                t.getDescription(),
                String.format("$%.2f", t.getAmount())
            };
            tableModel.addRow(row);
        }
    }

    private void updateBalance() {
        double bal = manager.getBalance();
        balanceLabel.setText(String.format("Total Balance: $%.2f", bal));
    }
}