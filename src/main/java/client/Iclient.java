package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Iclient extends Remote {
    public void retriveMessage(String name,String s) throws RemoteException;
    public String getname() throws RemoteException;
    public void sendMessage() throws RemoteException;
    public void reciveDiffusedFile(String filename, List<Integer> integers) throws RemoteException;

}
