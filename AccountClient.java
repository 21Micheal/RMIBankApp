// AccountClient.java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AccountClient {
    public static void main(String[] args) {
        try {
            // Locate the registry on localhost and default port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Lookup the remote object by its bound name
            Account account = (Account) registry.lookup("BankAccount");
            
            // Perform transactions on the remote Account
            account.deposit(1000);
            account.withdraw(200);
            System.out.println("Final balance: " + account.getBalance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
