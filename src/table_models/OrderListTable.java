package table_models;

import mai_n.MySQLConnector;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by Rimskii on 12.09.2015.
 */
public class OrderListTable extends DefaultTableModel{

    private static final String[] colNames = {"Номер", "Комментарий", "Фамилия", "Имя"};
    private static final String tableName = "order_table";
    private static final String usersTable = "users";
    private MySQLConnector con;

    public OrderListTable(MySQLConnector con) throws SQLException {
        super((Object[][]) null, null);
        this.con = con;

    }

    public void fillTable() throws SQLException {
        //String sqls = "select * from "+tableName;


        String sqls = "select order_table.id, order_table.`comment`, users.Surname, users.Name from "+tableName+", "+usersTable+" where managerName like login";

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

        final Vector<String> colNamesVec = new Vector<>(); colNamesVec.add(colNames[0]); colNamesVec.add(colNames[1]); colNamesVec.add(colNames[2]); colNamesVec.add(colNames[3]);
        this.setDataVector(values, colNamesVec);
    }




}
