package laboratory;

import java.sql.*;
import java.util.*;
import static laboratory.DatabaseInformation.*;

public class ConnectionPool {

    private String driver = null;
    private String url = null;
    private int size = 0;
    private String username = new String();
    private String password = new String();
    private ArrayList pool = null;

    public ConnectionPool() {
    }

    public void setDriver(String value) {
        if (value != null) {
            driver = value;
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setURL(String value) {
        if (value != null) {
            url = value;
        }
    }

    public String getURL() {
        return url;
    }

    public void setSize(int value) {
        if (value > 1) {
            size = value;
        }
    }

    public int getSize() {
        return size;
    }

    public void setUsername(String value) {
        if (value != null) {
            username = value;
        }
    }

    public String getUserName() {
        return username;
    }

    public void setPassword(String value) {
        if (value != null) {
            password = value;
        }
    }

    public String getPassword() {
        return password;
    }

    // 2020-01-17 @TP Creating and returning a connection
    private Connection createConnection() throws Exception {
        Connection con = DriverManager.getConnection(url, username, password);
        return con;
    }

    // 2020-01-17 @TP Initialization of the pool
    public synchronized void initializePool() throws Exception {
        if (driver == null || url == null || size < 1) {
            throw new Exception("No parameters specified.");
        }
        System.out.println("Creating connections.");

        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC driver cannot be found. " + e);
            }
            for (int x = 0; x < size; x++) {
                Connection con = createConnection();
                if (con != null) {
                    PooledConnection pcon = new PooledConnection(con);
                    addConnection(pcon);
                }
            }
        } catch (Exception e) {
            System.err.println("Error. Initilization of the pool. " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    // 2020-01-17 @TP Adding the PooledConnection to the pool
    private void addConnection(PooledConnection value) {
        if (pool == null) {
            pool = new ArrayList(size);
        }
        pool.add(value);
    }

    // 2020-01-16 @TP
    public synchronized void releaseConnection(Connection con) {
        for (int x = 0; x < pool.size(); x++) {
            PooledConnection pcon = (PooledConnection) pool.get(x);
            if (pcon.getConnection() == con) {
                boolean autocommit = false;
                int transactionIsolationLevel = Connection.TRANSACTION_READ_COMMITTED;

                try {
                    autocommit = con.getAutoCommit();
                } catch (SQLException sqle) {
                    System.err.println("Error SQL. AUTOCOMMIT property of the connection cannot be determined. " + sqle.getMessage());                    
                }
                if (autocommit == false) {
                    System.err.println("Warning SQL. Pool contains connection No " + x + " [" + con + "] with AUTOCOMMIT " + autocommit);
                }

                try {
                    transactionIsolationLevel = con.getTransactionIsolation();
                } catch (SQLException sqle) {
                    System.err.println("Error SQL. TRANSACTION ISOLATION LEVEL property of the connection cannot be determined. " + sqle.getMessage());
                }
                if (transactionIsolationLevel != Connection.TRANSACTION_READ_COMMITTED) {
                    String isolationLevel = getTransactionIsolationLevel(transactionIsolationLevel);                    
                    System.err.println("Warning SQL. Releasing connection No " + x + " [" + con + "] with TRANSACTION ISOLATION LEVEL = " + isolationLevel);
                }
                System.out.println("Information. Releasing connection No " + x + " [" + con + "] with [AUTOCOMMIT=" + autocommit + "]");

                pcon.setInUse(false);
                break;
            }
        }
    }

    // 2020-01-17 @TP Finding an available connection
    public synchronized Connection getConnection()
            throws Exception {
        PooledConnection pcon = null;
        for (int x = 0; x < pool.size(); x++) {
            pcon = (PooledConnection) pool.get(x);
            if (pcon.inUse() == false) {
                pcon.setInUse(true);
                Connection con = pcon.getConnection();

                boolean autocommit = false;
                try {
                    autocommit = con.getAutoCommit();
                } catch (SQLException sqle) {
                    System.err.println("Error SQL. AUTOCOMMIT property of the connection cannot be determined. " + sqle.getMessage());
                }
                System.out.println("Information. Getting connection No " + x + " [" + con + "] with [AUTOCOMMIT=" + autocommit + "]");
                return con;
            }
        }

        // 2020-01-17 @TP When there is no free connection, create and add a new one
        try {
            Connection con = createConnection();
            pcon = new PooledConnection(con);
            pcon.setInUse(true);
            pool.add(pcon);
        } catch (Exception e) {
            System.err.println("Error. Getting connection.");
            System.err.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return pcon.getConnection();
    }

    // 2020-01-17 @TP
    public synchronized void emptyPool() {
        for (int x = 0; x < pool.size(); x++) {
            System.out.println("Closing JDBC Connection " + x);
            PooledConnection pcon = (PooledConnection) pool.get(x);
                    
            if (pcon.inUse() == false) {
                pcon.close();
            } else {
                // If connection is still in use, sleep for 30 seconds and force close.
                try {
                    java.lang.Thread.sleep(30000);
                    pcon.close();
                } catch (InterruptedException ie) {
                    System.err.println(ie.getMessage());
                }
            }
        }
    }
}