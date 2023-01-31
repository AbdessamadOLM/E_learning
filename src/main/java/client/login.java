package client;

import server.Iserver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class login extends JFrame{
    private JPanel loginFr;
    private JTextField usernamefiled;
    private JButton loginButton;
    private JTextField passwordInput;
    static Iserver iserver;

    public login(){
        setContentPane(loginFr);
        this.setLocationRelativeTo(null);
        setTitle("Bonjour chère étudiant");
        setSize(300,150);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
        int port = 52000;
        String url = "rmi://localhost:"+port+"/echo";
        try {
            iserver = (Iserver) Naming.lookup(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!usernamefiled.equals("")){
                    try {
                       if(iserver.login(usernamefiled.getText(),passwordInput.getText())){
                           new Etudiantview(usernamefiled.getText());
                           dispose();
                       }else{
                           JOptionPane.showMessageDialog(new JFrame(),"username or password is wrong");
                           usernamefiled.setText("");
                           passwordInput.setText("");
                       }

                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }

                }else
                    JOptionPane.showMessageDialog(new JFrame(),"enter your name and mot de pass");
            }
        });
    }

    public static void main(String[] args) {
        login login = new login();
    }
}
