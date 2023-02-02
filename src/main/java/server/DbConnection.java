package server;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {

    String url = "jdbc:mysql://localhost:3306/elearn";
    String username = "root";
    String password = "";
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public boolean login(String name,String pass,String table) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM "+table;
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            if((resultSet.getString("nom")).equals(name) && (resultSet.getString("passwrd")).equals(pass)){
                connection.close();
                return true;
            }
        }
        connection.close();
        return false;
    }
    public List<String> collectStudProf(String name) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        int id = 0;
        List<String> list = new ArrayList<>();
        String query1 ;
        String query,prof="" ;
        statement = connection.createStatement();
       if(!name.endsWith("prof")){
           query1 = "select id_prof,nom from personne";
           resultSet = statement.executeQuery(query1);
           while (resultSet.next()){
               if((resultSet.getString("nom")).equals(name) ){
                   id = Integer.parseInt(resultSet.getString("id_prof"));
               }
           }
           query = "select personne.nom,prof.nom from personne " +
                   " INNER JOIN prof where personne.id_prof = prof.id_prof and personne.id_prof = "+id;
           resultSet = statement.executeQuery(query);
           while (resultSet.next()){
               prof = resultSet.getString("prof.nom");
               list.add(resultSet.getString("personne.nom"));
           }
       }else {
           query1 = "select id_prof,nom from prof";
           resultSet = statement.executeQuery(query1);
           while (resultSet.next()){
               if((resultSet.getString("nom")).equals(name) ){
                   prof = resultSet.getString("nom");
                   id = Integer.parseInt(resultSet.getString("id_prof"));
               }
           }
           query = "select nom from personne where id_prof ="+id;
           resultSet = statement.executeQuery(query);
           while (resultSet.next()){
               list.add(resultSet.getString("nom"));
           }
       }
        list.add(prof);
       connection.close();
       return list;

    }

}




class Main{
    public static void main(String[] args) {
        DbConnection db = new DbConnection();
        try {
            boolean b = db.login("Mehdi prof","123","prof");
            System.out.println(b);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

