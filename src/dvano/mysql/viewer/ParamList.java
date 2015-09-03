package dvano.mysql.viewer;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class ParamList {

    private String paramName;
    private Vector params;
    
    public ParamList(String sqlParam, String sqlTable, MySQLConnector con) throws SQLException 
    {
        params = new Vector();
        paramName=sqlParam;
        String srch = "select distinct "+sqlParam+" from "+sqlTable;
        ResultSet rs = con.getResultSet(srch);
        System.out.println("sql from ParamList="+srch);
        //ResultSetMetaData data = rs.getMetaData();
        params.add("%");
        while (rs.next()) {params.add(rs.getString(1));}
        
    }
    
    public String getParamName()
    {return paramName;}
    
    public Vector getValues()
    {return params;}
    
}
