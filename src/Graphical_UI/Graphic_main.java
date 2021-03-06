package Graphical_UI;

import mai_n.Logic_main;
import mai_n.MyException;
import table_models.STIPP_Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
/**
 * Created by Rimskii on 12.09.2015.
 */
public class Graphic_main extends JFrame{

    private final String[] bases={"МетчикиПлашки","Сверла","Фрезы", "Измерилка"};

    //private ActionListener acK0, acK0, acK0, acK0, acK0
    private JPanel workPanel; //панель с кнопками и поиском
    private JPanel atrzPanel; //панель для подключения к БД
    private JPanel subPaneWithCategs;
    private JTable STUSTable, STIPPTable, ProdOrdTable, OrderListTable; //Таблицы
    private JLabel pass, login;
    private JTextField user;
    private JComboBox datBase;
    private JPasswordField password; //authorization
    private JTextField search;
    private ArrayList <JComboBox> Kategorii;
    private JButton connect, show, discon, mkOder, showOrders, canOrder, exportOrders, showOrdersS, closeOrders;

    private KeyAdapter keyLforConn, keyLforShow;

    private JSplitPane stus_stipp_pane, global_split_pane;
    private JPanel inputPane, outputPane, sidePane, rightPane; // panel for make orders;
    private JScrollPane scPfSTUSTable, scPfSTIPPTable, scPfPrOrdTable, scPfOrdLstTable, scPfOrdLstSaleTable; //   "scPf" - scroll panel for

    private Logic_main mainThread;
    private String catFull;


    public Graphic_main() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    this.setTitle("MetallGearsViewer");
    this.setSize(1300, 700);
    this.setLocationRelativeTo(null);
    this.setResizable(true);

    this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            try {
                if (mainThread != null) mainThread.disconnect();
                }
            catch (SQLException ex) {
                Logger.getLogger(Graphic_main.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    });

        stus_stipp_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        global_split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

        inputPane = new JPanel();
        atrzPanel = new JPanel();
        workPanel = new JPanel();

        sidePane = new JPanel(new BorderLayout());
        rightPane = new JPanel(new BorderLayout());

        Kategorii = new ArrayList<JComboBox>();

        datBase = new JComboBox(bases);
        search = new JTextField("72", 10); //-----------------search field
        login = new JLabel("Пользователь");
        user = new JTextField("sale", 4);
        pass = new JLabel("Пароль:");
        password = new JPasswordField("pool",7);
    //==============================================================================
    //===================================="Connect"=================================
        connect = new JButton("Войти");
        connect.addActionListener((ActionEvent e) -> connectAction());
    //====================================="Show"===================================
        //to the sale script has moved

        //====================================="Show OrderSale"=============================
        showOrdersS = new JButton("Показать Заказы");
        showOrdersS.addActionListener((ActionEvent e) -> {
            showOrdersSaleAction();
            showOrdersS.setVisible(false);
            closeOrders.setVisible(true);

        });

        closeOrders = new JButton("Закрыть Заказы");
        closeOrders.addActionListener((ActionEvent e) -> {
            closeOrders.setVisible(false);
            showOrdersS.setVisible(true);
            rightPane.removeAll();
            this.remove(rightPane);
            global_split_pane.revalidate();

        });

