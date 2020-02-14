package org.epsi.b3.simplewebapp.db.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A driver to connect to a H2 DB.
 * @see ConnectionUtils
 */
public class H2ConnUtils {

    private static final Logger LOGGER = Logger.getLogger(H2ConnUtils.class.getName());
    private static boolean tablesCreated = false;

    public static Connection getH2Connection()
            throws ClassNotFoundException, SQLException {
        // H2 connection params - no need to be changed as the DB will be created on the fly with these params
        // (provided for remote DB access only)
        String dbName = "B3";
        String userName = "root";
        String password = "WWFGAtbppUXjKh7E";
        return getH2Connection(dbName, userName, password);
    }

    public static Connection getH2Connection(String dbName,
                                             String userName, String password)
            throws SQLException {

        String connectionURL =
                "jdbc:h2:mem:" + dbName // use an in-memory H2 DB
                        + ";DB_CLOSE_DELAY=-1"; // do not destroy the DB when closing connection - only on JVM halt

        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);

        // A hack to create the different test tables in memory before using them
        if (!tablesCreated) {
            createProductTable(conn);
            createUserAccountsTable(conn);
            tablesCreated = true;
        }

        return conn;
    }

    private static void createProductTable(Connection conn) throws SQLException {
        LOGGER.log(Level.INFO, "Creating table PRODUCT in H2 DB");
        String createProductTable = "CREATE TABLE IF NOT EXISTS `PRODUCT` (" +
                "  code VARCHAR(255) not NULL" +
                ", name VARCHAR(255) not NULL" +
                ", price INTEGER" +
                ", PRIMARY KEY ( code )" +
                ")";
        conn.createStatement().executeUpdate(createProductTable);
    }

    private static void createUserAccountsTable(Connection conn) throws SQLException {
        LOGGER.log(Level.INFO, "Creating table USER_ACCOUNT in H2 DB");
        String createProductTable =  "CREATE TABLE IF NOT EXISTS `USER_ACCOUNT` (" +
                "idUser INTEGER not NULL AUTO_INCREMENT" +
                ", userName VARCHAR(255) not NULL" +
                ", password VARCHAR(255)" +
                ", gender VARCHAR(255)" +
                ", PRIMARY KEY ( idUser )" +
                ")";
        conn.createStatement().executeUpdate(createProductTable);

        String insertUsers =  "INSERT INTO USER_ACCOUNT(idUser, userName, password, gender) VALUES " +
                " (1,'john','doe',0),(2,'alice','bob',1) ";
        conn.createStatement().execute(insertUsers);
    }
}
