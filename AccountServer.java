// AccountServer.java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;

public class AccountServer {
    public static void main(String[] args) {
        try {
            // Create an Account object
            Account account = new AccountImpl();
            
            // Create an RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Bind the remote object to the registry with the name "BankAccount"
            registry.bind("BankAccount", account);
            
            System.out.println("Bank Account Server is running...");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
