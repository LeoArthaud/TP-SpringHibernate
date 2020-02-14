package org.epsi.b3.simplewebapp.db.conn;
 
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An utility class to get connections to the database.
 */
public class ConnectionUtils {

    private static final Logger LOGGER = Logger.getLogger(ConnectionUtils.class.getName());

    /**
     * Gets a new connection to the DB.
     * @return a new DB connection
     * @throws ClassNotFoundException if the JDBC driver cannot be instantiated
     * @throws SQLException if a SQL error occurred.
     */
    public static Connection getRawConnection()
              throws ClassNotFoundException, SQLException {

        Connection conn = getDriver();
        // Remove the auto-commit of transactions (only commit when explicit).
        conn.setAutoCommit(false);
        return conn;
    }

    public static Connection tryAndGetConnection() {
        try {
            return getRawConnection();
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Unable to access the DB !", e);
            // this is a dead-end
            // forward the checked exception in a runtime to ease error handling and crash the app
            throw new RuntimeException("Unable to access the DB", e);
        }
    }

    private static Connection getDriver() throws SQLException, ClassNotFoundException {
        // Choose what database to use - H2 for in-memory tests or MySQL for a real DB.
        //return MySQLConnUtils.getMySQLConnection();
        return H2ConnUtils.getH2Connection();
    }
}