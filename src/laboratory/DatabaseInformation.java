package laboratory;

import java.sql.*;
import static laboratory.Laboratory.JDBC_DRIVER;
import static laboratory.Laboratory.DB_URL;
import static laboratory.Laboratory.USER;
import static laboratory.Laboratory.PASS;

public class DatabaseInformation {

    // 2021-01-15 @TP
    public static String getDriverVersion() {

        String information = "No information.";

        try {
            Class jdbc = Class.forName(JDBC_DRIVER);
            Driver driver = DriverManager.getDriver(DB_URL);
            information = "Class: " + jdbc.getCanonicalName() + " / JDBC version: " + driver.getMajorVersion() + "." + driver.getMinorVersion() + " / Database: " + DB_URL;
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        }
        return information;
    }

    // 2021-01-15 @TP
    public static String getTransactionIsolationLevels() {

        Connection connection = null;
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
                System.err.println("Error SQL. Exception: " + e);
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
                System.err.println("Error SQL. Exception: " + e);
            }

        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        }

        information = "Current connection isolation level = " + isolationLevel + ". Default isolation level = " + defaultIsolationLevel + ".";
        return information;
    }
    
    // 2020-01-16 @TP
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