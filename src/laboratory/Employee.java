package laboratory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
// import com.sun.rowset.*;

import static laboratory.Laboratory.JDBC_DRIVER;
import static laboratory.Laboratory.DB_URL;
import static laboratory.Laboratory.USER;
import static laboratory.Laboratory.PASS;

public class Employee {

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

    // 2021-01-17 @TP
    public static DatabaseResults getEmployees() {

        DatabaseResults employees = null;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            employees = DatabaseUtilities.getQueryResults("SELECT id_prowadzacego AS \"ID\", * FROM kadry.prowadzacy", connection);

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing connection. Exception: " + e);
            }
        }
        return employees;
    }

    // 2021-01-15 @TP
    public static List<Employee> getEmployees_Statement() {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            String sql = "SELECT * FROM kadry.prowadzacy ORDER BY 1";
            stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            stmt.setFetchSize(100);

            long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);

            // Information about supported modes
            boolean isCloseAtCommitSupported = connection.getMetaData().supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
            System.out.println(isCloseAtCommitSupported ? "Information. Database does support CLOSE_CURSORS_AT_COMMIT" : "Information. Database does NOT support CLOSE_CURSORS_AT_COMMIT");
            boolean isSensitiveSupported = connection.getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
            System.out.println(isSensitiveSupported ? "Information. Database does support TYPE_SCROLL_SENSITIVE" : "Information. Database does NOT support TYPE_SCROLL_SENSITIVE");

            while (rs.next()) {
                Employee row = new Employee(rs.getInt("id_prowadzacego"), rs.getString("imie"), rs.getString("nazwisko"), rs.getString("tytul"), rs.getString("stanowisko"), rs.getDouble("placa_zasadnicza"), rs.getInt("id_jednostki_zatrudniajacej"));
                employeeList.add(row);
            }
            connection.commit();
            long endTime = System.currentTimeMillis();

            System.out.println("Execution time " + (endTime - startTime) + " ms");

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return employeeList;
    }

    // 2021-01-15 @TP
    public static List<Employee> getEmployees_StatementSensitive() {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            // Information about supported modes
            boolean isCloseAtCommitSupported = connection.getMetaData().supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
            System.out.println(isCloseAtCommitSupported ? "Information. Database does support CLOSE_CURSORS_AT_COMMIT" : "Information. Database does NOT support CLOSE_CURSORS_AT_COMMIT");
            boolean isSensitiveSupported = connection.getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
            System.out.println(isSensitiveSupported ? "Information. Database does support TYPE_SCROLL_SENSITIVE" : "Information. Database does NOT support TYPE_SCROLL_SENSITIVE");

            String sql = "SELECT * FROM kadry.prowadzacy ORDER BY 1";
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            stmt.setFetchSize(100);

            long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // U??pienie wykorzystywane do badania w??asno??ci TYPE_SCROLL_SENSITIVE
                TimeUnit.MILLISECONDS.sleep(250);
                Employee row = new Employee(rs.getInt("id_prowadzacego"), rs.getString("imie"), rs.getString("nazwisko"), rs.getString("tytul"), rs.getString("stanowisko"), rs.getDouble("placa_zasadnicza"), rs.getInt("id_jednostki_zatrudniajacej"));
                employeeList.add(row);
                System.out.println("Imi??: " + row.firstName);
            }
            long endTime = System.currentTimeMillis();
            connection.commit();
            System.out.println("Execution time " + (endTime - startTime) + " ms");

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return employeeList;
    }

    // 2021-01-17 @TP
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
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.FETCH_FORWARD);
            rs = stmt.executeQuery(sql);

            rs.beforeFirst();
            if (rs.next()) {
                salary = rs.getDouble("placa_zasadnicza");
            } else {
                System.err.println("Information SQL. No rows were fetched.");
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (stmt != null && !stmt.isClosed()) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return salary;
    }

    // 2021-01-17 @TP
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
                System.err.println("Information SQL. No rows were fetched.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return salary;
    }

    // 2021-01-16 @TP
    public static List<Employee> getEmployeesSalary_PreparedStatementResultSet(int employeeLowerBound, int employeeUpperBound) {

        //double salary = -1;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<Employee>();

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT id_prowadzacego, placa_zasadnicza FROM kadry.prowadzacy WHERE id_prowadzacego BETWEEN ? AND ?";
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.FETCH_REVERSE);
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

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return employeeList;
    }

    // 2021-01-16 @TP
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

            // Przegl??danie zmienionych wynagrodze??
            rs = pstmt.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                // Co oznacza warto???? 6 w poni??szej metodzie getDouble(6)?
                changedSalary = rs.getDouble(6);
                System.out.println("Information. Updated salary = " + changedSalary);
            } else {
                System.err.println("Information. No records were updated.");
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & pstmt & connection. Exception: " + e);
            }
        }
        return changedSalary;
    }

    // 2021-01-16 @TP
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
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.FETCH_FORWARD);
            pstmt.clearParameters();
            pstmt.setInt(1, employee);

            rs = pstmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                salary = rs.getDouble("placa_zasadnicza");
                System.out.println("Information. Current salary = " + salary);
                changedSalary = (1 + salaryRise) * salary;
                rs.updateDouble("placa_zasadnicza", changedSalary);
                rs.updateRow();
                System.out.println("Information. Updated salary = " + changedSalary);
            }
            // rs.moveToInsertRow();

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return changedSalary;
    }

    // 2021-01-16 @TP
    public static double addEmployeePayment_PreparedStatementResultSet(double salary, int month, int year, String category, int employee) {

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM kadry.wyplaty WHERE id_prowadzacego = ? AND rok = ? AND miesiac = ? AND kategoria_wyplaty = ?";
            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.FETCH_FORWARD);
            pstmt.clearParameters();
            pstmt.setInt(1, employee);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            pstmt.setString(4, category);

            rs = pstmt.executeQuery();
            rs.beforeFirst();
            if (!rs.next()) {
                rs.moveToInsertRow();
                rs.updateInt("id_prowadzacego", employee);
                rs.updateInt("rok", year);
                rs.updateInt("miesiac", month);
                rs.updateString("kategoria_wyplaty", category);
                rs.updateDate("data_wyplaty", new java.sql.Date(System.currentTimeMillis()));
                rs.updateDouble("kwota", salary);
                rs.insertRow();
                // Dodanie rekordu do DB?
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    // Dodanie rekordu do DB?                    
                }
            } catch (Exception e) {
                System.out.println("Error. Closing rs & stmt & connection. Exception: " + e);
            }
        }
        return salary;
    }

    // 2021-01-16 @TP @TODO
    // Aktualizacja wynagrodzenia pracownik??w, kt??rzy zarabiaj?? nie wi??cej ni?? warto???? parametru salaryBound
    public static int changeEmployeeSalary_PreparedStatementViaExecuteUpdate(double salaryRise, int salaryBound) {

        int rowsUpdatedCount = -1;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            PreparedStatement pstmt;
            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE placa_zasadnicza <= ?";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, salaryBound);

            try {
                // W niniejszej metodzie wykonywane jest executeUpdate(). Jaka metoda by??a wykorzystwana wcze??niej?
                rowsUpdatedCount = pstmt.executeUpdate();
                if (rowsUpdatedCount > 0) {
                    System.out.println("Information. Updated salary for " + rowsUpdatedCount + " employees.");
                } else {
                    System.err.println("Information. No records were updated.");
                }
            } catch (Exception e) {
                System.err.println("Error. Update failed. Exception: " + e);
            } finally {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    System.err.println("Error. Closing rs & pstmt. Exception: " + e);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error. Closing connection failed. Exception: " + e);
            }
        }
        return rowsUpdatedCount;
    }

    // 2021-01-17 @TP
    public static double changeSalary_RollbackError(double salaryRise, int employee) {

        double updatedSalary = -1;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            PreparedStatement pstmt;
            ResultSet rs = null;
            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            System.out.println("Autocommit mode is set to " + connection.getAutoCommit());

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            try {
                rs = pstmt.executeQuery();
                rs.beforeFirst();
                if (rs.next()) {
                    // Co oznacza warto???? 6 w poni??szej metodzie getDouble(6)?
                    updatedSalary = rs.getDouble(6);
                    if (updatedSalary > 10000) {
                        System.out.println("Constraint violation. Updated salary = " + updatedSalary + " is greater than upper limit.");
                        connection.rollback(); // @TP Notice: Rollback cannot be executed in Autocommit = TRUE mode.
                        updatedSalary = -1;
                    } else {
                        System.out.println("Information. Updated salary = " + updatedSalary);
                    }
                } else {
                    System.err.println("Information. No records were updated.");
                }
            } catch (Exception e) {
                System.err.println("Error. Update failed. Exception: " + e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    pstmt.close();
                } catch (Exception e) {
                    System.err.println("Error. Closing rs & pstmt. Exception: " + e);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error. Closing connection. Exception: " + e);
            }
        }
        return updatedSalary;
    }

    // 2021-01-17 @TP
    public static double changeSalary_ExecuteQueryRollback(double salaryRise, int employee) {

        double updatedSalary = -1;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            // Ustawienie trybu AUTOCOMMIT na tryb pe??nej kontroli zatwiedzania oraz wycofywania transakcji.
            connection.setAutoCommit(false);

            PreparedStatement pstmt;
            ResultSet rs = null;
            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            try {
                rs = pstmt.executeQuery();
                rs.beforeFirst();
                // W celu obserwcji z u??yciem PgAdmina (Narz??dzia - Status serwera) na??o??onych w bazie danych blokad, mo??na po uruchomieniu transakcji wstrzyma?? jej zako??czenie poni??szym poleceniem.
                // Poni??sze u??pienie konieczne jest tak??e dla sprawdzenia dzia??ania przyk??adu nr 20
                if (rs.next()) {
                    updatedSalary = rs.getDouble(6);
                    if (updatedSalary > 10000) {
                        System.out.println("Constraint violation. Updated salary = " + updatedSalary + " is greater than upper limit.");
                        TimeUnit.SECONDS.sleep(15);
                        connection.rollback(); // @TP Notice: Rollback can be executed in Autocommit = FALSE mode.
                        updatedSalary = -1;
                    } else {
                        TimeUnit.SECONDS.sleep(15);
                        connection.commit();
                        System.out.println("Information. Updated salary = " + updatedSalary);
                    }
                } else {
                    System.err.println("Information. No records were updated.");
                    // Czy jest sens wykonywa?? COMMIT albo ROLLBACK gdy ??aden rekord nie zosta?? zmieniony?
                    // connection.commit(); // connection.rollback();
                }
            } catch (Exception e) {
                // W przypadku dzia??ania w trybie pe??nej kontroli nad transakcjami tj. AUTOCOMMIT = FALSE 
                // nale??y samodzielnie obs??ugiwa?? wycofywanie dzia??ania transakcji w przypadku wyst??pienia wyj??tk??w.
                TimeUnit.SECONDS.sleep(15);
                connection.rollback();
                System.err.println("Error. Update failed. Exception: " + e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    pstmt.close();
                } catch (Exception e) {
                    System.err.println("Error. Closing rs & pstmt. Exception: " + e);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (connection != null) {
                    // Bardzo wa??ne jest, aby po zako??czeniu transakcji ustawi?? z powrotem tryb AUTOCOMMIT = FALSE;.
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error. Setting AutoCommit failed. Exception: " + e);
            }
        }
        return updatedSalary;
    }

    // 2021-11-24 @TP
    // Algorytm przyznania podwy??ki:
    // - Je??eli wynagrodzenie pracownika po 1-szej podwy??ce przekracza 10000 PLN to zostanie ona cofni??ta (pracownik nie otrzyma pierwszej podwy??ki).
    // - Je??eli wynagrodzenie pracownika po 1-szej podwy??ce zawiera si?? w przedziale (5000, 10000] PLN to pracownik otrzyma tylko pierwsz?? podwy??k??.
    // - Je??eli wynagrodzenie pracownika po 1-szej podwy??ce zawiera si?? w przedziale (0, 5000] PLN to pracownik otrzyma pierwsz?? oraz drug?? podwy??k??.
    // Ile b??dzie wynosi?? kwota podwy??ki dla pracownika, kt??ry zarabia 3000 z?? zak??adaj??c, ??e ka??da podwy??ka ma ten sam wzrost 10%?
    public static double changeSalaryTwice_ExecuteQuerySavepoint(double salaryRise, int employee) {

        double updatedSalary = -1;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            // Ustawienie trybu AUTOCOMMIT na tryb pe??nej kontroli zatwiedzania oraz wycofywania transakcji.
            connection.setAutoCommit(false);
            PreparedStatement pstmt;
            ResultSet rs = null;

            // Start operacji U1
            String sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING *";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setDouble(1, salaryRise);
            pstmt.setInt(2, employee);

            try {
                rs = pstmt.executeQuery();
                rs.beforeFirst();
                // W celu obserwcji z u??yciem PgAdmina (Narz??dzia - Status serwera) na??o??onych w bazie danych blokad, mo??na po uruchomieniu transakcji wstrzyma?? jej zako??czenie poni??szym poleceniem.
                // TimeUnit.SECONDS.sleep(15);
                if (rs.next()) {
                    updatedSalary = rs.getDouble(6);

                    // Punkt zachowania
                    Savepoint sp = connection.setSavepoint("S1");
                    if (updatedSalary > 10000) {
                        System.out.println("Constraint violation. Updated salary = " + updatedSalary + " is greater than upper limit. Change not saved.");
                        // W przypadku dzia??ania w trybie pe??nej kontroli nad transakcjami tj. AUTOCOMMIT = FALSE
                        // nale??y samodzielnie obs??ugiwa?? wycofywanie dzia??ania transakcji w przypadku wyst??pienia wyj??tk??w.
                        connection.rollback();
                        updatedSalary = -1;
                    } else {

                        // Start operacji U2
                        sql = "UPDATE kadry.prowadzacy SET placa_zasadnicza = (1 + ?) * placa_zasadnicza WHERE id_prowadzacego = ? RETURNING placa_zasadnicza";

                        pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.clearParameters();
                        pstmt.setDouble(1, salaryRise);
                        pstmt.setInt(2, employee);

                        try {
                            rs = pstmt.executeQuery();
                            if (rs.next()) {
                                // Poni??szy zapis odwo??uje si?? jawnie, poprzez nazw?? do zwracanej poprzez UPDATE warto??ci.
                                updatedSalary = rs.getDouble("placa_zasadnicza");
                            }
                        } catch (Exception e) {
                            // W przypadku dzia??ania w trybie pe??nej kontroli nad transakcjami tj. AUTOCOMMIT = FALSE
                            // nale??y samodzielnie obs??ugiwa?? wycofywanie dzia??ania transakcji w przypadku wyst??pienia wyj??tk??w.
                            connection.rollback();
                            System.err.println("Error. Update failed. Exception: " + e);
                        }
                        // Koniec operacji U2

                        if (updatedSalary > 5000) {
                            System.out.println("Constraint violation. Updated salary = " + updatedSalary + " is greater than middle limit. Change not saved.");
                            connection.rollback(sp); // @TP Notice: Rollback can be executed in Autocommit = FALSE mode.
                            connection.commit();
                            updatedSalary = -1;
                        } else {
                            connection.commit();
                            System.out.println("Information. Updated salary = " + updatedSalary);
                        }
                    }
                } else {
                    System.err.println("Information. No records were updated.");
                    // Czy jest sens wykonywa?? COMMIT albo ROLLBACK gdy ??aden rekord nie zosta?? zmieniony?
                    // connection.commit(); / connection.rollback();
                }
            } catch (Exception e) {
                // W przypadku dzia??ania w trybie pe??nej kontroli nad transakcjami tj. AUTOCOMMIT = FALSE
                // nale??y samodzielnie obs??ugiwa?? wycofywanie dzia??ania transakcji w przypadku wyst??pienia wyj??tk??w.
                connection.rollback();
                System.err.println("Error. Update failed. Exception: " + e);
                // Koniec operacji U1

            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    pstmt.close();
                } catch (Exception e) {
                    System.err.println("Error. Closing rs & pstmt. Exception: " + e);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL. Exception: " + e);
        } catch (Exception e) {
            System.err.println("Error. Exception: " + e);
        } finally {
            try {
                if (connection != null) {
                    // Bardzo wa??ne jest, aby po zako??czeniu transakcji ustawi?? z powrotem tryb AUTOCOMMIT = FALSE;.
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println("Error. Setting AutoCommit failed. Exception: " + e);
            }
        }
        return updatedSalary;
    }
}