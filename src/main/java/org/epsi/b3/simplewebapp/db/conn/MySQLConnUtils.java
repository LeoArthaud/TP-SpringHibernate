package org.epsi.b3.simplewebapp.db.conn;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A driver to connect to a MySQL DB.
 * @see ConnectionUtils
 */
public class MySQLConnUtils {
  
 public static Connection getMySQLConnection()
         throws ClassNotFoundException, SQLException {
     /* !!! Change these settings accordingly !!! */
     String hostName = "localhost";
     String dbName = "B3";
     String userName = "root";
     String password = "WWFGAtbppUXjKh7E";
     return getMySQLConnection(hostName, dbName, userName, password);
 }
  
 public static Connection getMySQLConnection(String hostName, String dbName,
         String userName, String password) throws SQLException,
         ClassNotFoundException {
    
     Class.forName("com.mysql.jdbc.Driver");
  
     // The URL to connect to the MySQL DB.
     // Example:
     // jdbc:mysql://localhost:3306/simplehr
     String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

     return DriverManager.getConnection(connectionURL, userName,
             password);
 }
}