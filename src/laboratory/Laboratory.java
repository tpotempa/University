package laboratory;

import static laboratory.Employee.*;
import static laboratory.DatabaseInformation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.Component;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Laboratory extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(Laboratory.class);
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://195.150.230.210:5434/2021_potempa_tomasz";
    static final String USER = "2021_potempa_tomasz";
    static final String PASS = "12345";
    static final int POOL_SIZE = 5;

    public Laboratory(String description, Component relativePosition, List<Employee> employeeList) {
        EmployeeTableModel model = new EmployeeTableModel(employeeList);
        JTable table = new JTable(model);
        this.add(new JScrollPane(table));
        if (relativePosition == null) {
            this.setLocation(0, 0);
        } else {
            this.setLocationRelativeTo(relativePosition);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        this.setTitle("Employees table {" + description + "} at " + sdf.format(new Date(System.currentTimeMillis())));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {

        // Numer przykładu
        int example = 50;

        switch (example) {
            case 1: {
                // Przykład #1 :: Podstawowe informacje o JDBC, połączeniu z bazą danych oraz aktualnym i domyślnym poziomie izolacji transakcji
                logger.info(getDriverVersion());
                logger.info(getTransactionIsolationLevels());
                break;
            }
            case 2: {
                // Przykład #2 :: Informacje o wspieranych poziomach izolacji transakcji
                getSupportedTransactionIsolationLevels();
                break;
            }
            case 3: {
                // Przykład #3 :: Informacje o wspieranych własnościach zbioru rezultatów
                getSupportedResultSetModes();
                break;
            }

            case 10: {
                // Przykład #10 :: Odczyt danych z tabeli z użyciem Statement
                SwingUtilities.invokeLater(() -> {
                    List<Employee> employeeList = getEmployees_Statement();
                    new Laboratory("Transaction T1", null, employeeList);
                });
                break;
            }
            case 11: {
                // Przykład #11 :: Odczyt i zmiana danych z tabeli z użyciem Statement z obserwowaniem widoczności zmian
                SwingUtilities.invokeLater(() -> {
                    List<Employee> employeeList = getEmployees_Statement_UpdateVisibility(5, 5);
                    new Laboratory("Transaction T1", null, employeeList);
                });
                break;
            }
            case 12: {
                // Przykład #12 :: Generyczny dostęp do danych, be znajomości struktury tabeli.
                try {
                    System.out.println(getEmployees().displayHTML("University employees"));
                } catch (Exception e) {
                    logger.error("A generic exception occurred.", e);
                }
                break;
            }
            case 13: {
                // Przykład #13 :: Dostęp do danych z pełną kontrolą via Statement. Rezultat jest pojedynczą wartością.
                logger.info("Salary for a given employee ID is {}.", getEmployeeSalary_Statement(1));
                break;
            }
                
            case 20: {
                // Przykład #20 :: Dostęp do danych z pełną kontrolą via PreparedStatement. Rezultat jest pojedynczą wartością.
                logger.info("Salary for a given employee ID is {}.", getEmployeeSalary_PreparedStatement(1));
                break;
            }
            case 21: {
                // Przykład #21 :: Dostęp do danych z pełną kontrolą via PreparedStatement. Rezultat jest kolekcją, w tym przypadku zbiorem wartości.
                int lowerBound = 4;
                int upperBound = 7;
                logger.info("Salary for employees with ID between LOWER_BOUND = {} and UPPER_BOUND = {}.", lowerBound, upperBound);
                List<Employee> employeeList = getEmployeesSalary_PreparedStatementResultSet(lowerBound, upperBound);
                for (Employee employee : employeeList) {
                    logger.info("Salary for a employee ID = {} is {}.", employee.getId(), employee.getSalary());
                }
                break;
            }
            case 22: {
                // Przykład #22 :: Zmiana danych z pełną kontrolą via PreparedStatement.
                // Zmiana danych jest wykonywana poprzez UPDATE. Dodatkowo zmienione dane są odczytywane do ResultSet. Zmiana danych następuje z wykorzystaniem executeQuery().
                logger.info("Modified salary for a given employee ID is now {}.", changeEmployeeSalary_PreparedStatement(0.1, 1));
                break;
            }
            case 23: {
                // Przykład #23 :: Zmiana danych z pełną kontrolą via PreparedStatement i ResultSet.
                // Zmiana danych jest wykonywana poprzez SELECT. Zmienione dane są odczytywane do ResultSet i w ResultSet zmieniane. Zmiana danych następuje z wykorzystaniem updateRow().
                logger.info("Modified salary for a given employee ID is now {}.", changeEmployeeSalary_PreparedStatementResultSet(0.1, 1));
                break;
            }
            case 24: {
                // Przykład #24 :: Zmiana danych z pełną kontrolą via PreparedStatement z wykorzystaniem executeUpdate().
                // Zmiana danych jest wykonywana poprzez UPDATE. Zmiana danych następuje z wykorzystaniem executeUpdate().
                logger.info("Salary was modified for {} employees.", changeEmployeeSalary_PreparedStatementViaExecuteUpdate(0.1, 2000));
                break;
            }
            case 25: {
                // Przykład #25 :: Zmiana danych z pełną kontrolą via PreparedStatement. Analiza trybu AUTCOMMIT = TRUE tj. automatycznego zatwierdzania transakcji.
                // Zmiana danych jest wykonywana poprzez UPDATE. Dodatkowo zmienione dane są odczytywane do ResultSet.
                // Metoda powinna dokonać zmiany wynagrodzenia tylko w przypadku, gdy zmienione wynagrodzenie nie przekroczy limitu 10 000 PLN.
                // Jakie jest wynagrodzenie pracownika ID = 54 po wykonaniu metody?
                logger.info("Modified salary for a given employee ID is now {}.", changeSalary_RollbackError(0.25, 54));
                break;
            }
            case 26: {
                // Przykład #26 :: Zmiana danych z pełną kontrolą via PreparedStatement. Zmiana danych odbywa się w trybie AUTOCOMMIT = FALSE tj. nieautomatycznego zatwierdzania transakcji.
                // Zmiana danych jest wykonywana poprzez UPDATE. Dodatkowo zmienione dane są odczytywane do ResultSet.
                // Metoda powinna dokonać zmiany wynagrodzenia tylko w przypadku, gdy zmienione wynagrodzenie nie przekroczy limitu 10 000 PLN.
                double changedSalary = changeSalary_ExecuteQueryRollback(0.25, 54);
                if(changedSalary == -1)
                    logger.info("The salary was not changed.");
                else
                    logger.info("Modified salary for a given employee ID is now {}.", changedSalary);
                break;
            }
            case 27: {
                // Przykład #27 :: Kontrola nad operacjami z użyciem punktu zachowania
                long startTime = System.currentTimeMillis();
                int salaryChangesCount = changeSalary_ExecuteQuerySavepoint(0.1, 19);
                logger.info("The salary was changed {} times.", salaryChangesCount);
                long endTime = System.currentTimeMillis();
                logger.info("Execution time {} ms.", (endTime - startTime));
                break;
            }
            case 28: {
                // Przykład #28 :: Dodanie danych via SELECT
                if(addEmployeePayment_PreparedStatementResultSet(5555, 10, 2019, "wynagrodzenie miesięczne", 1)) {
                    logger.info("Monthly payment was added.");
                }
                break;
            }

            case 40: {
                // Przykład #40
                // Obserwacja widoczności zmiany wprowadzanych przez metodę changeSalary_ExecuteQueryRollback()
                SwingUtilities.invokeLater(() -> {
                    List<Employee> employeeList1 = getEmployees_Statement();
                    Laboratory tableT1 = new Laboratory("Transaction T1", null, employeeList1);
                    changeSalary_ExecuteQueryRollback(0.25, 20);
                    List<Employee> employeeList2 = getEmployees_Statement();
                    Laboratory tableT2 = new Laboratory("Transaction T2", tableT1, employeeList2);
                    changeSalary_ExecuteQueryRollback(0.25, 20);
                    List<Employee> employeeList3 = getEmployees_Statement();
                    new Laboratory("Transaction T3", tableT2, employeeList3);
                });
                break;
            }
                
            case 50: {
                // Przykład #50 :: Używanie puli połączeń
                // Inicjalizacja puli.
                ConnectionPool cp = new ConnectionPool();
                try {
                    Class.forName(JDBC_DRIVER);
                    logger.info(getDriverVersion());
                    try {
                        cp.setDriver(JDBC_DRIVER);
                        cp.setURL(DB_URL);
                        cp.setUsername(USER);
                        cp.setPassword(PASS);
                        cp.setSize(POOL_SIZE);
                        cp.initializePool();
                        logger.info("Connection pool of size {} on {} using {} was initialized.", cp.getSize(), cp.getURL(), cp.getDriver());
                    } catch (Exception e) {
                        logger.error("A generic exception occurred.", e);
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("An exception occurred while loading JDBC class.", e);
                }

                // Użycie połączeń z puli.
                java.sql.Connection con1 = null;
                java.sql.Connection con2 = null;
                java.sql.Connection con3 = null;
                java.sql.Connection con4 = null;
                java.sql.Connection con5 = null;
                java.sql.Connection con6 = null;
                try {
                    con1 = cp.getConnection();
                    con2 = cp.getConnection();
                    con3 = cp.getConnection();
                    con4 = cp.getConnection();
                    con5 = cp.getConnection();
                    con6 = cp.getConnection();
                    // Obsługa transakcji
                    // ...
                    // ...
                } catch (Exception e) {
                    logger.error("An exception occurred while getting a connection.", e);
                } finally {
                    cp.releaseConnection(con1);
                    cp.releaseConnection(con2);
                    cp.releaseConnection(con3);
                    cp.releaseConnection(con4);
                    cp.releaseConnection(con5);
                    cp.releaseConnection(con6);
                }

                // Zamknięcie puli.
                cp.emptyPool();
                break;
            }
            default: {
                logger.info("No choice has been made.");
            }
        }
    }
}