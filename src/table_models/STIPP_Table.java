
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
    private String catTableName;

    public STIPP_Table(MySQLConnector con)
    {
        colNames = new Vector<String>();
        colNames.add("Код товара");colNames.add("Поставщик");colNames.add("Наименование");colNames.add("Цена");colNames.add("Наличие");
        this.con = con;
    }

    public void fillTable(String codeFieldName, int userType, String stippName) throws SQLException
    {
        catTableName = stippName;
        String sqls = "select * from vendor";
        if(userType == 1)
            //sqls = "SELECT stus_id, prodName_ven, price_ven, quantity_ven from "+catTableName+" where "+searchField+" like \'"+codeFieldName+"\'";
            sqls = "SELECT stus_id, prodName_ven, saleManagerName, price_ven, quantity_ven from "+catTableName+", vendor" +
                    " where "+searchField+" like \'"+codeFieldName+"\'" +
                    " and venName LIKE purchManagerName";

        else if(userType == 2)
            sqls = "select stus_id, prodName_ven, venName, price_ven, quantity_ven from "+catTableName+" where "+searchField+" like \'"+codeFieldName+"\'";




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
            for (int i = 1; i <= maxColumns; i++)
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
