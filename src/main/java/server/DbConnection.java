package server;


import java.sql.*;

public class DbConnection {

    String url = "jdbc:mysql://localhost:3306/elearn";
    String username = "root";
    String password = "";
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public boolean login(String name,String pass) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM personne ";
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

}




class Main{
    public static void main(String[] args) {
        DbConnection db = new DbConnection();
        try {
            boolean b = db.login("abdo","123");
            System.out.println(b);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

