package mai_n;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

public class ParamList {

    private String paramName;
    private Vector fullParamNames;

    private Vector<HashSet> subCatNames;

    
    public ParamList(String sqlParam, String sqlTable, MySQLConnector con) throws SQLException 
    {
        setFullParamNames(new Vector());
        paramName=sqlParam;
        String srch = "select distinct "+sqlParam+" from "+sqlTable;
        ResultSet rs = con.getResultSet(srch);
        System.out.println("sql from ParamList="+srch);
        //ResultSetMetaData data = rs.getMetaData();
        //fullParamNames.add("%");
        while (rs.next()) {
            fullParamNames.add(rs.getString(1));
        }
    }

    public void ParamListSeparate() throws SQLException
    {
        setSubCatNames(new Vector<HashSet>());
        for (int i = 0; i< getFullParamNames().size(); i++)
        {
            String cursor = (String) getFullParamNames().get(i);
            String[] ss=cursor.split("/");
            for(int q=0; q<ss.length; q++)
            {
                if(q>= getSubCatNames().size()) getSubCatNames().add(new HashSet());
                getSubCatNames().get(q).add(ss[q]);
            }
        }
        for (int i = 0; i < subCatNames.size(); i++) subCatNames.get(i).add("%");
    }

    public Vector<Vector> getParamsVector()
    {
        Vector<Vector> res = new Vector<Vector>();

        for(int q=0; q< getSubCatNames().size(); q++)
        {
            if(q>=res.size()) res.add(new Vector());
            res.get(q).addAll(getSubCatNames().get(q));
        }
        return res;
    }


    public Vector getSubCatNames(String rootCatName)
    {
        HashSet preres = new HashSet();
        Vector res;
        if(rootCatName==null)
        {
            res = new Vector();
            res.addAll(subCatNames.get(0));
            return  res;
        }
        else
        {
            for(int i=0; i<fullParamNames.size(); i++)
            {
                String temp = (String) fullParamNames.get(i);
                if(temp.toUpperCase().indexOf(rootCatName.toUpperCase()) != -1)
                {
                    temp = temp.substring(rootCatName.length());
                    if(temp.contains("/")) temp = temp.substring(0, temp.indexOf("/"));
                    preres.add(temp);
                }
            }

            if(preres.isEmpty())
                return null;
            else{
                preres.add("%");
                res = new Vector();
                res.addAll(preres);
                return res;
            }
        }
    }

    public String getParamName()
    {return paramName;}
    
    public Vector getValues()
    {return getFullParamNames();}

    public Vector<HashSet> getSubCatNames() {
        return subCatNames;
    }

    public void setSubCatNames(Vector<HashSet> subCatNames) {
        this.subCatNames = subCatNames;
    }

    public Vector getFullParamNames() {
        return fullParamNames;
    }

    public void setFullParamNames(Vector fullParamNames) {
        this.fullParamNames = fullParamNames;
    }
}
