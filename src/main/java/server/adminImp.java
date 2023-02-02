package server;

import client.Iclient;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class adminImp extends UnicastRemoteObject implements Iserver {
    public ArrayList<Iclient> connect_clt ;
    DbConnection dbConnection = new DbConnection();
    protected adminImp() throws RemoteException {
        connect_clt = new ArrayList<Iclient>();
    }


    @Override
    public synchronized void registerClientInSession(Iclient iclient) throws RemoteException {
        connect_clt.add(iclient);
    }

    @Override
    public synchronized void brodcast(String name,String message) throws RemoteException {
        List<String> clas_student = new ArrayList<>();
        try {
            clas_student = dbConnection.collectStudProf(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0 ; i<connect_clt.size();i++){
            for (int j=0; j<clas_student.size();j++) {
                if(connect_clt.get(i).getname().equals(clas_student.get(j)))
                    connect_clt.get(i).retriveMessage(name, message);
            }
        }

    }

    @Override
    public List<String> getConnectedusers(String name) throws RemoteException {
        List<String> list = new ArrayList<>();
        List<String> clas_student = new ArrayList<>();
        try {
            clas_student = dbConnection.collectStudProf(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Iclient iclient:connect_clt) {
            for (int i=0; i<clas_student.size();i++){
                if(!(iclient.getname().equals(name)) && iclient.getname().equals(clas_student.get(i))){
                    list.add(iclient.getname());
                }
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
                if (client.getname().equals(name)){
                    client.retriveMessage(name,mess);
                }
            }
        }
    }

    @Override
    public void diffuserFile(String filename, List<Integer> list,List<String> users,String name) throws RemoteException {
        List<String> clas_student = new ArrayList<>();
        try {
            clas_student = dbConnection.collectStudProf(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(users.isEmpty()){
            for (Iclient iclient : connect_clt){
                for (int i=0; i<clas_student.size();i++){
                    if(!iclient.getname().equals(name) && iclient.getname().equals(clas_student.get(i)))
                        iclient.reciveDiffusedFile(filename,list);
                }
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
    public boolean login(String name, String passwd, boolean b) throws RemoteException {

        boolean b1 = false;
        try {
           if(b){
               b1 = dbConnection.login(name,passwd,"prof");
           }else
               b1 = dbConnection.login(name,passwd,"personne");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b1;
    }

    @Override
    public void brodcastVue(MouseEvent e, String name, Color c, int size) throws RemoteException {
        List<String> clas_student = new ArrayList<>();
        try {
            clas_student = dbConnection.collectStudProf(name);
        } catch (SQLException ev) {
            ev.printStackTrace();
        }
        for (Iclient iclient : connect_clt){
            for (int i=0; i<clas_student.size();i++){
                if(!iclient.getname().equals(name) && iclient.getname().equals(clas_student.get(i)))
                    iclient.reciveDiffusedVue(e,c,size);
            }
        }
    }

    @Override
    public void clearBoard(String name) throws RemoteException {
        List<String> clas_student = new ArrayList<>();
        try {
            clas_student = dbConnection.collectStudProf(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Iclient iclient : connect_clt){
            for (int i=0; i<clas_student.size();i++){
                if(!iclient.getname().equals(name) && iclient.getname().equals(clas_student.get(i)))
                    iclient.clear();
            }

        }
    }

    @Override
    public void deconnexion(String name) throws RemoteException {
        for (int i = 0 ; i<connect_clt.size(); i++) {
            if (connect_clt.get(i).getname().equals(name))
                connect_clt.remove(i);
        }
    }
}
