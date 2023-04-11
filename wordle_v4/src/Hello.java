import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
    String intento(String s, int vidas) throws RemoteException;
}
