package laboratory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static laboratory.Laboratory.JDBC_DRIVER;
import static laboratory.Laboratory.DB_URL;
import static laboratory.Laboratory.USER;
import static laboratory.Laboratory.PASS;

public class Employee {

    private static final Logger logger = LoggerFactory.getLogger(Employee.class);
    private int id;
    private String firstName;
    private String lastName;
    private String title;
    private String position;
    private double salary;
    private int departmentId;

    public Employee() {
    }

    public Employee(int id, String firstName, String lastName, String title, String position, double salary, int departmentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.position = position;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    private static void getSupportedResultSetModes(Connection connection) throws SQLException {

        logger.info("HOLDABILITY:");
        boolean isCloseAtCommitSupported = connection.getMetaData().supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
        logger.info(isCloseAtCommitSupported ? "Database supports CLOSE_CURSORS_AT_COMMIT." : "Database does NOT support CLOSE_CURSORS_AT_COMMIT.");
        boolean isHoldOverCommitSupported = connection.getMetaData().supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
        logger.info(isHoldOverCommitSupported ? "Database supports HOLD_CURSORS_OVER_COMMIT." : "Database does NOT support HOLD_CURSORS_OVER_COMMIT.");

        logger.info("SCROLLABILITY & SENSITIVITY:");
        boolean isSensitiveSupported = connection.getMetaData().supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(isSensitiveSupported ? "Database supports TYPE_FORWARD_ONLY." : "Database does NOT support TYPE_FORWARD_ONLY.");
        isSensitiveSupported = connection.getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(isSensitiveSupported ? "Database supports TYPE_SCROLL_INSENSITIVE." : "Database does NOT support TYPE_SCROLL_INSENSITIVE.");
        isSensitiveSupported = connection.getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(isSensitiveSupported ? "Database supports TYPE_SCROLL_SENSITIVE." : "Database does NOT support TYPE_SCROLL_SENSITIVE.");

        logger.info("UPDATES VISIBILITY:");
        boolean areOwnUpdatesVisible = connection.getMetaData().ownUpdatesAreVisible(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(areOwnUpdatesVisible ? "Own updates are visible in mode TYPE_FORWARD_ONLY." : "Own updates are NOT visible in mode TYPE_FORWARD_ONLY.");
        areOwnUpdatesVisible = connection.getMetaData().ownUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(areOwnUpdatesVisible ? "Own updates are visible in mode TYPE_SCROLL_INSENSITIVE." : "Own updates are NOT visible in mode TYPE_SCROLL_INSENSITIVE.");
        areOwnUpdatesVisible = connection.getMetaData().ownUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(areOwnUpdatesVisible ? "Own updates are visible in mode TYPE_SCROLL_SENSITIVE." : "Own updates are NOT visible in mode TYPE_SCROLL_SENSITIVE.");
        boolean areOtherUpdatesVisible = connection.getMetaData().othersUpdatesAreVisible(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(areOtherUpdatesVisible ? "Other updates are visible in mode TYPE_FORWARD_ONLY." : "Other updates are NOT visible in mode TYPE_FORWARD_ONLY.");
        areOtherUpdatesVisible = connection.getMetaData().othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(areOtherUpdatesVisible ? "Other updates are visible in mode TYPE_SCROLL_INSENSITIVE." : "Other updates are NOT visible in mode TYPE_SCROLL_INSENSITIVE.");
        areOtherUpdatesVisible = connection.getMetaData().othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(areOtherUpdatesVisible ? "Other updates are visible in mode TYPE_SCROLL_SENSITIVE." : "Other updates are NOT visible in mode TYPE_SCROLL_SENSITIVE.");

        logger.info("DELETES VISIBILITY:");
        boolean areOwnDeletesVisible = connection.getMetaData().ownDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(areOwnDeletesVisible ? "Own deletes are visible in mode TYPE_FORWARD_ONLY." : "Own deletes are NOT visible in mode TYPE_FORWARD_ONLY.");
        areOwnDeletesVisible = connection.getMetaData().ownDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(areOwnDeletesVisible ? "Own deletes are visible in mode TYPE_SCROLL_INSENSITIVE." : "Own deletes are NOT visible in mode TYPE_SCROLL_INSENSITIVE.");
        areOwnDeletesVisible = connection.getMetaData().ownDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(areOwnDeletesVisible ? "Own deletes are visible in mode TYPE_SCROLL_SENSITIVE." : "Own deletes are NOT visible in mode TYPE_SCROLL_SENSITIVE.");
        boolean areOtherDeletesVisible = connection.getMetaData().othersDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(areOtherDeletesVisible ? "Other deletes are visible in mode TYPE_FORWARD_ONLY." : "Other deletes are NOT visible in mode TYPE_FORWARD_ONLY.");
        areOtherDeletesVisible = connection.getMetaData().othersDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(areOtherDeletesVisible ? "Other deletes are visible in mode TYPE_SCROLL_INSENSITIVE." : "Other deletes are NOT visible in mode TYPE_SCROLL_INSENSITIVE.");
        areOtherDeletesVisible = connection.getMetaData().othersDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(areOtherDeletesVisible ? "Other deletes are visible in mode TYPE_SCROLL_SENSITIVE." : "Other deletes are NOT visible in mode TYPE_SCROLL_SENSITIVE.");

        logger.info("INSERTS VISIBILITY:");
        boolean areOwnInsertsVisible = connection.getMetaData().ownInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(areOwnInsertsVisible ? "Own inserts are visible in mode TYPE_FORWARD_ONLY." : "Own inserts are NOT visible in mode TYPE_FORWARD_ONLY.");
        areOwnInsertsVisible = connection.getMetaData().ownInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(areOwnInsertsVisible ? "Own inserts are visible in mode TYPE_SCROLL_INSENSITIVE." : "Own inserts are NOT visible in mode TYPE_SCROLL_INSENSITIVE.");
        areOwnInsertsVisible = connection.getMetaData().ownInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(areOwnInsertsVisible ? "Own inserts are visible in mode TYPE_SCROLL_SENSITIVE." : "Own inserts are NOT visible in mode TYPE_SCROLL_SENSITIVE.");
        boolean areOtherInsertsVisible = connection.getMetaData().othersInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY);
        logger.info(areOtherInsertsVisible ? "Other inserts are visible in mode TYPE_FORWARD_ONLY." : "Other inserts are NOT visible in mode TYPE_FORWARD_ONLY.");
        areOtherInsertsVisible = connection.getMetaData().othersInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
        logger.info(areOtherInsertsVisible ? "Other inserts are visible in mode TYPE_SCROLL_INSENSITIVE." : "Other inserts are NOT visible in mode TYPE_SCROLL_INSENSITIVE.");
        areOtherInsertsVisible = connection.getMetaData().othersInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
        logger.info(areOtherInsertsVisible ? "Other inserts are visible in mode TYPE_SCROLL_SENSITIVE." : "Other inserts are NOT visible in mode TYPE_SCROLL_SENSITIVE.");
    }

    private static void getSupportedTransactionIsolationLevels(Connection connection) throws SQLException {

        logger.info("TRANSACTION ISOLTAION LEVEL:");
        boolean isReadUncommittedSupported = connection.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
        logger.info(isReadUncommittedSupported ? "Database supports transaction isolation level TRANSACTION_READ_UNCOMMITTED." : "Database does NOT support transaction isolation level TRANSACTION_READ_UNCOMMITTED.");
        boolean isReadCommittedSupported = connection.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
        logger.info(isReadCommittedSupported ? "Database supports transaction isolation level TRANSACTION_READ_COMMITTED." : "Database does NOT support transaction isolation level TRANSACTION_READ_COMMITTED.");
        boolean isRepeatableReadSupported = connection.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ);
        logger.info(isRepeatableReadSupported ? "Database supports transaction isolation level TRANSACTION_REPEATABLE_READ." : "Database does NOT support transaction isolation level TRANSACTION_REPEATABLE_READ.");
        boolean isSerializableSupported = connection.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
        logger.info(isSerializableSupported ? "Database supports transaction isolation level TRANSACTION_SERIALIZABLE." : "Database does NOT support transaction isolation level TRANSACTION_SERIALIZABLE.");
    }

    private static void close(Connection connection, Statement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
                logger.info("Object {} closed.", rs.getClass().getName());
            }
        } catch (Exception e) {
            logger.error("An exception occurred while closing a result set.", e);
        }
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
                logger.info("Object {} closed.", stmt.getClass().getName());
            }
        } catch (NullPointerException e) {
            logger.error("Null pointer exception occurred while closing Statement object.", e);
        } catch (Exception e) {
            logger.error("An exception occurred while closing a {}.", stmt.getClass().getName(), e);
        }
        try {
            if (connection != null && !connection.isClosed()) {
                if (!connection.getAutoCommit()) {
                    try {
                        connection.setAutoCommit(true);
                        logger.info("Connection AutoCommit mode set to {}.", connection.getAutoCommit());
                    } catch (SQLException e) {
                        logger.error("An exception occurred while setting connection AutoCommit mode.", e);
                    }
                }
                connection.close();
                logger.info("Object {} closed.", connection.getClass().getName());
            }
        } catch (Exception e) {
            logger.error("An exception occurred while closing a connection.", e);
        }
    }

    public static DatabaseResults getEmployees() {

        DatabaseResults employees = null;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            employees = DatabaseUtilities.getQueryResults("SELECT id_prowadzacego AS \"ID\", * FROM kadry.prowadzacy", connection);

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, null, null);
        }
        return employees;
    }

    public static List<Employee> getEmployees_Statement() {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            String sql = "SELECT * FROM kadry.prowadzacy ORDER BY 1";
            stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            stmt.setFetchSize(100);

            long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Employee row = new Employee(rs.getInt("id_prowadzacego"), rs.getString("imie"), rs.getString("nazwisko"), rs.getString("tytul"), rs.getString("stanowisko"), rs.getDouble("placa_zasadnicza"), rs.getInt("id_jednostki_zatrudniajacej"));
                employeeList.add(row);
            }
            long endTime = System.currentTimeMillis();

            logger.info("Execution time {} ms.", (endTime - startTime));

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, stmt, rs);
        }
        return employeeList;
    }

    public static List<Employee> getEmployees_StatementUpdateVisibility() {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<>();

        Integer sleep = 1; // Czas uśpienia pozwalający na (nie)obserwację w bazie danych zmian wykonywanych w ramach transakcji (PgAdmin - Narzędzia - Status serwera).

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Informacja o wspieranych poziomach izolacji transakcji
            getSupportedTransactionIsolationLevels(connection);
            // Informacja o wspieranych własnościach zbioru rezultatów
            getSupportedResultSetModes(connection);

            String sql = "SELECT * FROM kadry.prowadzacy WHERE id_prowadzacego <= 10 ORDER BY 1";
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            stmt.setFetchSize(1);

            long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TimeUnit.MILLISECONDS.sleep(sleep);
                logger.info("Employees ID = {}. Płaca zasadnicza = {}.", rs.getInt("id_prowadzacego"), rs.getDouble("placa_zasadnicza"));
                if(rs.getInt("id_prowadzacego") == 5) {
                    rs.updateDouble("placa_zasadnicza", 5 * rs.getDouble("placa_zasadnicza"));
                    rs.updateRow();
                }
            }
            long endTime = System.currentTimeMillis();
            logger.info("First read execution time {} ms.", (endTime - startTime));

            startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TimeUnit.MILLISECONDS.sleep(sleep);
                Employee row = new Employee(rs.getInt("id_prowadzacego"), rs.getString("imie"), rs.getString("nazwisko"), rs.getString("tytul"), rs.getString("stanowisko"), rs.getDouble("placa_zasadnicza"), rs.getInt("id_jednostki_zatrudniajacej"));
                employeeList.add(row);
                logger.info("Employees ID = {}. Płaca zasadnicza = {}.", rs.getInt("id_prowadzacego"), rs.getDouble("placa_zasadnicza"));
            }
            endTime = System.currentTimeMillis();
            logger.info("Second read execution time {} ms.", (endTime - startTime));

            connection.rollback();

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, stmt, rs);
        }
        return employeeList;
    }

    public static double getEmployeeSalary_Statement(int employee) {

        double salary = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT placa_zasadnicza FROM kadry.prowadzacy WHERE id_prowadzacego = " + employee;
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            rs = stmt.executeQuery(sql);

            rs.beforeFirst();
            if (rs.next()) {
                salary = rs.getDouble("placa_zasadnicza");
            } else {
                logger.info("No rows were fetched.");
            }

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, stmt, rs);
        }
        return salary;
    }

    public static double getEmployeeSalary_PreparedStatement(int employee) {

        double salary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT placa_zasadnicza FROM kadry.prowadzacy WHERE id_prowadzacego = ?";
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setInt(1, employee);
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                salary = rs.getDouble("placa_zasadnicza");
            } else {
                logger.info("No rows were fetched.");
            }
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return salary;
    }

    public static List<Employee> getEmployeesSalary_PreparedStatementResultSet(int employeeLowerBound, int employeeUpperBound) {

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            logger.info("Salaries of employees with ID between = {} and {}.", employeeLowerBound, employeeUpperBound);
            String sql = "SELECT id_prowadzacego, placa_zasadnicza FROM kadry.prowadzacy WHERE id_prowadzacego BETWEEN ? AND ?";
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            pstmt.clearParameters();
            pstmt.setInt(1, employeeLowerBound);
            pstmt.setInt(2, employeeUpperBound);

            rs = pstmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                Employee row = new Employee();
                row.setId(rs.getInt("id_prowadzacego"));
                row.setSalary(rs.getDouble("placa_zasadnicza"));
                employeeList.add(row);
            }

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return employeeList;
    }

    public static double changeEmployeeSalary_PreparedStatement(double salaryRise, int employee) {

        double changedSalary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            // Przeglądanie zmienionych wynagrodzeń.
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                // Co oznacza wartość 6 w poniższej metodzie getDouble(6)?
                changedSalary = rs.getDouble(6);
                logger.info("Updated salary = {}.", changedSalary);
            } else {
                logger.info("No rows were updated.");
            }

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return changedSalary;
    }

    public static double changeEmployeeSalary_PreparedStatementResultSet(double salaryRise, int employee) {

        double salary;
        double changedSalary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT id_prowadzacego, placa_zasadnicza FROM kadry.prowadzacy WHERE id_prowadzacego = ?";
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            pstmt.clearParameters();
            pstmt.setInt(1, employee);

            rs = pstmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                salary = rs.getDouble("placa_zasadnicza");
                logger.info("Current salary = {}.", salary);
                changedSalary = (1 + salaryRise) * salary;
                rs.updateDouble("placa_zasadnicza", changedSalary);
                rs.updateRow();
                logger.info("Updated salary = {}.", changedSalary);
            }
            // @TODO rs.moveToInsertRow();

        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return changedSalary;
    }

    public static double addEmployeePayment_PreparedStatementResultSet(double salary, int month, int year, String category, int employee) {

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM kadry.wyplaty WHERE id_prowadzacego = ? AND rok = ? AND miesiac = ? AND kategoria_wyplaty = ?";
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            pstmt.clearParameters();
            pstmt.setInt(1, employee);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            pstmt.setString(4, category);

            rs = pstmt.executeQuery();
            rs.beforeFirst();
            if (!rs.next()) {
                rs.moveToInsertRow(); // Ustawienie na pozycji w zbiorze danych pozwalającej dodać rekord.
                rs.updateInt("id_prowadzacego", employee);
                rs.updateInt("rok", year);
                rs.updateInt("miesiac", month);
                rs.updateString("kategoria_wyplaty", category);
                rs.updateDate("data_wyplaty", new java.sql.Date(System.currentTimeMillis()));
                rs.updateDouble("kwota", salary);
                rs.insertRow(); // Dodanie rekordu do bazy danych.
            }
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return salary;
    }

    // @TODO Zmieniono 2022-11-16
    // Aktualizacja wynagrodzenia pracowników, którzy zarabiają nie więcej niż wartość parametru salaryBound.
    public static int changeEmployeeSalary_PreparedStatementViaExecuteUpdate(double salaryRise, int salaryBound) {

        int rowsUpdatedCount = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE placa_zasadnicza <= ?";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, salaryBound);

            // W poniższej linii wykonywana jest metoda executeUpdate(). Jaka metoda była wykorzystywana wcześniej? Czym się różnią obie metody?
            rowsUpdatedCount = pstmt.executeUpdate();
            if (rowsUpdatedCount > 0) {
                logger.info("Salary updated for {} employees.", rowsUpdatedCount);
            } else {
                logger.info("No employees salaries were updated.");
            }

        } catch (SQLException e) {
            logger.error("SQL exception occurred due to failed update.", e);
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, null);
        }
        return rowsUpdatedCount;
    }

    public static double changeSalary_RollbackError(double salaryRise, int employee) {

        double updatedSalary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            logger.info("Autocommit mode is set to {}.", connection.getAutoCommit());

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            rs = pstmt.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                // Co oznacza wartość 6 w poniższej metodzie getDouble(6)?
                updatedSalary = rs.getDouble(6);
                if (updatedSalary > 10000) {
                    logger.info("Constraint violation. Updated salary = {} is greater than upper limit.", updatedSalary);
                    connection.rollback(); // Wycofanie wprowadzonych zmian nie może być wykonywane w trybie automatycznego zatwierdzania transakcji.
                    updatedSalary = -1;
                } else {
                    logger.info("Updated salary = {}.", updatedSalary);
                }
            } else {
                logger.info("No employees salaries were updated.");
            }
        } catch (SQLException e) {
            logger.error("SQL exception occurred due to failed update.", e);
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return updatedSalary;
    }

    public static double changeSalary_ExecuteQueryRollback(double salaryRise, int employee) {

        double updatedSalary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Integer sleep = 15; // Czas uśpienia pozwalający na obserwację nałożonych w bazie danych blokad (PgAdmin - Narzędzia - Status serwera).

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Ustawienie połączenia w tryb nieautomatycznego zatwierdzania/wycofywania transakcji tj. AutoCommit = false.
            connection.setAutoCommit(false);
            logger.info("AutoCommit mode is set to {}.", connection.getAutoCommit());

            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            try {
                rs = pstmt.executeQuery();
                rs.beforeFirst();
                if (rs.next()) {
                    updatedSalary = rs.getDouble(6);
                    if (updatedSalary > 10000) {
                        logger.info("Constraint violation. Updated salary = {} is greater than upper limit.", updatedSalary);
                        TimeUnit.SECONDS.sleep(sleep);
                        connection.rollback();  // Wycofanie wprowadzonych zmian może być wykonywane w trybie nieautomatycznego zatwierdzania transakcji.
                        updatedSalary = -1;
                    } else {
                        TimeUnit.SECONDS.sleep(sleep);
                        connection.commit();
                        logger.info("Updated salary = {}.", updatedSalary);
                    }
                } else {
                    logger.info("No employees salaries were updated.");
                    // Czy ma sens wykonanie zatwierdzenia transakcji, gdy żaden rekord nie został zmieniony?
                    connection.commit();
                }
            } catch (Exception e) {
                // W przypadku działania w trybie nieautomatycznego zatwierdzania/wycofywania transakcji tj. AutoCommit = false należy w przypadku wystąpienia wyjątków samodzielnie obsługiwać wycofywanie ewentualnych zmian wprowadzonych w ramach transakcji.
                TimeUnit.SECONDS.sleep(sleep);
                connection.rollback();
                logger.error("Exception occurred therefore update was rollbacked.", e);
            }
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return updatedSalary;
    }

    // Algorytm przyznania podwyżki:
    // - Jeżeli po pierwszej podwyżce wynagrodzenie pracownika wynosi ponad 10000 PLN to podwyżka zostanie cofnięta (pracownik nie otrzyma pierwszej podwyżki).
    // - Jeżeli po pierwszej podwyżce wynagrodzenie pracownika zawiera się w przedziale (5000, 10000] PLN to pracownik otrzyma tylko pierwszą podwyżkę.
    // - Jeżeli po pierwszej podwyżce wynagrodzenie pracownika zawiera się w przedziale (0, 5000] PLN to pracownik otrzyma pierwszą oraz drugą podwyżkę.
    // Ile będzie wynosić kwota podwyżki dla pracownika, który zarabia 3000 PLN zakładając, że każda podwyżka ma ten sam wzrost 10%?
    public static double changeSalaryTwice_ExecuteQuerySavepoint(double salaryRise, int employee) {

        double updatedSalary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Integer sleep = 15; // Czas uśpienia pozwalający na obserwację nałożonych w bazie danych blokad (PgAdmin - Narzędzia - Status serwera).

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Ustawienie połączenia w tryb nieautomatycznego zatwierdzania/wycofywania transakcji tj. AutoCommit = false.
            connection.setAutoCommit(false);
            logger.info("AutoCommit mode is set to {}.", connection.getAutoCommit());

            // Start operacji U1
            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            try {
                rs = pstmt.executeQuery();
                rs.beforeFirst();
                if (rs.next()) {
                    updatedSalary = rs.getDouble(6);

                    // Punkt zachowania
                    Savepoint sp = connection.setSavepoint("S1");
                    if (updatedSalary > 10000) {
                        logger.info("Constraint violation. Updated salary = {} is greater than or equal to upper limit (10000 PLN). Modification not saved.", updatedSalary);
                        TimeUnit.SECONDS.sleep(sleep);
                        connection.rollback();  // Wycofanie wprowadzonych zmian może być wykonywane w trybie nieautomatycznego zatwierdzania transakcji.
                        updatedSalary = -1;
                    } else {

                        logger.info("Salary after first rise = {}.", updatedSalary);
                        // Start operacji U2
                        sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING placa_zasadnicza";

                        //pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.clearParameters();
                        pstmt.setDouble(1, salaryRise);
                        pstmt.setInt(2, employee);

                        try {
                            rs = pstmt.executeQuery();
                            rs.beforeFirst();
                            if (rs.next()) {
                                // Poniższy zapis odwołuje się jawnie, poprzez nazwę do zwracanej poprzez UPDATE wartości.
                                updatedSalary = rs.getDouble("placa_zasadnicza");
                            }
                        } catch (Exception e) {
                            // W przypadku działania w trybie nieautomatycznego zatwierdzania/wycofywania transakcji tj. AutoCommit = false należy w przypadku wystąpienia wyjątków samodzielnie obsługiwać wycofywanie ewentualnych zmian wprowadzonych w ramach transakcji.
                            TimeUnit.SECONDS.sleep(sleep);
                            connection.rollback();
                            logger.error("Exception occurred therefore update was rollbacked.", e);
                        }
                        // Koniec operacji U2

                        if (updatedSalary > 5000) {
                            logger.info("Constraint violation. Updated salary = {} is greater than or equal to middle limit (5000 PLN). Modification not saved.", updatedSalary);
                            TimeUnit.SECONDS.sleep(sleep);
                            connection.rollback(sp); // Wycofanie transakcji do punktu zachowania może być wykonane tylko w trybie nieautomatycznego zatwierdzania transakcji.
                            connection.commit();
                            updatedSalary = -1;
                        } else {
                            TimeUnit.SECONDS.sleep(sleep);
                            connection.commit();
                            logger.info("Updated salary = {}.", updatedSalary);
                        }
                    }
                } else {
                    logger.info("No employees salaries were updated.");
                    // Czy ma sens wykonanie zatwierdzenia transakcji, gdy żaden rekord nie został zmieniony?
                    connection.commit();
                }
            } catch (Exception e) {
                // W przypadku działania w trybie nieautomatycznego zatwierdzania/wycofywania transakcji tj. AutoCommit = false należy w przypadku wystąpienia wyjątków samodzielnie obsługiwać wycofywanie ewentualnych zmian wprowadzonych w ramach transakcji.
                TimeUnit.SECONDS.sleep(sleep);
                connection.rollback();
                logger.error("Exception occurred therefore update was rollbacked.", e);
            }
        } catch (Exception e) {
            logger.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return updatedSalary;
    }
}