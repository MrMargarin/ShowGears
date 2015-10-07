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

    private static final String[] colNames = {"Номер", "Комментарий", "Фамилия", "Имя", "Завершен", "Дата создания"};
    private static final String tableName = "order_table";
    private static final String usersTable = "users";
    private MySQLConnector con;

    @Override
    public Class<?> getColumnClass(int column)
    {
        switch (column)
        {
            case 0: return String.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return String.class;
            case 4: return Boolean.class;
            default: return String.class;
        }
    }

    public OrderListTable(MySQLConnector con) throws SQLException {
        super((Object[][]) null, null);
        this.con = con;

    }

    public void fillTable() throws SQLException {
        //String sqls = "select * from "+tableName;


        String sqls = "select order_table.id, order_table.`comment`, users.Surname, users.Name, order_table.`status` from "+tableName+", "+usersTable+" where managerName like login";

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
            for (int i = 1; i <= maxColumns; i++) {
               Object val = con.getResultSet().getObject(i);
               // if(i == (maxColumns))
               //{value.add((boolean) val); break;}
                value.add(val);
            }
            values.add(value);
        }

        final Vector<String> colNamesVec = new Vector<>(); //colNamesVec.add(colNames[0]); colNamesVec.add(colNames[1]); colNamesVec.add(colNames[2]); colNamesVec.add(colNames[3]);
        for(String i : colNames) colNamesVec.add(i);

        this.setDataVector(values, colNamesVec);
    }


    public void confirmChanges()
    {

    }


}
