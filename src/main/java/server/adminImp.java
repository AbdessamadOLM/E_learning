package server;

import client.Iclient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class adminImp extends UnicastRemoteObject implements Iserver {
    public ArrayList<Iclient> connect_clt ;
    protected adminImp() throws RemoteException {
        connect_clt = new ArrayList<Iclient>();
    }


    @Override
    public synchronized void registerClientInSession(Iclient iclient) throws RemoteException {
        connect_clt.add(iclient);
    }

    @Override
    public synchronized void brodcast(String name,String message) throws RemoteException {
        for (int i = 0 ; i<connect_clt.size();i++){
            connect_clt.get(i).retriveMessage(name,message);
        }
    }

    @Override
    public List<String> getConnectedusers(String name) throws RemoteException {
        List<String> list = new ArrayList<>();
        for (Iclient iclient:connect_clt) {
            if(!(iclient.getname().equals(name))){
                list.add(iclient.getname());
            }
        }
        return list;
    }

    @Override
    public void sendToSpecifique(String name,List<String> list,String mess) throws RemoteException {
        for (Iclient client:connect_clt) {
            for(int i = 0; i<list.size(); i++){
                if(client.getname().equals(list.get(i))){
                    client.retriveMessage(name,mess);
                }
            }
        }
    }

    @Override
    public void diffuserFile(String filename, List<Integer> list,List<String> users,String name) throws RemoteException {
        if(users.isEmpty()){
            for (Iclient iclient : connect_clt){
                if(iclient.getname().equals(name))
                    continue;
                iclient.reciveDiffusedFile(filename,list);
            }
        }else {
            for (int i = 0; i< users.size();i++){
                for (Iclient iclient : connect_clt){
                    if(iclient.getname().equals(name) || !iclient.getname().equals(users.get(i)))
                        continue;
                    iclient.reciveDiffusedFile(filename,list);
                }
            }
        }


    }

    @Override
    public boolean login(String name, String passwd) throws RemoteException {
        DbConnection db = new DbConnection();
        boolean b = false;
        try {
            b = db.login(name,passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }
}
