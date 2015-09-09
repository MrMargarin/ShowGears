package table_models;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Vector;
import mai_n.MySQLConnector;



public class OrderTable extends DefaultTableModel{
    private static final String[] colNames = {"Код товара", "Наименование", "Категория", "Количество", "Удалить"};
    private final String orderProdsNameTable = "orderlist_table";
    private final String orderTable = "order_table";
    private final String rowCountSQL = "select count(*) from orderlist_table";
    
    //private final String[] colNames = {"Код товара", "Наименование", "Категория", "Количество", "Удалить"};
    
    //private DefaultTableModel odTable;
    private MySQLConnector con;
    private String managerName;
    private String comment;
    private int orderRowCount;
    private int orderNumber;
    
    public OrderTable(MySQLConnector con, String managerName) {
        super(null, colNames);
        this.con = con;
        this.managerName = managerName;


        try {
            orderRowCount = (Integer) this.con.getResultSet(rowCountSQL).getObject(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        orderNumber = orderRowCount+1;

    }
    
    public int getOrderRowCount() {return orderRowCount;}

    public int getOrderNumber() {return orderNumber;}
    
    public void exportTable()
    {
        System.out.println("I am in OrderTable.exportTable.");

        String sql ="insert into "+orderTable+
                //+ orderNumber+
                " (`comment`, `managerName`) values ('"+ comment+
                "', '"       + managerName+
                "');";
        con.execSQL(sql); //insert order

        for(int i = 0; i < this.getRowCount(); i++)
        {
            //exportOrderedProduct((Integer) this.getValueAt(i, 0), (Integer) this.getValueAt(i, 3));
            exportOrderedProduct(this.getValueAt(i, 0), this.getValueAt(i, 3));
        }


    }

    private void exportOrderedProduct(Object prod_id, Object quantity)
    {

        String sql = "insert into "+orderProdsNameTable+" (`order_id`, `stus_id`, `quantity_req`)  values ('"+orderNumber+"', '"+prod_id+"', '"+quantity+"');";
        System.out.println("sql: "+sql);
        con.execSQL(sql); //insert orders
    }

    public void setComment(String comment)
    {this.comment = comment;}
    
}