    //====================================="Show Order"=============================
        showOrders = new JButton("Показать Заказы");
        showOrders.addActionListener((ActionEvent e) -> {
            showOrdersAction();
            global_split_pane.setBottomComponent(stus_stipp_pane);
            global_split_pane.setTopComponent(sidePane);
            global_split_pane.setDividerLocation(0.3);
            global_split_pane.revalidate();
        });
    //====================================="Export to Excel"=============================
        exportOrders = new JButton("Вывести");
        exportOrders.addActionListener((ActionEvent e) -> exportAction());
    //====================================="Disconnect"=============================
        discon = new JButton("Выйти");
        discon.addActionListener((ActionEvent e) -> disconAction());
    //====================================="Make Oder"==============================
        mkOder = new JButton("Сделать Заказ");
        mkOder.addActionListener((ActionEvent e) -> {
            mkOrderAction();
            global_split_pane.setTopComponent(sidePane);
            global_split_pane.setDividerLocation(0.35);
            global_split_pane.revalidate();
        });
    //==============================================================================
        canOrder = new JButton("Отменить Заказ");
        canOrder.addActionListener((ActionEvent e) -> {
            canOrderAction();
            global_split_pane.setTopComponent(null);
            global_split_pane.setDividerLocation(0);
            global_split_pane.revalidate();
        });
    //==============================================================================
            atrzPanel.add(login);
            atrzPanel.add(user);
            atrzPanel.add(pass);
            atrzPanel.add(password);
            atrzPanel.add(datBase);
            //datBase.setVisible(false);
            atrzPanel.add(connect);


        inputPane.add(atrzPanel);
        inputPane.add(workPanel);

            atrzPanel.setVisible(true);
            workPanel.setVisible(false);

        //KEYS_BINDS
        mkKeyBinds();

        password.addKeyListener(keyLforConn);
        user.addKeyListener(keyLforConn);
        search.addKeyListener(keyLforShow);
        //===================================set icon=======================
        Image img = new ImageIcon("images\\Mini_logo2.png").getImage();
        this.setIconImage(img);


        ImageIcon icon = new ImageIcon("images\\metgears.png");
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
        JPanel beaut = new JPanel();
        beaut.setBackground(Color.decode("#252525"));
        beaut.add(thumb);
        this.add(beaut, BorderLayout.NORTH);
        this.add(inputPane, BorderLayout.SOUTH);


        //this.add(sidePane, BorderLayout.WEST);

