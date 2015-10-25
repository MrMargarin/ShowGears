package Graphical_UI;

import mai_n.Logic_main;
import mai_n.MyException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
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


    private JPanel workPanel; //панель с кнопками и поиском
    private JPanel atrzPanel; //панель для подключения к БД
    private JTable STUSTable, STIPPTable, ProdOrdTable, OrderListTable; //Таблицы
    private JLabel pass, login;
    private JTextField user;
    private JPasswordField password; //authorization
    private JTextField search;
    private ArrayList<JComboBox> Kategorii;
    private JButton connect, show, discon, mkOder, showOrders, canOrder, exportOrders, showOrdersS, closeOrders;

    private KeyAdapter keyLforConn, keyLforShow;

    private JSplitPane stus_stipp_pane, global_split_pane;
    private JPanel inputPane, outputPane, sidePane, rightPane; // panel for make orders;
    private JScrollPane scPfSTUSTable, scPfSTIPPTable, scPfPrOrdTable, scPfOrdLstTable, scPfOrdLstSaleTable; //   "scPf" - scroll panel for

    private Logic_main mainThread;

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
                } catch (SQLException ex) {
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
        show = new JButton("Искать");
        show.addActionListener((ActionEvent e) -> {
            searchAction();
            global_split_pane.setBottomComponent(stus_stipp_pane);
            global_split_pane.revalidate();
        });



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
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int answer = JOptionPane.showConfirmDialog(button, "Удалить выбранный товар из заказа?");
                    if(answer == JOptionPane.YES_OPTION)
                    {
                        //System.out.println(super.getComponent().getClass().toString());
                        //System.out.println("before: ordertabSIZE: "+mainThread.getOrderTabMod().getRowCount()+" | row index: "+row);
                        mainThread.getOrderTabMod().removeRow(ProdOrdTable.getSelectedRow());


                        //System.out.println("after: ordertabSIZE: " + mainThread.getOrderTabMod().getRowCount() + " | row index: " + row);
                    }
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
            return new String(label);
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
        String manName, ordNumber;
        manName = this.OrderListTable.getValueAt(this.OrderListTable.getSelectedRow(), 2).toString() + " " + this.OrderListTable.getValueAt(this.OrderListTable.getSelectedRow(), 3).toString();

        ordNumber = this.OrderListTable.getValueAt(this.OrderListTable.getSelectedRow(), 0).toString();
        mainThread.getOrderTabMod().exportToExcell(manName, ordNumber);
        JOptionPane.showMessageDialog(this, "Файл был сохранен в папке "+mainThread.getOrderTabMod().getPath());
    }

    private void connectAction()
    {
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


        try
        {
            mainThread.build_STUSnSTIPP();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }

        if(mainThread.getUser().getType()==1) //sale
        {
            Vector<Vector> vecOFCats = mainThread.getParList().getParamsVector();
            for(int ind = 0; ind < vecOFCats.size(); ind++)
            Kategorii.add(new JComboBox(vecOFCats.get(ind)));


            workPanel.add(new JLabel("Найти:"));
            workPanel.add(search);
            for(int d=0; d<Kategorii.size(); d++){
            workPanel.add(Kategorii.get(d));}

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

    private void searchAction()
    {
        try {
            if(stus_stipp_pane.isAncestorOf(scPfSTIPPTable)){ stus_stipp_pane.remove(scPfSTIPPTable); }
            //================Поисковик=================================

            String catFull = "";
            for(int i =0; i<Kategorii.size(); i++) catFull+=Kategorii.get(i).getSelectedItem().toString() + "/";

            StringBuilder cFull = new StringBuilder(catFull);
            cFull.deleteCharAt(cFull.length() - 1);

            mainThread.mkSTUSsearch(search.getText(), cFull.toString());

            if(mainThread.getMainTableMod().getNumOfItFnd()==0) JOptionPane.showMessageDialog(this, "По заданным параметрам не найдено ни одного товара.", "X", WIDTH);

            STUSTable = new JTable(mainThread.getMainTableMod().getMainTab()){
                @Override
                public boolean isCellEditable(int arg0, int arg1){return false;}};
            //==========================================================

            //================Установка Сортировщика====================
            TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getMainTableMod().getMainTab());
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
                            Vector ordProd = new Vector();
                            ordProd.add(model.getValueAt(row, 0));
                            ordProd.add(model.getValueAt(row, 1));
                            ordProd.add(model.getValueAt(row, 2));
                            ordProd.add(new Integer(0));
                            ordProd.add("Удалить");
                            mainThread.getOrderTabMod().addRow(ordProd);
                        }
                    }
                }
            });

            //==========================Table-STIPP======================================
            ListSelectionModel selModel = STUSTable.getSelectionModel();
            selModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    try {
                        int[] selectedRows = STUSTable.getSelectedRows();
                        if(selectedRows.length!=0) {
                            int selIndex = selectedRows[0];
                            TableModel model = STUSTable.getModel();
                            Object value = model.getValueAt(selIndex, 0);

                            mainThread.mkSTIPP(value.toString());
                            TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(mainThread.getSubTableMod().getSubTab());
                            STIPPTable = new JTable(mainThread.getSubTableMod().getSubTab());
                            STIPPTable.setRowSorter(sorter2);
                            STIPPTable.setFillsViewportHeight(true);
                            scPfSTIPPTable = new JScrollPane(STIPPTable);
                            stus_stipp_pane.setBottomComponent(scPfSTIPPTable);
                        }

                        double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                        stus_stipp_pane.setDividerLocation(0.5);
                        //stus_stipp_pane.revalidate();

                    } catch (SQLException ex) {

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
        if (mainThread.getOrderListSaleTabMod().getRowCount() == 0)
            JOptionPane.showMessageDialog(this, "Заказов нет.", "X", WIDTH);

        OrderListTable = new JTable(mainThread.getOrderListSaleTabMod()) {
            @Override
            public boolean isCellEditable(int row, int col) {return false;}
        };

        OrderListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //==========================================================

        //================Установка Сортировщика====================
        TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getOrderListSaleTabMod());
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
        if(mainThread.getOrderListTabMod().getRowCount()==0) JOptionPane.showMessageDialog(this, "Заказов нет.", "X", WIDTH);

        OrderListTable = new JTable(mainThread.getOrderListTabMod()){
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
        TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getOrderListTabMod());
        OrderListTable.setRowSorter(sorter1);
        //================Установка Ширины Колонок==================

        scPfOrdLstTable = new JScrollPane(OrderListTable);

        sidePane.removeAll();
        sidePane.add(scPfOrdLstTable, BorderLayout.CENTER);
        sidePane.revalidate();

        //==========================Table-STIPP======================================
        ListSelectionModel selModel = OrderListTable.getSelectionModel();
        selModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int[] selectedRows = OrderListTable.getSelectedRows();
                    if (selectedRows.length != 0) {
                        int selIndex = selectedRows[0];
                        TableModel model = OrderListTable.getModel();
                        Object value = model.getValueAt(selIndex, 0);

                        mainThread.mkProdOrderTable((new Integer(value.toString())));
                        TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(mainThread.getOrderTabMod());
                        ProdOrdTable = new JTable(mainThread.getOrderTabMod());
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
                            Object val = model2.getValueAt(selInx, 0);
                            try {
                                mainThread.mkSTIPP(val.toString());
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }

                        TableRowSorter<TableModel> sorter3 = new TableRowSorter<TableModel>(mainThread.getSubTableMod().getSubTab());
                        STIPPTable = new JTable(mainThread.getSubTableMod().getSubTab());
                        STIPPTable.setRowSorter(sorter3);
                        STIPPTable.setFillsViewportHeight(true);
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
            sidePane.add(new JLabel("Менеджер: " + mainThread.getUser().getName() + " "+ mainThread.getUser().getSurname() +"                  Номер заказа: " + mainThread.getOrderTabMod().getOrderNumber()), BorderLayout.NORTH);
            ProdOrdTable = new JTable(mainThread.getOrderTabMod());
            ProdOrdTable.getColumn("Удалить").setCellRenderer(new ButtonRenderer());
            ProdOrdTable.getColumn("Удалить").setCellEditor(
                    new ButtonEditor(new JCheckBox()));
            scPfPrOrdTable = new JScrollPane(ProdOrdTable);
            sidePane.add(scPfPrOrdTable, BorderLayout.CENTER);
            JTextField commentTextField = new JTextField("Комментарий...", 20);



            JButton sendOrder = new JButton("Отправить");
            sendOrder.addActionListener((ActionEvent ev) -> {

                mainThread.getOrderTabMod().setComment(commentTextField.getText());
                mainThread.getOrderTabMod().exportTable();
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
