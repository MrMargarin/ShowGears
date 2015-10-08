package table_models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;
import java.util.Vector;
import mai_n.MySQLConnector;
import java.io.FileNotFoundException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ProdOrderTable extends DefaultTableModel{
    private static final String[] colNames = {"Код товара", "Наименование", "Категория", "Количество", "Удалить"};
    private static final String[] colNamesWrem = {"Код товара", "Наименование", "Категория", "Количество"};
    private final String orderProdsNameTable = "orderlist_table";
    private final String orderTable = "order_table";
    private final String rowCountSQL = "select count(*) from order_table";
    private final String path = "D:\\workbook.xls"; //path to export excell file
    

    
    //private DefaultTableModel odTable;
    private MySQLConnector con;
    private String managerName;
    private String comment;
    private int orderRowCount;
    private int orderNumber;

    public ProdOrderTable(MySQLConnector con)
    {
        super((Object[][]) null, null);
        this.con = con;
    }

    public ProdOrderTable(MySQLConnector con, int orderCode) throws SQLException { //конструктор для ....
        super((Object[][]) null, null);
        this.con = con;
        String sql = "SELECT stus_table.id, stus_table.prodName, stus_table.catName, orderlist_table.quantity_req " +
                "FROM orderlist_table " +
                "INNER JOIN stus_table ON stus_table.id = orderlist_table.stus_id where orderlist_table.order_id like '"+orderCode+"'";
        ResultSet rs = this.con.getResultSet(sql);
        ResultSetMetaData data = rs.getMetaData();

        Vector<Vector<Object>> values = new Vector<>();
        int maxColumns = data.getColumnCount();

        while (rs.next()) {
            Vector<Object> value = new Vector<>();
            for (int i = 1; i <= maxColumns; i++)
                value.add(con.getResultSet().getObject(i));
            values.add(value);
        }

        final Vector<String> colNamesVec = new Vector<>(); colNamesVec.add(colNames[0]); colNamesVec.add(colNames[1]); colNamesVec.add(colNames[2]); colNamesVec.add(colNames[3]);
        this.setDataVector(values, colNamesVec);


    }

    public ProdOrderTable(MySQLConnector con, String managerName) { //конструктор для заполнения заказа _SM
        super(null, colNames);
        this.con = con;
        this.managerName = managerName;


        try {

            ResultSet temp = con.getResultSet(rowCountSQL);
            if(temp.next())
            orderRowCount = temp.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        orderNumber = orderRowCount+1;

    }

    public void fillTable(int orderCode) throws SQLException {   //table for PM, like SM's STUS  _PM

        String sql = "SELECT stus_table.id, stus_table.prodName, stus_table.catName, orderlist_table.quantity_req " +
                "FROM orderlist_table " +
                "INNER JOIN stus_table ON stus_table.id = orderlist_table.stus_id where orderlist_table.order_id like '"+orderCode+"'";
        ResultSet rs = this.con.getResultSet(sql);
        ResultSetMetaData data = rs.getMetaData();

        Vector<Vector<Object>> values = new Vector<>();
        int maxColumns = data.getColumnCount();

        while (rs.next()) {
            Vector<Object> value = new Vector<>();
            for (int i = 1; i <= maxColumns; i++)
                value.add(con.getResultSet().getObject(i));
            values.add(value);
        }

        final Vector<String> colNamesVec = new Vector<>(); colNamesVec.add(colNames[0]); colNamesVec.add(colNames[1]); colNamesVec.add(colNames[2]); colNamesVec.add(colNames[3]);
        this.setDataVector(values, colNamesVec);
    }

    public int getOrderRowCount() {return orderRowCount;}

    public int getOrderNumber() {return orderNumber;}
    
    public void exportTable()
    {
/*
        String sql ="insert into "+orderTable+
                //+ orderNumber+
                " (`comment`, `managerName`) values ('"+ comment+
                "', '"       + managerName+
                "');";
*/

        String sql ="insert into "+orderTable+
                //+ orderNumber+
                " (`comment`, `managerName`, `date`) values ('"+ comment+
                "', '"       + managerName+ "', NOW());";


        try {
            con.execSQL(sql); //insert order
        } catch (SQLException e) {
            //
        }
        try {
        for(int i = 0; i < this.getRowCount(); i++)
        {
            exportOrderedProduct(this.getValueAt(i, 0), this.getValueAt(i, 3));
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Недостаточно прав у вашего пользоателя", e.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }


    }

    private void exportOrderedProduct(Object prod_id, Object quantity) throws SQLException {

        String sql = "insert into "+orderProdsNameTable+" (`order_id`, `stus_id`, `quantity_req`)  values ('"+orderNumber+"', '"+prod_id+"', '"+quantity+"');";
        System.out.println("sql: "+sql);

            con.execSQL(sql); //insert orders

    }

    public void exportToExcell()
    {
        //Exporting to Excel
        try{
            Workbook wb = new HSSFWorkbook();
            CreationHelper createhelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("Заказ");
            Row row = null;
            Cell cell = null;
            final int offset = 2;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("Заказ №"+orderNumber);
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("Менеджер: "+managerName);
            for (int i=0;i<this.getRowCount();i++) {
                row = sheet.createRow(i+offset);
                for (int j=0;j<this.getColumnCount();j++) {

                    cell = row.createCell(j);
                    cell.setCellValue(this.getValueAt(i, j).toString());
                }
            }


            FileOutputStream out = new FileOutputStream(path);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProdOrderTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProdOrderTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setComment(String comment)
    {this.comment = comment;}
    
}
