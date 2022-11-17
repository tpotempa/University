package laboratory;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static laboratory.Laboratory.JDBC_DRIVER;
import static laboratory.Laboratory.DB_URL;
import static laboratory.Laboratory.USER;
import static laboratory.Laboratory.PASS;

public class DatabaseInformation {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInformation.class);
    public static String getDriverVersion() {

        String information = "No information.";

        try {
            Class<?> jdbc = Class.forName(JDBC_DRIVER);
            Driver driver = DriverManager.getDriver(DB_URL);
            information = "Class: " + jdbc.getCanonicalName() + " / JDBC version: " + driver.getMajorVersion() + "." + driver.getMinorVersion() + " / Database: " + DB_URL;
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        }
        return information;
    }

    public static String getTransactionIsolationLevels() {

        Connection connection;
        String information;
        String isolationLevel = "No information.";
        String defaultIsolationLevel = "No information.";

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            try {
                switch (connection.getMetaData().getDefaultTransactionIsolation()) {
                    case Connection.TRANSACTION_NONE:
                        defaultIsolationLevel = "TRANSACTION_NONE";
                        break;
                    case Connection.TRANSACTION_READ_COMMITTED:
                        defaultIsolationLevel = "TRANSACTION_READ_COMMITTED";
                        break;
                    case Connection.TRANSACTION_READ_UNCOMMITTED:
                        defaultIsolationLevel = "TRANSACTION_READ_UNCOMMITTED";
                        break;
                    case Connection.TRANSACTION_REPEATABLE_READ:
                        defaultIsolationLevel = "TRANSACTION_REPEATABLE_READ";
                        break;
                    case Connection.TRANSACTION_SERIALIZABLE:
                        defaultIsolationLevel = "TRANSACTION_SERIALIZABLE";
                        break;
                    default:
                        defaultIsolationLevel = "UNKNOWN";
                }
            } catch (Exception e) {
                logger.error("A generic exception occurred.", e);
            }
            try {
                switch (connection.getTransactionIsolation()) {
                    case Connection.TRANSACTION_NONE:
                        isolationLevel = "TRANSACTION_NONE";
                        break;
                    case Connection.TRANSACTION_READ_COMMITTED:
                        isolationLevel = "TRANSACTION_READ_COMMITTED";
                        break;
                    case Connection.TRANSACTION_READ_UNCOMMITTED:
                        isolationLevel = "TRANSACTION_READ_UNCOMMITTED";
                        break;
                    case Connection.TRANSACTION_REPEATABLE_READ:
                        isolationLevel = "TRANSACTION_REPEATABLE_READ";
                        break;
                    case Connection.TRANSACTION_SERIALIZABLE:
                        isolationLevel = "TRANSACTION_SERIALIZABLE";
                        break;
                    default:
                        isolationLevel = "UNKNOWN";
                }
            } catch (Exception e) {
                logger.error("A generic exception occurred.", e);
            }

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        }

        information = "For current connection transaction isolation level = " + isolationLevel + ". Default isolation level = " + defaultIsolationLevel + ".";
        return information;
    }
    
    public static String getTransactionIsolationLevel(int transactionIsolationLevel) {
            String isolationLevel;
            switch (transactionIsolationLevel) {
                case Connection.TRANSACTION_NONE:
                    isolationLevel = "TRANSACTION_NONE";
                    break;
                case Connection.TRANSACTION_READ_COMMITTED:
                    isolationLevel = "TRANSACTION_READ_COMMITTED";
                    break;
                case Connection.TRANSACTION_READ_UNCOMMITTED:
                    isolationLevel = "TRANSACTION_READ_UNCOMMITTED";
                    break;
                case Connection.TRANSACTION_REPEATABLE_READ:
                    isolationLevel = "TRANSACTION_REPEATABLE_READ";
                    break;
                case Connection.TRANSACTION_SERIALIZABLE:
                    isolationLevel = "TRANSACTION_SERIALIZABLE";
                    break;
                default:
                    isolationLevel = "UNKNOWN";
        }    
        return isolationLevel;
    }
}