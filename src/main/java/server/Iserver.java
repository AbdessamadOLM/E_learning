package server;

import client.Iclient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Iserver extends Remote {
    public void registerClientInSession(Iclient iclient) throws RemoteException;
    public void  brodcast(String name,String message) throws RemoteException;
    public List getConnectedusers(String name)throws RemoteException;
    public void sendToSpecifique(String name ,List<String> list,String mess) throws RemoteException;
    public void diffuserFile(String filename,List<Integer> list,List<String> users,String name) throws RemoteException;
    public boolean login(String name,String passwd) throws RemoteException;
}
