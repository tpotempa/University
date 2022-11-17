package laboratory;

import java.util.*;

public class DatabaseResults {
    private final int columnCount;
    private final String[] columnNames;
    private final ArrayList<String[]> queryResults;
    String[] rowData;

    public DatabaseResults(int columnCount, String[] columnNames) {
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        rowData = new String[columnCount];
        queryResults = new ArrayList<>();
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
        return queryResults.get(index);
    }

    public void addRow(String[] row) {
        queryResults.add(row);
    }

    public String displayHTML(String tableTitle) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("<table align=\"center\" cellSpacing=\"0\" cellPadding=\"5\" border=\"1\" bordercolor=\"FFFFFF\" bgcolor=\"#CCCCCC\" width=\"90%\">");

        // Tytuł tabeli.
        buffer.append("<tr><th height=\"30\" colspan=\"");
        buffer.append(this.getColumnCount() + 1);
        buffer.append("\">");
        buffer.append(tableTitle.toUpperCase());
        buffer.append("</th></tr>");

        // Nagłówek kolumny.
        buffer.append("<tr><th>LP.</th>");

        String[] header = this.getColumnNames();
        String columnTitle;

        for (int col = 0; col < this.getColumnCount(); col++) {
            buffer.append("<th>");
            buffer.append(header[col]);
            buffer.append("</th>");
        }
        buffer.append("</tr>");

        // Wiersze tabeli.
        for (int row = 0; row < this.getRowCount(); row++) {
            buffer.append("<tr><td align=\"center\" title=\"\">");
            buffer.append(row + 1);
            buffer.append("</td>");
            for (int col = 0; col < getColumnCount(); col++) {
                String[] record = this.getRow(row);
                columnTitle = "Row no. " + (row + 1);
                buffer.append("<td align=\"center\" title=\"");
                buffer.append(columnTitle);
                buffer.append("\">");
                buffer.append(record[col]);
                buffer.append("</td>");
            }
            buffer.append("</tr>");
        }

        buffer.append("</table>");

        return (buffer.toString());
    }
}