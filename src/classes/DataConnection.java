package classes;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * Created by eveil on 1/21/14.
 */
public class DataConnection implements Runnable{

    private String user;
    private String password;
    private String database;
    private DefaultTableModel SQLTable;
    private ArrayList<String> colNames;

    public Connection connection;
    public Statement statement;

    public String return_status;

    public DataConnection(String p_user, String p_password, String p_database)
    {
        user=p_user;
        password=p_password;
        database=p_database;
    }

    public void run()
    {
        try{
            connectDB();
           }
        catch(ClassNotFoundException ex){
            String msg="ClassNotFound exception, likely caused from a " +
                    "missing jdbc driver.\nDownload driver at http:dev.mysql" +
                    ".com/downloads/connector/j\nIn the project tree, " +
                    "right click your jdk library under External Libraries, " +
                    "Open library settings, Press + and add a path to the " +
                    "jar file for th jdbc.";
            JOptionPane.showMessageDialog(null,msg,"No database connector " +
                        "detected",JOptionPane.INFORMATION_MESSAGE);
            }
        catch(Exception e){
            reportFatalException(e, "Connect");
            return;
        }
        
        try{    
            ResultSet rs = this.getResultSet("select * from _sverla_");
            ResultSetMetaData data = rs.getMetaData();
            Vector<Object> columns = new Vector<>();
            Vector<Vector<Object>> values = new Vector<>();
            int maxColumns = data.getColumnCount();
            for (int i = 1; i <= maxColumns; i++) {
                columns.add(data.getColumnName(i));
            }
            while (rs.next()) {
                Vector<Object> value = new Vector<>();
                for (int i = 1; i <= maxColumns; i++) {
                    value.add(rs.getObject(i));
                }
                values.add(value);
            }
            SQLTable = new DefaultTableModel(values, columns);
                        
        }
        catch(SQLException ex){
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        JOptionPane.showMessageDialog(null,"Connection is good.",
                    "Test Connection",JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * When fatal exceptions are caught, all descendant classed handle them
     * the same way at their own thread entry points.
     *
     * @param e Exception The thrown fatal exception
     * @param notice_title String The title on the user error notification
     */
    protected void reportFatalException(Exception e, String notice_title)
    {
        e.printStackTrace();
        System.err.println(e.getCause());
        JOptionPane.showMessageDialog(null,e.getMessage(),
                notice_title,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Get the connector from:
     * http://dev.mysql.com/downloads/connector/j
     *
     * @throws Exception
     */
    public void connectDB() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        connection= DriverManager.getConnection("jdbc:mysql://192.168.1.158/"+
                database+"?user="+user+"&password="+password);
    }
    
     public  void execute(String sql) throws SQLException {
        this.statement.execute(sql);
    }

    public  void disconnect() throws SQLException {
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

    public  ResultSet getResultSet(String sql) throws SQLException {
        return this.statement.executeQuery(sql);
    }
    
    public DefaultTableModel getTab()
    {return SQLTable;}
}
