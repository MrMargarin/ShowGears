package mai_n;


import table_models.STUS_Table;
import table_models.OrderListTable;
import table_models.OrderTable;
import table_models.STIPP_Table;

import java.sql.SQLException;

public class Logic_main {

    private final String namField = "prodName"; //�������� ���� ������������ �������, �� �������� ���������� �����
    private final String urlToDB = "jdbc:mysql://localhost/main_db";
    private final String paramName = "CatName"; //�������� ��������� �� �������� ����� ������������ �����
    private final String paramTableName = "stus_table"; //�������� �������, ��� �������� �������� ��������� �� �������� ����� ������������ �����

    private STUS_Table mainTableMod;
    private STIPP_Table subTableMod;
    private OrderTable orderTabMod;     //������ ������
    private OrderListTable orderListTabMod;


    private String userName; //current user
    private String srch; //searcging query

    private ParamList cats;     //one of parameters of products

    private MySQLConnector connector;


    public Logic_main(String usm, String pss) throws SQLException, ClassNotFoundException {
        setConnector(new MySQLConnector(urlToDB, usm, pss));
        setUserName(usm); //init user current name

    }

    public void build_STUSnSTIPP() throws SQLException {

            setMainTableMod(new STUS_Table(getConnector()));
            setSubTableMod(new STIPP_Table(getConnector()));
            //orderTabMod = new OrderTable(connector, userName);
            setCats(new ParamList(paramName, paramTableName, this.connector));
    }




    public ParamList getCats()
    {return cats;}

    public STUS_Table getMainTableMod() {
        return mainTableMod;
    }

    public void setMainTableMod(STUS_Table mainTableMod) {
        this.mainTableMod = mainTableMod;
    }

    public STIPP_Table getSubTableMod() {
        return subTableMod;
    }

    public void setSubTableMod(STIPP_Table subTableMod) {
        this.subTableMod = subTableMod;
    }

    public OrderTable getOrderTabMod() {
        return orderTabMod;
    }

    public void setOrderTabMod(OrderTable orderTabMod) {
        this.orderTabMod = orderTabMod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCats(ParamList cats) {
        this.cats = cats;
    }

    public MySQLConnector getConnector() {
        return connector;
    }

    public void setConnector(MySQLConnector connector) {
        this.connector = connector;
    }

    public void disconnect() throws SQLException {
        connector.disconnect();
    }

    public void mkSTUSsearch(String search, Object categ) throws SQLException {
        srch = "select * from stus_table where "+namField+" like \'%" + search + "%\' and CatName like \'"+categ+"\'";
        mainTableMod.fillTable(srch);
    }

    public void mkSTIPP(String productCode) throws SQLException {
        subTableMod.fillTable(productCode, "stipp_table");

    }

    public void mkOrderTable()
    {
        orderTabMod = new OrderTable(connector, userName);
    }
}
