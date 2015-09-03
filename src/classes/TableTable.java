package classes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

public class TableTable extends JFrame {

     public static void createGUI() {
          JFrame frame = new JFrame("Test frame");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

          String[] columnNames = {
                    "Name",
                    "Last modified",
                    "Type",
                    "Size"
          };
          
          String[][] data = {
                    {"addins", "02.11.2006 19:15", "Folder", ""},
                    {"AppPatch", "03.10.2006 14:10", "Folder", ""},
                    {"assembly", "02.11.2006 14:20", "Folder", ""},
                    {"Boot", "13.10.2007 10:46", "Folder", ""},
                    {"Branding", "13.10.2007 12:10", "Folder", ""},
                    {"Cursors", "23.09.2006 16:34", "Folder", ""},
                    {"Debug", "07.12.2006 17:45", "Folder", ""},
                    {"Fonts", "03.10.2006 14:08", "Folder", ""},
                    {"Help", "08.11.2006 18:23", "Folder", ""}
          };
          
          JPanel mainPanel = new JPanel();
          mainPanel.setLayout(new BorderLayout());
          
          final JTable table = new JTable(data, columnNames);          
          JScrollPane scrollPane = new JScrollPane(table);
          mainPanel.add(scrollPane, BorderLayout.CENTER);
          
          JPanel bottomPanel = new JPanel();
          bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));          
          
          JLabel selLabel = new JLabel("Selected:");
          bottomPanel.add(selLabel);
          
          final JLabel currentSelectionLabel = new JLabel("");
          currentSelectionLabel.setAutoscrolls(true);          
          bottomPanel.add(currentSelectionLabel);
          
          mainPanel.add(bottomPanel, BorderLayout.SOUTH);
          
          ListSelectionModel selModel = table.getSelectionModel();
          
          selModel.addListSelectionListener(new ListSelectionListener() {               
               public void valueChanged(ListSelectionEvent e) {
                    String result = "";
                    int[] selectedRows = table.getSelectedRows();
                    for(int i = 0; i < selectedRows.length; i++) {
                         int selIndex = selectedRows[i];
                         TableModel model = table.getModel();
                         Object value = model.getValueAt(selIndex, 0);
                         result = result + value;
                         if(i != selectedRows.length-1) {
                               result += ", ";
                         }
                    }
                    currentSelectionLabel.setText(result);                    
               }               
          });
          
          frame.getContentPane().add(mainPanel);
          
          frame.setPreferredSize(new Dimension(550, 200));
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
     }

     public static void main(String[] args) {
          javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    createGUI();
               }
          });
     }
}