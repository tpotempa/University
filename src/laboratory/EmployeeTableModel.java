package laboratory;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EmployeeTableModel extends AbstractTableModel
{
    private final List<Employee> employeeList;
    
    private final String[] columnNames = new String[] {
            "ID", "Imię", "Nazwisko", "Tytuł", "Stanowisko", "Wynagrodzenie", "Jednostka"
    };
    
    private final Class<?>[] columnClass = new Class[] {
        Integer.class, String.class, String.class, String.class, String.class, Double.class, Integer.class
    };

    public EmployeeTableModel(List<Employee> employeeList)
    {
        this.employeeList = employeeList;
    }
    
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return employeeList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Employee row = employeeList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getFirstName();
        }
        else if(2 == columnIndex) {
            return row.getLastName();
        }
        else if(3 == columnIndex) {
            return row.getTitle();
        }
        else if(4 == columnIndex) {
            return row.getPosition();
        }
        else if(5 == columnIndex) {
            return row.getSalary();
        }
        else if(6 == columnIndex) {
            return row.getDepartmentId();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Employee row = employeeList.get(rowIndex);
        if(0 == columnIndex) {
            row.setId((Integer) aValue);
        }
        else if(1 == columnIndex) {
            row.setFirstName((String) aValue);
        }
        else if(2 == columnIndex) {
            row.setLastName((String) aValue);
        }
        else if(3 == columnIndex) {
            row.setTitle((String) aValue);
        }
        else if(4 == columnIndex) {
            row.setPosition((String) aValue);
        }
        else if(5 == columnIndex) {
            row.setSalary((Double) aValue);
        }
        else if(6 == columnIndex) {
            row.setDepartmentId((Integer) aValue);
        }
    }
    
}