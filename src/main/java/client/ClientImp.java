package client;

import server.Iserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class ClientImp extends UnicastRemoteObject implements Iclient{
    public  Iserver chatserver;
    public String name;
    public  JTextField input;
    public JTextArea output;
    public JPanel paneFile ;
    protected ClientImp(String name , Iserver server,JTextArea output,JTextField input,JPanel JpaneFile) throws RemoteException {
        this.name = name;
        this.chatserver = server;
        this.input = input;
        this.output = output;
        this.paneFile = JpaneFile;
        chatserver.registerClientInSession(this);
    }

    @Override
    public void retriveMessage(String name,String s) throws RemoteException {
        output.setText(output.getText()+"\n"+name+" > "+s);
    }

    @Override
    public String getname() throws RemoteException {
        return name;
    }

    @Override
    public void sendMessage() throws RemoteException {
        chatserver.brodcast(name,input.getText());
    }

    @Override
    public void reciveDiffusedFile(String filename, List<Integer> integers) throws RemoteException {
        JLabel label = new JLabel("<HTML><U><font size=\"4\" color=\"#365899\">" + filename + "</font></U></HTML>");
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    FileOutputStream out;
                    String separator;
                    separator = "\\";
                    out = new FileOutputStream(System.getProperty("user.home") +separator+"Downloads"+ separator + filename);
                    String[] extension = filename.split("\\.");
                    for (int i = 0; i<integers.size(); i++) {
                        int cc = integers.get(i);
                        if(extension[extension.length - 1].equals("txt")||
                                extension[extension.length - 1].equals("java")||
                                extension[extension.length - 1].equals("php")||
                                extension[extension.length - 1].equals("c")||
                                extension[extension.length - 1].equals("cpp")||
                                extension[extension.length - 1].equals("xml")
                        )
                            out.write((char)cc);
                        else{
                            out.write((byte)cc);
                        }
                    }
                    out.flush();
                    out.close();
                    JOptionPane.showMessageDialog(new JFrame(),"your file saved at " + System.getProperty("user.home") +separator+"Downloads"+ separator + filename,"File Saved",JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });
        paneFile.add(label);
        paneFile.repaint();
        paneFile.revalidate();
    }

}
