package mai_n;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by Rimskii on 16.09.2015.
 */
public class User {

    private static String userTabSQLName = "users";
    private static String userLoginFieldSQLName = "login";
    private String name;
    private String surname;
    private String login;
    private int type; //1-sale; 2-prch
    private MySQLConnector con;

    User(String login, MySQLConnector con) throws SQLException, MyException {
        this.login = login;
        this.con = con;
        String sqls = "select * from "+userTabSQLName+" where "+userLoginFieldSQLName+" like \'"+login+"\'";
        ResultSet rs = con.getResultSet(sqls);
        this.name = null;
        this.surname = null;
        this.type = 0;

        while (rs.next()) {
            name = con.getResultSet().getString(2);
            surname = con.getResultSet().getString(3);
            type = con.getResultSet().getInt(5);
        }
        if(type==0) {
            throw new MyException();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
