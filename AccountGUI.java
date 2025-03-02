// AccountGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;


public class AccountGUI extends JFrame {
    private Account account;           // Remote account object
    private JTextField depositField;   // Input field for deposit amount
    private JTextField withdrawField;  // Input field for withdraw amount
    private JLabel balanceLabel;       // Displays the current balance
    private JTextArea logArea;         // Displays logs and messages

    public AccountGUI(Account account) {
        this.account = account;
        initComponents();
        updateBalance();  // Retrieve and display the initial balance
    }

    private void initComponents() {
        setTitle("Bank Account Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Panel for transaction inputs and buttons
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Deposit Amount:"));
        depositField = new JTextField();
        panel.add(depositField);

        panel.add(new JLabel("Withdraw Amount:"));
        withdrawField = new JTextField();
        panel.add(withdrawField);

        // Button to perform deposit
        JButton depositButton = new JButton("Deposit");
        panel.add(depositButton);
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                depositAction();
            }
        });

        // Button to perform withdrawal
        JButton withdrawButton = new JButton("Withdraw");
        panel.add(withdrawButton);
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                withdrawAction();
            }
        });

        // Button to refresh/check balance
        JButton checkBalanceButton = new JButton("Check Balance");
        panel.add(checkBalanceButton);
        checkBalanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBalance();
            }
        });

        // Label to display current balance
        balanceLabel = new JLabel("Balance: ");
        panel.add(balanceLabel);

        // Log area for displaying transaction messages
        logArea = new JTextArea(5, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Set layout for the main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    // Action to perform deposit
    private void depositAction() {
        try {
            double amount = Double.parseDouble(depositField.getText());
            account.deposit(amount);
            logArea.append("Deposited: " + amount + "\n");
            updateBalance();
        } catch (NumberFormatException ex) {
            logArea.append("Invalid deposit amount.\n");
        } catch (RemoteException ex) {
            logArea.append("Remote error during deposit.\n");
        }
    }

    // Action to perform withdrawal
    private void withdrawAction() {
        try {
            double amount = Double.parseDouble(withdrawField.getText());
            account.withdraw(amount);
            logArea.append("Withdrew: " + amount + "\n");
            updateBalance();
        } catch (NumberFormatException ex) {
            logArea.append("Invalid withdrawal amount.\n");
        } catch (RemoteException ex) {
            logArea.append("Remote error during withdrawal.\n");
        }
    }

    // Method to update the balance display
    private void updateBalance() {
        try {
            double balance = account.getBalance();
            balanceLabel.setText("Balance: " + balance);
            logArea.append("Current balance: " + balance + "\n");
        } catch (RemoteException ex) {
            logArea.append("Remote error during balance check.\n");
        }
    }

    // Main method to launch the GUI client
    public static void main(String[] args) {
        try {
            // Locate the RMI registry (adjust host if needed)
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Account account = (Account) registry.lookup("BankAccount");

            // Launch the GUI on the Event Dispatch Thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new AccountGUI(account).setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error connecting to the remote account:\n" + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
