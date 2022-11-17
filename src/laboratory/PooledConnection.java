package laboratory;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static laboratory.DatabaseInformation.*;

public class PooledConnection {

    private static final Logger logger = LoggerFactory.getLogger(PooledConnection.class);
    private Connection connection = null;    
    private boolean inuse = false;

    // Konstruktor
    public PooledConnection(Connection value) {
        if (value != null) {
            connection = value;
        }
    }

    // Zwracanie referencji do połączenia.
    public Connection getConnection() {
        return connection;
    }

    // Ustawianie stanu połączenia.
    public void setInUse(boolean value) {
        inuse = value;
    }

    // Zwracanie stanu połączenia.
    public boolean inUse() {
        boolean autoCommit = false;
        int transactionIsolationLevel = Connection.TRANSACTION_NONE;

        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            logger.error("An exception occurred while getting AutoCommit property of the connection.", e);
        }
        if (!inuse && !autoCommit) {
            logger.warn("Pool contains connection [{}] with AutoCommit set to false.", connection);
            return true;
        }

        try {
            transactionIsolationLevel = connection.getTransactionIsolation();
        } catch (SQLException e) {
            logger.error("An exception occurred while getting Transaction Isolation Level property of the connection.", e);
        }
        if (!inuse && transactionIsolationLevel != Connection.TRANSACTION_READ_COMMITTED) {
            String isolationLevel = getTransactionIsolationLevel(transactionIsolationLevel);
            logger.warn("Pool contains connection [{}] with Transaction Isolation Level different than {}.", connection, isolationLevel);
            return true;
        }
        return inuse;
    }

    // Zamykanie połączenia.
    public void close() {
        try {
            connection.close();
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }
}