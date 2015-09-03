package dvano.mysql.viewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MySQLConnector {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet rs = null;
    private String USER, ADDR, PASS;

//    MySQLConnector() throws ClassNotFoundException, SQLException {
//        registerDriver();
//        disconnect();
//        connect("jdbc:mysql://192.168.1.158/sverlamg", "admin", "pool");
//        getResultSet("select * from _sverla_");
//    }
//
//    MySQLConnector(String sql) throws ClassNotFoundException, SQLException {
//        registerDriver();
//        disconnect();
//        connect("jdbc:mysql://192.168.1.158/sverlamg", "admin", "pool");
//        getResultSet(sql);
//    }
    
    MySQLConnector(String ad, String us, String ps) throws ClassNotFoundException, SQLException {
        registerDriver();
        disconnect();
        USER = us; ADDR = ad; PASS = ps;
        connect(ad, us, ps);
    }
    
    MySQLConnector(String ad, String us, String ps, String sql) throws ClassNotFoundException, SQLException {
        registerDriver();
        disconnect();
        USER = us; ADDR = ad; PASS = ps;
        connect(ad, us, ps);
        getResultSet(sql);
    }
    
    public void registerDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

    private void execute(String sql) throws SQLException {
        this.statement.execute(sql);
    }

    public void connect(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
        this.statement = this.connection.createStatement();
    }

    public void disconnect() throws SQLException {
        if (this.statement != null) {
            if (!this.statement.isClosed()) {
                this.statement.close();
            }
        }
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                this.connection.close();
            }
        }
    }

    public ResultSet getResultSet(String sql) throws SQLException {
        rs = this.statement.executeQuery(sql);
        return rs;
    }
    
    public void execSQL(String sql)
    {
        try {
            this.statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Adding new object is fault: " + ex.getMessage());
        }
    }
    
    public ResultSet getResultSet() {
        return rs;
        
    }
    
    
}
