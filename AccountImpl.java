// AccountImpl.java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AccountImpl extends UnicastRemoteObject implements Account {
    private double balance;

    // Constructor must throw RemoteException
    protected AccountImpl() throws RemoteException {
        super();
        balance = 0;
    }

    @Override
    public synchronized void deposit(double amount) throws RemoteException {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount + ". New balance: " + balance);
        }
    }

    @Override
    public synchronized void withdraw(double amount) throws RemoteException {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Withdrew: " + amount + ". New balance: " + balance);
        } else {
            System.out.println("Insufficient funds for withdrawal of: " + amount);
        }
    }

    @Override
    public synchronized double getBalance() throws RemoteException {
        return balance;
    }
}
