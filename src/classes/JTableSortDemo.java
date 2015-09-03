package classes;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JTableSortDemo {
  public static void main(String[] args) {
    Object[][] data = { { "A", 5 }, { "B", 2 }, { "C", 4 }, { "D", 8 } };
    String columnNames[] = { "Item", "Value" };
    TableModel model = new DefaultTableModel(data, columnNames) {
      public Class<?> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
      }
    };
    JTable table = new JTable(model);

    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
    table.setRowSorter(sorter);

    JScrollPane scrollPane = new JScrollPane(table);
    JFrame frame = new JFrame("Sorting Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(scrollPane);
    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}