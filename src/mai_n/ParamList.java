package mai_n;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

public class ParamList {

    private String paramName;
    private Vector params;

    private Vector<HashSet> params2;

    
    public ParamList(String sqlParam, String sqlTable, MySQLConnector con) throws SQLException 
    {
        params = new Vector();
        paramName=sqlParam;
        String srch = "select distinct "+sqlParam+" from "+sqlTable;
        ResultSet rs = con.getResultSet(srch);
        System.out.println("sql from ParamList="+srch);
        //ResultSetMetaData data = rs.getMetaData();
        //params.add("%");
        while (rs.next()) {params.add(rs.getString(1));}
        
    }

    public void ParamListSeparate() throws SQLException
    {
        setParams2(new Vector<HashSet>());



        for (int i = 0; i<params.size(); i++)
        {
            String cursor = (String) params.get(i);
            String[] ss=cursor.split("/");
            for(int q=0; q<ss.length; q++)
            {
                if(q>= getParams2().size()) getParams2().add(new HashSet());
                getParams2().get(q).add(ss[q]);
            }
        }


        for (int i = 0; i <params2.size(); i++) params2.get(i).add("%");


    }

    public Vector<Vector> getParamsVector()
    {
        Vector<Vector> res = new Vector<Vector>();

        for(int q=0; q< getParams2().size(); q++)
        {
            if(q>=res.size()) res.add(new Vector());
            res.get(q).addAll(getParams2().get(q));
        }
        return res;
    }

    public String getParamName()
    {return paramName;}
    
    public Vector getValues()
    {return params;}

    public Vector<HashSet> getParams2() {
        return params2;
    }

    public void setParams2(Vector<HashSet> params2) {
        this.params2 = params2;
    }
}
