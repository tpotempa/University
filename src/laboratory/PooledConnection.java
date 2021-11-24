package laboratory;

import java.sql.*;
import static laboratory.DatabaseInformation.*;

public class PooledConnection {

    private Connection connection = null;    
    private boolean inuse = false;

    // 2021-01-15 @TP Constructor that takes the passed in JDBC Connection and stores it in the connection attribute.
    public PooledConnection(Connection value) {
        if (value != null) {
            connection = value;
        }
    }

    // 2021-01-15 @TP Returning a reference to the JDBC Connection.
    public Connection getConnection() {
        return connection;
    }

    // 2021-01-15 @TP Setting the status of the PooledConnection.
    public void setInUse(boolean value) {
        inuse = value;
    }

    // 2021-01-15 @TP Returning the current status of the connection.
    public boolean inUse() {
        boolean autoCommit = false;
        int transactionIsolationLevel = Connection.TRANSACTION_NONE;

        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException sqle) {
            System.err.println("Error SQL. AUTOCOMMIT property of the connection cannot be determined. " + sqle.getMessage());
        }
        if (!inuse && !autoCommit) {
            System.err.println("Warning SQL. Pool contains connection [" + connection + "] with AUTOCOMMIT = false.");
            return true;
        }

        try {
            transactionIsolationLevel = connection.getTransactionIsolation();
        } catch (SQLException sqle) {
            System.err.println("Error SQL. TRANSACTION ISOLATION LEVEL property of the connection cannot be determined. " + sqle.getMessage());
        }
        if (!inuse && transactionIsolationLevel != Connection.TRANSACTION_READ_COMMITTED) {
            String isolationLevel = getTransactionIsolationLevel(transactionIsolationLevel);
            System.err.println("Warning SQL. Pool contains connection [" + connection + "] with TRANSACTION ISOLATION LEVEL != " + isolationLevel);
            return true;
        }
        return inuse;
    }

    // 2021-01-15 @TP Closing the real JDBC Connection.
    public void close() {
        try {
            connection.close();
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }
}