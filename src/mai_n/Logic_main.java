package mai_n;


import table_models.ProdOrderTable;
import table_models.STUS_Table;
import table_models.OrderListTable;
import table_models.STIPP_Table;

import java.sql.SQLException;

public class Logic_main {

    private final String namField = "prodName"; //�������� ���� ������������ �������, �� �������� ���������� �����
    private final String urlToDB = "jdbc:mysql://localhost/main_db";
    //private final String urlToDB = "jdbc:mysql://192.168.0.202/main_db";
    private final String paramName = "CatName"; //�������� ��������� �� �������� ����� ������������ �����
     //�������� �������, ��� �������� �������� ��������� �� �������� ����� ������������ �����

    private String paramTableName;
    private String stippTName;

    private STUS_Table mainTableMod;
    private STIPP_Table subTableMod;
    private ProdOrderTable orderTabMod;     //������ ������
    private OrderListTable orderListTabMod;
    private OrderListTable orderListSaleTabMod;

    private User user;


    private String userName; //current user

    private String srch; //searcging query

    private ParamList cats;     //one of parameters of products

    private MySQLConnector connector;

    private ParamList parList;

    public Logic_main(String usm, String pss, String datBase) throws SQLException, ClassNotFoundException, MyException {
        paramTableName = datBase;
        stippTName = "stipp_"+datBase.substring(5);
        setConnector(new MySQLConnector(urlToDB, usm, pss));
        setUserName(usm); //init user current name
        setUser(new User(usm, this.connector));
    }

    public void build_STUSnSTIPP() throws SQLException {

            setMainTableMod(new STUS_Table(getConnector()));
            setSubTableMod(new STIPP_Table(getConnector()));

            setParList(new ParamList(paramName, paramTableName, this.connector));
            getParList().ParamListSeparate();
            setCats(getParList());
    }



    public void build_Order_Tables() throws SQLException {
        if(user.getType()==2){
        setOrderListTabMod(new OrderListTable(getConnector()));
        setOrderTabMod(new ProdOrderTable(getConnector()));}
        else{
            setOrderListSaleTabMod(new OrderListTable(getConnector()));
        }



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

    public ProdOrderTable getOrderTabMod() {
        return orderTabMod;
    }

    public void setOrderTabMod(ProdOrderTable orderTabMod) {
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


        srch = "select * from "+paramTableName+" where "+namField+" like \'%" + search + "%\' and CatName like \'"+categ+"\'";

        System.out.println(srch);
        mainTableMod.fillTable(srch);
    }

    public void mkSTIPP(String productCode) throws SQLException {

        subTableMod.fillTable(productCode, user.getType(), stippTName);
    }

    public void mkOrderTable()
    {
        orderTabMod = new ProdOrderTable(connector, userName);
    }

    public void mkProdOrderTable(int code) throws SQLException {
        orderTabMod.fillTable(code, paramTableName);
    }


    public void mkOrderListTable() throws SQLException {
        if(user.getType()==2)  orderListTabMod.fillTable();
        else {
            orderListSaleTabMod.fillTableSale(userName);
        }
    }

    public OrderListTable getOrderListTabMod() {
        return orderListTabMod;
    }

    public void setOrderListTabMod(OrderListTable orderListTabMod) {
        this.orderListTabMod = orderListTabMod;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParamList getParList() {
        return parList;
    }

    public void setParList(ParamList parList) {
        this.parList = parList;
    }

    public OrderListTable getOrderListSaleTabMod() {
        return orderListSaleTabMod;
    }

    public void setOrderListSaleTabMod(OrderListTable orderListSaleTabMod) {
        this.orderListSaleTabMod = orderListSaleTabMod;
    }
}