        this.add(global_split_pane, BorderLayout.CENTER);
    }

    private void mkKeyBinds() {
        keyLforConn = new KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent ke) {
            if(ke.getKeyCode() == KeyEvent.VK_ENTER)
                connect.doClick();
            }};

        keyLforShow = new KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent ke) {
            if(ke.getKeyCode() == KeyEvent.VK_ENTER)
                show.doClick();
            }};
    }


    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;
        //private int row;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                int answer = JOptionPane.showConfirmDialog(button, "Удалить выбранный товар из заказа?");
                if(answer == JOptionPane.YES_OPTION)
                {
                    //System.out.println(super.getComponent().getClass().toString());
                    //System.out.println("before: ordertabSIZE: "+mainThread.getProdOrder_tab().getRowCount()+" | row index: "+row);
                    mainThread.getProdOrder_tab().removeRow(ProdOrdTable.getSelectedRow());


                    //System.out.println("after: ordertabSIZE: " + mainThread.getProdOrder_tab().getRowCount() + " | row index: " + row);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            //this.row = row;
            //System.err.print("ROW:" + row);
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //




                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private void exportAction()
    {
        String managerName, ordNumber;
        managerName = this.OrderListTable.getValueAt(this.OrderListTable.getSelectedRow(), 2).toString() + " " + this.OrderListTable.getValueAt(this.OrderListTable.getSelectedRow(), 3).toString();

        ordNumber = this.OrderListTable.getValueAt(this.OrderListTable.getSelectedRow(), 0).toString();
        mainThread.getProdOrder_tab().exportToExcell(managerName, ordNumber);
        JOptionPane.showMessageDialog(this, "Файл был сохранен в папке " + mainThread.getProdOrder_tab().getPath());
    }

    private void connectAction() {

        try {
            mainThread = new Logic_main(user.getText(), password.getText());
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка авторизации", WIDTH);
        }
        catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        catch (MyException e2){
            System.err.println("User Not Found!");
            System.exit(1);
        }
    //===================================================================================================
        if(mainThread.getUser().getType()==1) //sale
        {
        String stus_name;
        int choose = datBase.getSelectedIndex();
        if(choose==0)
                stus_name = "stus_baz";
        else if(choose==1)
                stus_name = "stus_svr";
        else if(choose==2)
                stus_name = "stus_frz";
        else if(choose==3)
                stus_name = "stus_izm";
        else {
                JOptionPane.showMessageDialog(this, "При входе была выбрана не существующая база.\n В данный момент выбрана база сверл.", "Ошибка при выборе базы", WIDTH);
                stus_name = "stus_svr";
        }
        System.out.println("stus_name: " + stus_name);

        mainThread.setStusnStippNames(stus_name);
        try
        {
            mainThread.build_STUSnSTIPP();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }


        show = new JButton("Искать");
        show.addActionListener((ActionEvent e) -> {
            searchAction(catFull);
            global_split_pane.setBottomComponent(stus_stipp_pane);
            global_split_pane.revalidate();
        });


        ActionListener combbCatsLsnr = e -> {
            JComboBox cb = (JComboBox)e.getSource();


            int arLsCmpSz = subPaneWithCategs.getComponentCount()-1;
            for(int i = arLsCmpSz;
                    i > subPaneWithCategs.getComponentZOrder(cb);
                    i--) {
                subPaneWithCategs.remove(i);

            }

            catFull = "";
            for(int i =0; i < subPaneWithCategs.getComponentCount(); i++)
                catFull+=((JComboBox) subPaneWithCategs.getComponent(i)).getSelectedItem().toString()+ "/";

            String subCatName = (String) cb.getSelectedItem();
            if(!subCatName.equals("%")) {



                Vector sCats = mainThread.getParList().getSubCatNames(catFull);
                if (sCats != null) subPaneWithCategs.add(new JComboBox(sCats));
            }
        };


        subPaneWithCategs = new JPanel();
        subPaneWithCategs.setLayout(new BoxLayout(subPaneWithCategs, BoxLayout.Y_AXIS));

        subPaneWithCategs.addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                JComboBox cb = (JComboBox)e.getChild();
                cb.setSelectedItem("%");
                cb.addActionListener(combbCatsLsnr);
                //Kategorii.add(cb);


                subPaneWithCategs.revalidate();
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                subPaneWithCategs.revalidate();
            }
        });

        //--root_categories_init
        subPaneWithCategs.add(new JComboBox(mainThread.getParList().getSubCatNames(null)));

            workPanel.add(new JLabel("Найти:"));
            workPanel.add(search);

            workPanel.add(subPaneWithCategs);
            workPanel.add(show);
            workPanel.add(discon);
            workPanel.add(mkOder);
            workPanel.add(showOrdersS);
            workPanel.add(closeOrders);
            closeOrders.setVisible(false);
            showOrdersS.setVisible(true);
            search.requestFocus(); //focus search field
        }
        else if(mainThread.getUser().getType()==2) //prch
        {

            //mainThread.setStipp_tab(new STIPP_Table(mainThread.getConnector()));

            workPanel.add(showOrders);
            workPanel.add(exportOrders);
            showOrders.setVisible(true);
            exportOrders.setVisible(true);
            workPanel.add(discon);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Не определен тип пользователя.", "Ошибка авторизации", WIDTH);
            System.exit(0);
        }

        atrzPanel.setVisible(false);
        workPanel.setVisible(true);

    }

    private void searchAction(String catFull)
    {
        try {
            if(stus_stipp_pane.isAncestorOf(scPfSTIPPTable)){ stus_stipp_pane.remove(scPfSTIPPTable); }
            //================Поисковик=================================

            //String catFull = "";
            //for(int i =0; i<Kategorii.size(); i++) catFull+=Kategorii.get(i).getSelectedItem().toString()+ "%";

            String catFULL = catFull;
            catFULL = catFULL.substring(0, catFULL.length()-1) + "%";

            //StringBuilder cFull = new StringBuilder(catFull);
            //cFull.deleteCharAt(cFull.length() - 1);



            mainThread.mkSTUSsearch(search.getText(), catFULL);

            if(mainThread.getStus_tab().getNumOfItFnd()==0) JOptionPane.showMessageDialog(this, "По заданным параметрам не найдено ни одного товара.", "X", WIDTH);

            STUSTable = new JTable(mainThread.getStus_tab().getMainTab()){
                @Override
                public boolean isCellEditable(int arg0, int arg1){return false;}};
            //==========================================================

            //================Установка Сортировщика====================
            TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getStus_tab().getMainTab());
            STUSTable.setRowSorter(sorter1);
            //================Установка Ширины Колонок==================
            STUSTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            STUSTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            STUSTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            scPfSTUSTable = new JScrollPane(STUSTable);

            //==========================Table-Order-add_new_product=======================
            STUSTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me)
                {
                    JTable table =(JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    TableModel model = STUSTable.getModel();
                    if (me.getClickCount() == 2) {
                        if(ProdOrdTable !=null) {
                            row = table.convertRowIndexToModel(row);
                            Vector ordProd = new Vector();
                            ordProd.add(model.getValueAt(row, 0));
                            ordProd.add(model.getValueAt(row, 1));
                            ordProd.add(model.getValueAt(row, 2));
                            ordProd.add(new Integer(0));
                            ordProd.add("Удалить");
                            mainThread.getProdOrder_tab().addRow(ordProd);
                        }
                    }
                }
            });

            //==========================Table-STIPP======================================

            //sort event listener=============================================
            STUSTable.getRowSorter().addRowSorterListener(
                    new RowSorterListener() {

                        @Override
                        public void sorterChanged(RowSorterEvent e) {
                            STUSTable.clearSelection();

                            //STUSTable.
                            //mainThread.getStus_tab().setMainTable((DefaultTableModel) STUSTable.getModel());
                        }
                    });
            //value change listener===========================================
            ListSelectionModel selModel = STUSTable.getSelectionModel();
            selModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    try {
                        int[] selectedRows = STUSTable.getSelectedRows();
                        if(selectedRows.length!=0) {
                            int selIndex = STUSTable.convertRowIndexToModel(selectedRows[0]);
                            TableModel model = STUSTable.getModel();
                            //System.out.println("value in stus has chang. get new model for stus");
                            Object value = model.getValueAt(selIndex, 0);
                            mainThread.mkSTIPP(value.toString());
                            TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(mainThread.getStipp_tab().getSubTab());
                            STIPPTable = new JTable(mainThread.getStipp_tab().getSubTab());
                            STIPPTable.setRowSorter(sorter2);
                            STIPPTable.setFillsViewportHeight(true);
                            scPfSTIPPTable = new JScrollPane(STIPPTable);
                            stus_stipp_pane.setBottomComponent(scPfSTIPPTable);
                        }

                        double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                        stus_stipp_pane.setDividerLocation(0.5);
                        //stus_stipp_pane.revalidate();

                    } catch (Exception ex) {

                        Logger.getLogger(Graphic_main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            stus_stipp_pane.setTopComponent(scPfSTUSTable); //панель с таблицей результатов товаров
            //stus_stipp_pane.validate();



        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "IN THE CONNECTION" + ex.getMessage(), "Error", WIDTH);
        }

    }


    private void showOrdersSaleAction() {


        try {
            mainThread.build_Order_Tables();
            mainThread.mkOrderListTable();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        if (mainThread.getOrderListSale_tab().getRowCount() == 0)
            JOptionPane.showMessageDialog(this, "Заказов нет.", "X", WIDTH);

        OrderListTable = new JTable(mainThread.getOrderListSale_tab()) {
            @Override
            public boolean isCellEditable(int row, int col) {return false;}
        };

        OrderListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //==========================================================

        //================Установка Сортировщика====================
        TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getOrderListSale_tab());
        OrderListTable.setRowSorter(sorter1);

        scPfOrdLstSaleTable = new JScrollPane(OrderListTable);

        rightPane.removeAll();
        rightPane.add(scPfOrdLstSaleTable, BorderLayout.CENTER);
        this.add(rightPane, BorderLayout.EAST);
        //rightPane.revalidate();

    }

    private void showOrdersAction()
    {
        showOrders.setVisible(false);
        try {
            mainThread.build_Order_Tables();
            mainThread.mkOrderListTable();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        if(mainThread.getOrderListPrch_tab().getRowCount()==0) JOptionPane.showMessageDialog(this, "Заказов нет.", "X", WIDTH);

        OrderListTable = new JTable(mainThread.getOrderListPrch_tab()){
            @Override
            public boolean isCellEditable(int row, int col) {
            switch (col) {
                case 5:
                    return true;
                default:
                    return false;
            }
            }

        };

        OrderListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //==========================================================

        //================Установка Сортировщика====================
        TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getOrderListPrch_tab());
        OrderListTable.setRowSorter(sorter1);
        //================Установка Ширины Колонок==================

        scPfOrdLstTable = new JScrollPane(OrderListTable);

        sidePane.removeAll();
        sidePane.add(scPfOrdLstTable, BorderLayout.CENTER);
        sidePane.revalidate();

        //==========================Table-STIPP======================================
        ListSelectionModel selModel = OrderListTable.getSelectionModel();
        selModel.addListSelectionListener(new ListSelectionListener() {
            @Override//
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int[] selectedRows = OrderListTable.getSelectedRows();
                    if (selectedRows.length != 0) {
                        int selIndex = selectedRows[0];
                        TableModel model = OrderListTable.getModel();
                        Object codeTrd = model.getValueAt(selIndex, 0);
                        int code = new Integer(codeTrd.toString());

                        mainThread.mkProdOrderTable(code);
                        TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(mainThread.getProdOrder_tab());
                        ProdOrdTable = new JTable(mainThread.getProdOrder_tab());
                        ProdOrdTable.setRowSorter(sorter2);
                        ProdOrdTable.setFillsViewportHeight(true);
                        scPfPrOrdTable = new JScrollPane(ProdOrdTable);

                    }

                    //double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                    stus_stipp_pane.setTopComponent(scPfPrOrdTable);
                    stus_stipp_pane.setDividerLocation(0.5);

                    //stus_stipp_pane.validate();

                } catch (SQLException ex) {
                    System.err.println("Error when value changed in OrderListTable");
                }

                ListSelectionModel selModel2 = ProdOrdTable.getSelectionModel();
                selModel2.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int[] selRows = ProdOrdTable.getSelectedRows();
                        if (selRows.length != 0) {
                            int selInx = selRows[0];
                            TableModel model2 = ProdOrdTable.getModel();
                            Object val = model2.getValueAt(selInx, 1);
                            try {
                                mainThread.mkSTIPP(val.toString());
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }


                            TableRowSorter<TableModel> sorter3 = new TableRowSorter<TableModel>(mainThread.getStipp_tab().getSubTab());
                            STIPPTable = new JTable(mainThread.getStipp_tab().getSubTab());

                            //STIPPTable.getSelectedRow()

                            STIPPTable.setRowSorter(sorter3);
                            STIPPTable.setFillsViewportHeight(true);
                        }
                        //===========================================================================
                        /*ListSelectionModel selModelSTIPP = STIPPTable.getSelectionModel();
                        selModelSTIPP.addListSelectionListener(new ListSelectionListener() {
                            @Override
                            public void valueChanged(ListSelectionEvent e) {

                            }
                        });*/


                        STIPPTable.addMouseListener(new MouseAdapter() {
                            public void mousePressed(MouseEvent me) {
                                JTable table = (JTable) me.getSource();
                                Point p = me.getPoint();
                                int row = table.rowAtPoint(p);
                                TableModel model = STIPPTable.getModel();
                                if (me.getClickCount() == 2) {
                                    if (ProdOrdTable != null) {
                                        row = table.convertRowIndexToModel(row);
                                        //Vector ordProd = new Vector();
                                        //ordProd.add(model.getValueAt(row, 2));
                                        //ordProd.add(model.getValueAt(row, 3));
                                        Long code = (Long) ProdOrdTable.getValueAt(ProdOrdTable.getSelectedRow(), 0);
                                        String ven, price;
                                        ven = (String) model.getValueAt(row, 2); price = (String) model.getValueAt(row, 3);
                                        ProdOrdTable.setValueAt(ven, ProdOrdTable.getSelectedRow(), 5);
                                        ProdOrdTable.setValueAt(price, ProdOrdTable.getSelectedRow(), 6);
                                        try {
                                            mainThread.getProdOrder_tab().appointVendorToCommPos(code, ven, price);
                                        } catch (SQLException e1) {
                                            System.out.println("Ошибка при назначении поставщика для товарной позиции.");
                                            e1.printStackTrace();
                                        }

                                        //mainThread.getProdOrder_tab().addRow(ordProd);
                                    }
                                }
                            }
                        });







                        scPfSTIPPTable = new JScrollPane(STIPPTable);
                        stus_stipp_pane.setBottomComponent(scPfSTIPPTable);
                        stus_stipp_pane.setDividerLocation(0.5);
                        //stus_stipp_pane.revalidate();

                    }
                });
            }
        });

    }

    private void disconAction() {
        try {

            sidePane.removeAll();
            stus_stipp_pane.removeAll();
            rightPane.removeAll();

            try {
                global_split_pane.remove(stus_stipp_pane);
                global_split_pane.remove(sidePane);
            } catch (NullPointerException e){}
            atrzPanel.setVisible(true);
            workPanel.removeAll();
            subPaneWithCategs.removeAll();
            Kategorii.clear();
            workPanel.setVisible(false);
            inputPane.revalidate();

            mainThread.disconnect();
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "IN THE DISCONNECTION"+ex.getMessage(), "Error", WIDTH);
        }
    }

    private void canOrderAction() {

        sidePane.removeAll();
        sidePane.revalidate();
        ProdOrdTable = null;
        scPfPrOrdTable = null;
    }

    private void mkOrderAction() {
        if (sidePane.getComponentCount() != 0)
            JOptionPane.showMessageDialog(this, "Завершите, уже существующий заказ.", "Заказ не завершен!", WIDTH);
        else {
            mainThread.mkOrderTable();
            sidePane.add(new JLabel("Менеджер: " + mainThread.getUser().getName() + " "+ mainThread.getUser().getSurname() +"                  Номер заказа: " + mainThread.getProdOrder_tab().getOrderNumber()), BorderLayout.NORTH);
            ProdOrdTable = new JTable(mainThread.getProdOrder_tab());
            ProdOrdTable.getColumn("Удалить").setCellRenderer(new ButtonRenderer());
            ProdOrdTable.getColumn("Удалить").setCellEditor(
                    new ButtonEditor(new JCheckBox()));
            scPfPrOrdTable = new JScrollPane(ProdOrdTable);
            sidePane.add(scPfPrOrdTable, BorderLayout.CENTER);
            JTextField commentTextField = new JTextField("Комментарий...", 20);



            JButton sendOrder = new JButton("Отправить");
            sendOrder.addActionListener((ActionEvent ev) -> {

                mainThread.getProdOrder_tab().setComment(commentTextField.getText());
                mainThread.exportNewOrder();
                JOptionPane.showMessageDialog(this, "Заказ принят.", "Заказ", WIDTH);
                canOrderAction();
                global_split_pane.setTopComponent(null);
                global_split_pane.setDividerLocation(0);
                global_split_pane.revalidate();

            });
            JPanel intputOrderPane = new JPanel();
            intputOrderPane.add(commentTextField, BorderLayout.SOUTH);
            intputOrderPane.add(sendOrder);
            intputOrderPane.add(canOrder);
            sidePane.add(intputOrderPane, BorderLayout.SOUTH);
            sidePane.revalidate();
        }
    }






}
