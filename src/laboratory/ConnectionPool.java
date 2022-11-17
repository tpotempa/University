package laboratory;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static laboratory.DatabaseInformation.*;

public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private String driver;
    private String url;
    private int size = 0;
    private String username;
    private String password;
    private ArrayList<PooledConnection> pool;

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

    public void setPassword(String value) {
        if (value != null) {
            password = value;
        }
    }

    // Tworzenie i zwrócenie połączenia.
    private Connection createConnection() throws Exception {
        return DriverManager.getConnection(url, username, password);
    }

    // Inicjalizacja puli połączeń.
    public synchronized void initializePool() throws Exception {
        if (driver == null || url == null || size < 1) {
            throw new Exception("No required parameters specified.");
        }
        logger.info("Creating connections.");

        try {
            try {
                Class.forName(driver);
                logger.info(getDriverVersion());
            } catch (ClassNotFoundException e) {
                logger.error("An exception occurred while loading {} class.", driver, e);
            } catch (Exception e) {
                logger.error("A generic exception occurred.", e);
            }
            for (int x = 0; x < size; x++) {
                Connection con = createConnection();
                if (con != null) {
                    PooledConnection pcon = new PooledConnection(con);
                    addConnection(pcon);
                }
            }
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
            throw new Exception(e.getMessage());
        }
    }

    // Dodanie połączenia do puli.
    private void addConnection(PooledConnection value) {
        if (pool == null) {
            pool = new ArrayList<>(size);
        }
        pool.add(value);
    }

    // Zwracanie połączenia do puli.
    public synchronized void releaseConnection(Connection con) {
        for (int x = 0; x < pool.size(); x++) {
            PooledConnection pcon = pool.get(x);
            if (pcon.getConnection() == con) {
                boolean autocommit = false;
                int transactionIsolationLevel = Connection.TRANSACTION_READ_COMMITTED;

                try {
                    autocommit = con.getAutoCommit();
                } catch (SQLException e) {
                    logger.error("An exception occurred while getting AutoCommit property of the connection.", e);
                }
                if (!autocommit) {
                    logger.warn("Pool contains connection No {} [{}] with AutoCommit set to false.", x, con);
                }

                try {
                    transactionIsolationLevel = con.getTransactionIsolation();
                } catch (SQLException e) {
                    logger.error("An exception occurred while getting Transaction Isolation Level property of the connection.", e);
                }
                if (transactionIsolationLevel != Connection.TRANSACTION_READ_COMMITTED) {
                    String isolationLevel = getTransactionIsolationLevel(transactionIsolationLevel);
                    logger.info("Releasing connection No {} [{}] with Transaction Isolation Level set to {}.", x, con, isolationLevel);
                }
                logger.info("Releasing connection No {} [{}] with AutoCommit set to {}.", x, con, autocommit);

                pcon.setInUse(false);
                break;
            }
        }
    }

    // Pobieranie nieużywanego połączenia z puli połączeń. W przypadku braku nieużywanego połączenia tworzenie nowego.
    public synchronized Connection getConnection() throws Exception {
        PooledConnection pcon;
        for (int x = 0; x < pool.size(); x++) {
            pcon = pool.get(x);
            if (!pcon.inUse()) {
                pcon.setInUse(true);
                Connection con = pcon.getConnection();

                boolean autocommit = false;
                try {
                    autocommit = con.getAutoCommit();
                } catch (SQLException e) {
                    logger.error("An exception occurred while getting AutoCommit property of the connection.", e);
                }
                logger.info("Getting connection No {} [{}] with AutoCommit set to {}.", x, con, autocommit);
                return con;
            }
        }

        // W przypadku braku nieużywanego połączenia w puli, tworzenie nowego oraz dodanie do puli połączeń.
        try {
            Connection con = createConnection();
            pcon = new PooledConnection(con);
            pcon.setInUse(true);
            pool.add(pcon);
            boolean autocommit = false;
            try {
                autocommit = con.getAutoCommit();
            } catch (SQLException e) {
                logger.error("An exception occurred while getting AutoCommit property of the connection.", e);
            }
            logger.info("Creating and getting a new connection No {} [{}] with AutoCommit set to {}.", pool.size() - 1, con, autocommit);
        } catch (Exception e) {
            logger.error("An exception occurred while getting the connection.", e);
            throw new Exception(e.getMessage());
        }
        return pcon.getConnection();
    }

    // Zamykanie puli połączeń.
    public synchronized void emptyPool() {
        for (int x = 0; x < pool.size(); x++) {
            logger.info("Closing connection {}.", x);
            PooledConnection pcon = pool.get(x);

            // Jeżeli połączenie jest używane, poczekaj, a następnie wymuś zamknięcie.
            if (!pcon.inUse()) {
                pcon.close();
            } else try {
                Thread.sleep(10000);
                pcon.close();
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
        }
    }
}