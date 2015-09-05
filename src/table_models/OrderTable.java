package table_models;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import mai_n.MySQLConnector;



public class OrderTable extends DefaultTableModel{
    private static final String[] colNames = {"Код товара", "Наименование", "Категория", "Количество", "Удалить"};
    private final String orderProdsNameTable = "OrdProd";
    private final String orderListNameTable = "OrdList";
    
    //private final String[] colNames = {"Код товара", "Наименование", "Категория", "Количество", "Удалить"};
    
    private DefaultTableModel odTable;
    private MySQLConnector con;
    private String managerName;
    private int code; 
    
    OrderTable(MySQLConnector con) {
        super(null, colNames);
        this.con = con;
    }
    
    //public void addRow(Vector row)
    //{
    //    odTable.addRow(row);
    //}
    
    //public void rmvRow(int ind)
    //{
    //    odTable.removeRow(ind);
    //}
    
    public void rmvColumn(int column) {
        columnIdentifiers.remove(column);
        for (Object row: dataVector) {
            ((Vector) row).remove(column);
        }
        fireTableStructureChanged();
    }
    
    public OrderTable getDTMwithoutCol(int col)
    {
        OrderTable dtmWc = this;
        dtmWc.rmvColumn(col);
        return dtmWc;
    }
    
    public void exportTable()
    {
        String prodData = "";
        
        prodData.concat(prodData);
        
        //con.execSQL("insert into "+);
    }
    
}
