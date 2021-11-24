package laboratory;

import java.sql.*;
import java.util.*;

public class DatabaseResults {

    private Connection connection;
    private String productName;
    private String productVersion;
    private int columnCount;
    private String[] columnNames;
    private ArrayList queryResults;
    String[] rowData;

    public DatabaseResults(Connection connection, String productName, String productVersion, int columnCount, String[] columnNames) {
        this.connection = connection;
        this.productName = productName;
        this.productVersion = productVersion;
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        rowData = new String[columnCount];
        queryResults = new ArrayList();
    }

    public Connection getConnection() {
        return (connection);
    }

    public String getProductName() {
        return (productName);
    }

    public String getProductVersion() {
        return (productVersion);
    }

    public int getColumnCount() {
        return (columnCount);
    }

    public String[] getColumnNames() {
        return (columnNames);
    }

    public int getRowCount() {
        return (queryResults.size());
    }

    public String[] getRow(int index) {
        return ((String[]) queryResults.get(index));
    }

    public void addRow(String[] row) {
        queryResults.add(row);
    }

    public String displayHTML(String tableTitle) throws Exception {
        StringBuilder buffer = new StringBuilder();

        buffer.append("<TABLE align=\"center\" cellSpacing=\"0\" cellPadding=\"5\" border=\"1\" bordercolor=\"FFFFFF\" bgcolor=\"#CCCCCC\" width=\"90%\">");

        // TABLE TITLE
        buffer.append("<TR><TH height=\"30\" colspan=\"");
        buffer.append(Integer.toString(this.getColumnCount() + 1));
        buffer.append("\">");
        buffer.append(tableTitle.toUpperCase());
        buffer.append("</TH></TR>");

        // COLUMN HEADER
        buffer.append("<TR><TH>L.P.</TH>");

        String[] header = this.getColumnNames();
        String columnTitle;

        for (int col = 0; col < this.getColumnCount(); col++) {
            buffer.append("<TH>");
            buffer.append(header[col]);
            buffer.append("</TH>");
        }
        buffer.append("</TR>");

        // DATA/ROWS
        for (int row = 0; row < this.getRowCount(); row++) {
            buffer.append("<TR><TD align=\"center\" title=\"\">");
            buffer.append(Integer.toString(row + 1));
            buffer.append("</TD>");
            for (int col = 0; col < getColumnCount(); col++) {
                String[] record = this.getRow(row);
                columnTitle = "Row no. " + Integer.toString(row + 1);
                buffer.append("<TD align=\"center\" title=\"");
                buffer.append(columnTitle);
                buffer.append("\">");
                buffer.append(record[col]);
                buffer.append("</TD>");
            }
            buffer.append("</TR>");
        }

        buffer.append("</TABLE>");

        return (buffer.toString());
    }
}