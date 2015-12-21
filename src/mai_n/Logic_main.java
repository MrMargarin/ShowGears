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

    private String stusTName;
    private String stippTName;
    private String stippFromOrder;

    private STUS_Table stus_tab;
    private STIPP_Table stipp_tab;
    private ProdOrderTable prodOrder_tab;     //������ ������
    private OrderListTable orderListPrch_tab;
    private OrderListTable orderListSale_tab;

    private User user;


    private String userName; //current user

    private String srch; //searcging query

    private ParamList cats;     //one of parameters of products

    private MySQLConnector connector;

    private ParamList parList;

    public Logic_main(String usm, String pss, String datBase) throws SQLException, ClassNotFoundException, MyException {
        setStusTName(datBase);
        setStippTName("stipp_"+datBase.substring(5));
        setConnector(new MySQLConnector(urlToDB, usm, pss));
        setUserName(usm); //init user current name
        setUser(new User(usm, this.connector));
    }

    public Logic_main(String usm, String pss) throws SQLException, ClassNotFoundException, MyException {
        setStusTName(null);
        setStippTName(null);
        setConnector(new MySQLConnector(urlToDB, usm, pss));
        setUserName(usm); //init user current name
        setUser(new User(usm, this.connector));
    }

    public void setStusnStippNames(String datBase)
    {
        setStusTName(datBase);
        setStippTName("stipp_" + datBase.substring(5));
    }

    public void build_STUSnSTIPP() throws SQLException {

            setStus_tab(new STUS_Table(getConnector()));
            setStipp_tab(new STIPP_Table(getConnector()));

            setParList(new ParamList(paramName, getStusTName(), this.connector));
            getParList().ParamListSeparate();
            setCats(getParList());
    }



    public void build_Order_Tables() throws SQLException {
        if(user.getType()==2){
        setOrderListPrch_tab(new OrderListTable(getConnector()));
        setProdOrder_tab(new ProdOrderTable(getConnector()));}
        else{
            setOrderListSale_tab(new OrderListTable(getConnector()));
        }



    }




    public ParamList getCats()
    {return cats;}

    public STUS_Table getStus_tab() {
        return stus_tab;
    }

    public void setStus_tab(STUS_Table stus_tab) {
        this.stus_tab = stus_tab;
    }

    public STIPP_Table getStipp_tab() {
        return stipp_tab;
    }

    public void setStipp_tab(STIPP_Table stipp_tab) {
        this.stipp_tab = stipp_tab;
    }

    public ProdOrderTable getProdOrder_tab() {
        return prodOrder_tab;
    }

    public void exportNewOrder()
    {
        prodOrder_tab.exportTable(stusTName, stippTName);
    }

    public void setProdOrder_tab(ProdOrderTable prodOrder_tab) {
        this.prodOrder_tab = prodOrder_tab;
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


        srch = "select * from "+ getStusTName() +" where "+namField+" like \'%" + search + "%\' and CatName like \'"+categ+"\'";

        System.out.println("Запрос для поиска товаров: "+srch);
        stus_tab.fillTable(srch);
    }

    public void mkSTIPP(String productCode) throws SQLException {
        int t = user.getType();
        if(t==1) {
            stipp_tab.fillTable(productCode, 1, getStippTName());
        }
        else if(t==2){
            setStipp_tab(new STIPP_Table(getConnector()));
            stipp_tab.fillTable(productCode,2,stippFromOrder);
        }

        else{
            System.out.println("==============Не определен тип пользователя в mkSTIPP(productCode)\nНеизвестный тип пользователя: "+t);
        }
    }

    public void mkOrderTable()
    {
        prodOrder_tab = new ProdOrderTable(connector, userName);
    }

    public void mkProdOrderTable(int code) throws SQLException {
        stippFromOrder = prodOrder_tab.fillTableForPRCH(code);
    }


    public void mkOrderListTable() throws SQLException {
        if(user.getType()==2)  orderListPrch_tab.fillTable();
        else {
            orderListSale_tab.fillTableSale(userName);
        }
    }

    public OrderListTable getOrderListPrch_tab() {
        return orderListPrch_tab;
    }

    public void setOrderListPrch_tab(OrderListTable orderListPrch_tab) {
        this.orderListPrch_tab = orderListPrch_tab;
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

    public OrderListTable getOrderListSale_tab() {
        return orderListSale_tab;
    }

    public void setOrderListSale_tab(OrderListTable orderListSale_tab) {
        this.orderListSale_tab = orderListSale_tab;
    }

    public String getStusTName() {
        return stusTName;
    }

    public void setStusTName(String stusTName) {
        this.stusTName = stusTName;
        if(stusTName!=null)stippTName = "stipp_" + stusTName.substring(5);
    }

    public String getStippTName() {
        return stippTName;
    }

    public void setStippTName(String stippTName) {
        this.stippTName = stippTName;
    }

    public String getStippFromOrder() {
        return stippFromOrder;
    }

    public void setStippFromOrder(String stippFromOrder) {
        this.stippFromOrder = stippFromOrder;
    }
}
