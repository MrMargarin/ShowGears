
package table_models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import mai_n.MySQLConnector;


public class STIPP_Table {
    private final String searchField = "stus_id"; //Имя поля по которому производится поиск товара(первичный ключ)
    private DefaultTableModel subTable;
    private MySQLConnector con;
    private Vector<String> colNames;

    public STIPP_Table(MySQLConnector con)
    {
        colNames = new Vector<String>();
        colNames.add("Код товара");colNames.add("Поставщик");colNames.add("НаименованиеПоставщика");colNames.add("Цена");colNames.add("Наличие");
        this.con = con;
    }
    
    public void fillTable(String codeFieldName, String catTableName) throws SQLException
    {
        String sqls = "select * from "+catTableName+" where "+searchField+" like \'"+codeFieldName+"\'";
        ResultSet rs = con.getResultSet(sqls);
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
            for (int i = 2; i <= maxColumns; i++)
                value.add(con.getResultSet().getObject(i));
            values.add(value);
        }
        
        subTable = new DefaultTableModel(values, colNames);
    }
    
    public void closeConec() throws SQLException
    {
        this.con.disconnect();
    }
    
    public DefaultTableModel getSubTab()
    { return subTable;}
    
    
    
}
