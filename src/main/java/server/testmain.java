package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class testmain {
    public static void main(String[] args) {
        try {
            adminImp adminImp = new adminImp();
            LocateRegistry.createRegistry(52000);
            Naming.rebind("rmi://localhost:52000/echo",adminImp);
            System.out.println("server est en écoute");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
