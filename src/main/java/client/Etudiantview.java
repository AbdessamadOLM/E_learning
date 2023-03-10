package client;

import server.Iserver;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class Etudiantview extends JFrame {

    public String name ;
    public Iserver iserver;
    public ClientImp client;
    public Color color;
    public int size = 10;
    public DefaultListModel model = new DefaultListModel();
    public List<String> listeUser = new ArrayList<>() ;
    public Etudiantview(String name) throws RemoteException {
           lancerComponent();
           etudiantName.setText(name);
           this.name = name;
           iserver = login.iserver;
           try{
               client = new ClientImp(name,iserver,output,input,sharedFile,whiteboard) ;
           }catch (Exception e){
               System.out.println("error lier à ClientImp"+e.getMessage());
           }
        actualiserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.clear();
                    listeUser =  iserver.getConnectedusers(name);
                        for (int i=0 ;i<listeUser.size();i++){
                            model.addElement(listeUser.get(i));
                        }
                    list1.setModel(model);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedValues = list1.getSelectedValuesList();
                try {
                   if(selectedValues.isEmpty()){
                       if(!input.getText().isEmpty()){
                           client.sendMessage();
                           input.setText(" ");
                       }else
                           JOptionPane.showMessageDialog(new JFrame(),"entrez votre message");
                   }else {
                       iserver.sendToSpecifique(name,selectedValues,input.getText());
                       input.setText(" ");
                   }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        fileChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    String[] extension = file.getName().split("\\.");
                    if(extension[extension.length - 1].equals("txt")||
                            extension[extension.length - 1].equals("java")||
                            extension[extension.length - 1].equals("php")||
                            extension[extension.length - 1].equals("c")||
                            extension[extension.length - 1].equals("png")||
                            extension[extension.length - 1].equals("jpg")||
                            extension[extension.length - 1].equals("jpeg")||
                            extension[extension.length - 1].equals("pdf")
                    ){
                        try {
                            ArrayList<Integer> inc;
                            try (FileInputStream in = new FileInputStream(file)) {
                                inc = new ArrayList<>();
                                int c=0;
                                while((c=in.read()) != -1) {
                                    inc.add(c);
                                }
                                in.close();
                            }
                           iserver.diffuserFile(file.getName(),inc,list1.getSelectedValuesList(),name);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        } catch (RemoteException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        } catch (IOException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }

                        JLabel jfile = new JLabel(file.getName() + " Uploaded ...");
                        jfile.setFont(new java.awt.Font("Arial", Font.ITALIC, 16));
                        jfile.setOpaque(true);
                        jfile.setBackground(Color.WHITE);
                        jfile.setForeground(Color.black);
                        sharedFile.add(jfile);
                        sharedFile.repaint();
                        sharedFile.revalidate();
                    }
                }
            }
        });
        whiteboard.addMouseMotionListener(new MouseMotionAdapter() {
        });
        whiteboard.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(!sizeOfTrait.getText().isEmpty()){
                    size = Integer.parseInt(sizeOfTrait.getText());
                }
                try {
                    iserver.brodcastVue(e,name,color,size);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                Graphics g = whiteboard.getGraphics();
                g.setColor(color);
                g.fillOval(e.getX(),e.getY(),size,size);
            }
        });
        colorchoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color1 = JColorChooser.showDialog(whiteboard, "Make a choice", Color.MAGENTA);
                color = color1;
            }
        });
        clearBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    iserver.clearBoard(name);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                Graphics2D g = (Graphics2D) whiteboard.getGraphics();
                g.setBackground(Color.white);
                g.clearRect(0, 0, whiteboard.getWidth(), whiteboard.getHeight());
            }
        });
        deconexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    iserver.deconnexion(name);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
    }


    public void lancerComponent(){
        setContentPane(global);
        this.setLocationRelativeTo(null);
        setSize(1000,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        sharedFile.setSize(new Dimension(300,200));
        output.setFont(new Font("Serif",Font.BOLD,10));
        whiteboard.setPreferredSize(new Dimension(500,250));
        whiteboard.setBackground(Color.white);
    }




    private JPanel global;
    private JPanel left;
    private JPanel top;
    private JPanel right;
    private JPanel buttom;
    private JTextArea output;
    private JLabel etudiantName;
    private JList list1 ;
    private JTextField input;
    private JButton send;
    private JButton actualiserButton;
    private JButton fileChoose;
    private JPanel sharedFile ;
    private JPanel whiteboard;
    private JButton colorchoose;
    private JButton clearBoard;
    private JTextField sizeOfTrait;
    private JButton deconexion;


}
