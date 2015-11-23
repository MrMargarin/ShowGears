package table_models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import mai_n.MySQLConnector;
import java.io.FileNotFoundException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ProdOrderTable extends DefaultTableModel{
    //private static final String[] colNames = {"Код товара", "Наименование", "Категория", "Поставщик", "Количество", "Удалить"};
    //private static final String[] colNamesWrem = {"Код товара", "Наименование", "Категория", "Количество"};
    private static final String[] colNames = {"Код товара", "Наименование", "Категория", "Количество", "Удалить"};
    private final String orderProdsNameTable = "orderlist_table";
    private final String orderTable = "order_table";
    private final String rowCountSQL = "select count(*) from order_table";
    private final String path = "export\\"; //path to export excell file
    

    
    //private DefaultTableModel odTable;
    private MySQLConnector con;
    private String managerName;
    private String comment;
    private int orderRowCount;
    private int orderNumber;

    public ProdOrderTable(MySQLConnector con)
    {
        super((Object[][]) null, null);
        this.setCon(con);
    }

    public ProdOrderTable(MySQLConnector con, int orderCode) throws SQLException { //конструктор для ....
        super((Object[][]) null, null);
        this.setCon(con);
        String sql = "SELECT stus_table.id, stus_table.prodName, stus_table.catName, orderlist_table.quantity_req " +
                "FROM orderlist_table " +
                "INNER JOIN stus_table ON stus_table.id = orderlist_table.stus_id where orderlist_table.order_id like '"+orderCode+"'";
        ResultSet rs = this.getCon().getResultSet(sql);
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
        this.setCon(con);
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

    public static String getStippByOrderNumber(int orCode, MySQLConnector con) throws SQLException {
        String sql = "select order_table.baseName from order_table where order_table.id = " + orCode;
        ResultSet rs = con.getResultSet(sql);
        String stusName, stippName;
        rs.next();
        stusName = rs.getString(1);
        stippName = "stipp_" + stusName.substring(5);
        return stippName;
    }

    public String fillTable(int orderCode) throws SQLException {   //table for PM, like SM's STUS  _PM

        String sql = "select order_table.baseName from order_table where order_table.id = " + orderCode;
        ResultSet rs = this.getCon().getResultSet(sql);
        String stusName, stippName = null;
        try {
            rs.next();
            stusName = rs.getString(1);
            stippName = "stipp_" + stusName.substring(5);

        /*String sql = "SELECT "+stusName+".id, " +
                stusName+".prodName, " +
                stusName+".catName, " +
                stippName+".venName, " +
                "orderlist_table.quantity_req " +
                "FROM orderlist_table " +
                "INNER JOIN "+stusName+" ON "+stusName+".id = orderlist_table.stus_id " +
                "INNER JOIN "+stippName+" ON "+stusName+".id = "+stippName+".stus_id " +
                "where orderlist_table.order_id like '"+orderCode+"'";
        */
        sql = "SELECT "+stusName+".id, " +
                stusName+".prodName, " +
                stusName+".catName, " +
                "orderlist_table.quantity_req, " +
                "vendor.purchManagerName "+
                "FROM orderlist_table " +
                "INNER JOIN "+stusName+" ON "+stusName+".id = orderlist_table.stus_id " +
                "INNER JOIN vendor ON vendor.id = orderlist_table.ven_id " +
                "where orderlist_table.order_id like '"+orderCode+"'";
        System.out.println("заполнение таблицы товаров от заказа==Запрос из ProdOrderTable.fillTable: " +sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        rs = this.getCon().getResultSet(sql);
        ResultSetMetaData data = rs.getMetaData();

        Vector<Vector<Object>> values = new Vector<>();
        int maxColumns = data.getColumnCount();

        while (rs.next()) {
            Vector<Object> value = new Vector<>();
            for (int i = 1; i <= maxColumns; i++)
                value.add(getCon().getResultSet().getObject(i));
            values.add(value);
        }

        final Vector<String> colNamesVec = new Vector<>();
        colNamesVec.add(colNames[0]);
        colNamesVec.add(colNames[1]);
        colNamesVec.add(colNames[2]);
        colNamesVec.add(colNames[3]);
        colNamesVec.add("Поставщик");
        this.setDataVector(values, colNamesVec);
        return stippName;
    }

    public int getOrderRowCount() {return orderRowCount;}

    public int getOrderNumber() {return orderNumber;}
    
    public void exportTable(String stusName, String stippName)
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
            getCon().execSQL(sql); //insert order
        } catch (SQLException e) {
            //
        }
        try {
        for(int i = 0; i < this.getRowCount(); i++)
        {
            exportOrderedProduct(this.getValueAt(i, 0), this.getValueAt(i, 3), stusName);
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Недостаточно прав у вашего пользоателя", e.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }


    }

    private void exportOrderedProduct(Object prod_id, Object quantity, String baseName) throws SQLException {

        String sql = "insert into "+orderProdsNameTable+" (`order_id`, `stus_id`, `quantity_req`, `stusName`)  values ('"+orderNumber+"', '"+prod_id+"', '"+quantity+"', '"+baseName+"');";
        System.out.println("sql adding new order: "+sql);

            getCon().execSQL(sql); //insert orders

    }

    public void exportToExcell(String manName, String ordNum)
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
            cell.setCellValue("Заказ №"+ordNum);
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("Менеджер: "+manName);
            for (int i=0;i<this.getRowCount();i++) {
                row = sheet.createRow(i+offset);
                for (int j=0;j<this.getColumnCount();j++) {

                    cell = row.createCell(j);
                    cell.setCellValue(this.getValueAt(i, j).toString());
                }
            }


            FileOutputStream out = new FileOutputStream(getPath()+"Zakaz"+ordNum+".xls");
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

    public String getPath() {
        return path;
    }

    public MySQLConnector getCon() {
        return con;
    }

    public void setCon(MySQLConnector con) {
        this.con = con;
    }
}
