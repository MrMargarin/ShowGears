package table_models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import mai_n.MySQLConnector;

/**
 *
 * @author dvano
 */
public class STUS_Table {
    
    private MySQLConnector con;
    
    private DefaultTableModel mainTable;
    private String USER, ADDR, PASS;
    
    private ArrayList<String> colNamesOLD;
    private ArrayList<Integer> colToDel; //колонки с нулевыми значениями цены поставщиков(bidlocoding)
    private ArrayList<String> vendorNames;
    private int numOfItFound;
    private Vector<String> colNames;
    
    public STUS_Table(MySQLConnector con)
    {
        colNames = new Vector<String>();
        colNames.add("Код товара");colNames.add("Наименование");colNames.add("Категория");
        this.con = con;
    }
    
    public void fillTable(String sql) throws SQLException
    {
        mainTable = new DefaultTableModel(null, new String[]{"Код товара","Наименование", "Категория"});
        ResultSet rs = con.getResultSet(sql);
        ResultSetMetaData data = rs.getMetaData();
        //==================================read-col-names======================
        //Vector<Object> columns = new Vector<>();
        Vector<Vector<Object>> values = new Vector<>();
        int maxColumns = data.getColumnCount();
        //for (int i = 1; i <= maxColumns; i++) 
        //    columns.add(data.getColumnName(i));
        //==================================read-data===========================
        while (rs.next()) {
            Vector<Object> value = new Vector<>();
            for (int i = 1; i <= maxColumns; i++) 
                value.add(con.getResultSet().getObject(i));
            values.add(value);
        }
        
        mainTable = new DefaultTableModel(values, colNames);
        //fillColNamesnVendorNames();
        numOfItFound = values.size();
        if(numOfItFound==0)
            System.out.println("There is no items has found.");
        //else{
        //    fillColToDel(values);}
    }


    public String getElem(int row, int col)
    {
        return (String)((Vector)mainTable.getDataVector().elementAt(row)).elementAt(col);
    }
    
    public ArrayList getColNames() throws SQLException
    {
        return colNamesOLD;
    }

    

    public DefaultTableModel getMainTab()
    { return mainTable;}
    
    
    
    public void closeConec() throws SQLException
    {
        this.con.disconnect();
    }
    
    public static Vector<Vector<Object>> transparate(Vector<Vector<Object>> vec)
    {
        Vector<Vector<Object>> transp = new Vector<Vector<Object>>();
        for(int i = 0; i<vec.get(0).size(); i++) // Transpon
        {
            Vector t = new Vector();
            for(int j = 0; j<vec.size(); j++)
            {
                t.add(vec.get(j).get(i));
            }
            transp.add(t);        
        }
        return transp;
    }
    
    public static Vector<Vector<Object>> transparate2(Vector<Vector<Object>> vec)
    {
        Vector<Vector<Object>> transp = new Vector<>();
        for(int i = 0; i<vec.size(); i++) // Transpon
        {
            Vector t = new Vector();
            for(int j = 0; j<vec.get(0).size(); j++)
            {
                t.add(vec.get(i).get(j));
            }
            transp.add(t);
        }
        return transp;
    }
    
    public static String[][] transparateS(Vector<Vector<Object>> vec)
    {
        String[][] temp = new String[vec.get(0).size()][vec.size()];
        vec.toArray(temp);
        
        for (int i = 0; i < vec.size(); i++)
            for (int j = 0; j < vec.get(0).size(); j++)
                temp[j][i] = (String) vec.get(i).get(j);
        
        return temp;
    }
    public ArrayList<Integer> getColToDel()
    {
        return colToDel;
    }
    
    public int getNumOfItFnd()
    {return numOfItFound;}


}

