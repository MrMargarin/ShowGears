package Graphical_UI;

import mai_n.Logic_main;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


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

    private JComboBox Kategorii;


    private JButton connect, show, discon, mkOder, showOrders;

    private JSplitPane stus_stipp_pane;
    private JSplitPane po_stipp_pane;
    private JPanel inputPane, outputPane, _S_ordPane, _P_ordPane; // panel for make orders;
    private JScrollPane scPfSTUSTable, scPfSTIPPTable, scPfPrOrdTable, scPfOrdLstTable; //   "scPf" - scroll panel for

    private Logic_main mainThread;


    public Graphic_main() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        this.setTitle("MetallGearsViewer");
        this.setSize(1000, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    mainThread.disconnect();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrameProgram2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });


        stus_stipp_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        po_stipp_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        inputPane = new JPanel();
        atrzPanel = new JPanel();
        workPanel = new JPanel();
        _S_ordPane = new JPanel(new BorderLayout());
        _P_ordPane = new JPanel(new BorderLayout());
        scPfOrdLstTable = new JScrollPane();

        search = new JTextField("72", 10); //-----------------search field
        login = new JLabel("Login");
        user = new JTextField("root", 4);
        pass = new JLabel("Pass:");
        password = new JPasswordField("",7);

//===================================="Connect"=================================
        connect = new JButton("Войти");
        connect.addActionListener((ActionEvent e) -> {
        try {
                mainThread = new Logic_main(user.getText(), password.getText());
            }
        catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка авторизации", WIDTH);
            }
        catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

        atrzPanel.setVisible(false);
        workPanel.setVisible(true);
        search.requestFocus(); //focus search field

        try
        {
            mainThread.build_STUSnSTIPP();
        }
        catch (SQLException e1) {
                e1.printStackTrace();
        }

        Kategorii = new JComboBox(mainThread.getCats().getValues());
        workPanel.add(Kategorii);                           //there is categories initialization

        inputPane.revalidate();
        });


//====================================="Show"===================================
        show = new JButton("Search");
        show.addActionListener((ActionEvent e) -> {
        try {
            if(stus_stipp_pane.isAncestorOf(scPfSTIPPTable)){ stus_stipp_pane.remove(scPfSTIPPTable); }
                //================Поисковик=================================

            mainThread.mkSTUSsearch(search.getText(), Kategorii.getSelectedItem());
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
                        stus_stipp_pane.revalidate();

                        } catch (SQLException ex) {

                            Logger.getLogger(MainFrameProgram2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                stus_stipp_pane.setTopComponent(scPfSTUSTable); //панель с таблицей результатов товаров
                stus_stipp_pane.validate();



            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "IN THE CONNECTION" + ex.getMessage(), "Error", WIDTH);
            }

        });
//====================================="Show Order"=============================

        showOrders = new JButton("Показать Заказы");
        showOrders.addActionListener((ActionEvent e) -> {

            try {
                mainThread.build_Order_Tables();
                mainThread.mkOrderListTable();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if(mainThread.getOrderListTabMod().getRowCount()==0) JOptionPane.showMessageDialog(this, "Заказов нет.", "X", WIDTH);

            OrderListTable = new JTable(mainThread.getOrderListTabMod()){
                @Override
                public boolean isCellEditable(int arg0, int arg1){return false;}};
            //==========================================================

            //================Установка Сортировщика====================
            TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainThread.getMainTableMod().getMainTab());
            OrderListTable.setRowSorter(sorter1);
            //================Установка Ширины Колонок==================
            OrderListTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            OrderListTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            OrderListTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            scPfOrdLstTable = new JScrollPane(OrderListTable);
            _P_ordPane.add(scPfOrdLstTable, BorderLayout.CENTER);
            _P_ordPane.revalidate();

            //==========================Table-Order-add_new_product=======================
            OrderListTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    TableModel model = OrderListTable.getModel();
                    if (me.getClickCount() == 2) {
                        //to do when double click in order list table
                    }
                }
            });

            //==========================Table-STIPP======================================
            ListSelectionModel selModel = OrderListTable.getSelectionModel();
            selModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    try {
                        int[] selectedRows = OrderListTable.getSelectedRows();
                        if(selectedRows.length!=0) {
                            int selIndex = selectedRows[0];
                            TableModel model = OrderListTable.getModel();
                            Object value = model.getValueAt(selIndex, 0);

                            mainThread.mkProdOrderTable((int) value);
                            TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(mainThread.getOrderTabMod());
                            ProdOrdTable = new JTable(mainThread.getOrderTabMod());
                            ProdOrdTable.setRowSorter(sorter2);
                            ProdOrdTable.setFillsViewportHeight(true);
                            scPfPrOrdTable = new JScrollPane(ProdOrdTable);

                        }

                        //double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                        po_stipp_pane.setTopComponent(scPfPrOrdTable);
                        po_stipp_pane.setDividerLocation(0.5);
                        po_stipp_pane.revalidate();

                    } catch (SQLException ex) {

                        Logger.getLogger(MainFrameProgram2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ListSelectionModel selModel2 = ProdOrdTable.getSelectionModel();
                    selModel2.addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            int[] selRows = ProdOrdTable.getSelectedRows();
                            if(selRows.length!=0) {
                                int selInx = selRows[0];
                                TableModel model2 = ProdOrdTable.getModel();
                                Object val = model2.getValueAt(selInx, 2);
                                try {
                                    mainThread.mkSTIPP(val.toString());
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }

                                TableRowSorter<TableModel> sorter3 = new TableRowSorter<TableModel>(mainThread.getSubTableMod().getSubTab());
                                STIPPTable = new JTable(mainThread.getSubTableMod().getSubTab());
                                STIPPTable.setRowSorter(sorter3);
                                STIPPTable.setFillsViewportHeight(true);
                                scPfSTIPPTable = new JScrollPane(STIPPTable);
                                po_stipp_pane.setBottomComponent(scPfSTIPPTable);

                            }
                        }
                    });
                }

            });



            po_stipp_pane.revalidate();


        });


//====================================="Disconnect"=============================
    discon = new JButton("Discon");
    discon.addActionListener((ActionEvent e) -> {
    try {
        stus_stipp_pane.removeAll();
        stus_stipp_pane.revalidate();
        mainThread.disconnect();
        atrzPanel.setVisible(true);
        workPanel.setVisible(false);
        inputPane.revalidate();
    }
    catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "IN THE DISCONNECTION"+ex.getMessage(), "Error", WIDTH);
    }
    });

//====================================="Make Oder"=============================


    mkOder = new JButton("Заказ");
    mkOder.addActionListener((ActionEvent e) -> {
    if (_S_ordPane.getComponentCount() != 0)
    JOptionPane.showMessageDialog(this, "Завершите, уже существующий заказ.", "Заказ не завершен!", WIDTH);
    else {
    mainThread.mkOrderTable();
    _S_ordPane.add(new JLabel("Менеджер: " + mainThread.getUserName() + "                  Номер заказа: " + mainThread.getOrderTabMod().getOrderNumber()), BorderLayout.NORTH);
    ProdOrdTable = new JTable(mainThread.getOrderTabMod());
    ProdOrdTable.getColumn("Удалить").setCellRenderer(new ButtonRenderer());
    ProdOrdTable.getColumn("Удалить").setCellEditor(
    new ButtonEditor(new JCheckBox()));
    scPfPrOrdTable = new JScrollPane(ProdOrdTable);
    _S_ordPane.add(scPfPrOrdTable, BorderLayout.CENTER);
    JTextField commentTextField = new JTextField("Комментарий...", 70);
    JButton sendOrder = new JButton("Отправить");
    sendOrder.addActionListener((ActionEvent ev) -> {
    mainThread.getOrderTabMod().setComment(commentTextField.getText());
    mainThread.getOrderTabMod().exportTable();

    _S_ordPane.removeAll();
    _S_ordPane.revalidate();
    });
    JPanel intputOrderPane = new JPanel();
    intputOrderPane.add(commentTextField, BorderLayout.SOUTH);
    intputOrderPane.add(sendOrder);
    _S_ordPane.add(intputOrderPane, BorderLayout.SOUTH);
    _S_ordPane.revalidate();
    }
    });

    atrzPanel.add(login);
    atrzPanel.add(user);
    atrzPanel.add(pass);
    atrzPanel.add(password);
    atrzPanel.add(connect);


    workPanel.add(new JLabel("Найти:"));
    workPanel.add(search);
    workPanel.add(show);
    workPanel.add(discon);
    workPanel.add(mkOder);
    workPanel.add(showOrders);


    inputPane.add(atrzPanel);
    inputPane.add(workPanel);
    atrzPanel.setVisible(true);
    workPanel.setVisible(false);
                //KEYS_BINDS
    KeyAdapter keyLforConn = new KeyAdapter() {
    @Override
    public void keyReleased(java.awt.event.KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_ENTER)
    connect.doClick();
    }};

    KeyAdapter keyLforShow = new KeyAdapter() {
    @Override
    public void keyReleased(java.awt.event.KeyEvent ke) {
    if(ke.getKeyCode() == KeyEvent.VK_ENTER)
        show.doClick();
    }};

    password.addKeyListener(keyLforConn);
    user.addKeyListener(keyLforConn);
    search.addKeyListener(keyLforShow);
    //===================================set icon=======================
    Image img = new ImageIcon("images\\Mini_logo.png").getImage();
    this.setIconImage(img);
    this.getContentPane().add(inputPane, BorderLayout.SOUTH);
    this.getContentPane().add(_P_ordPane, BorderLayout.WEST);
    this.getContentPane().add(_S_ordPane, BorderLayout.EAST);
    //this.getContentPane().add(stus_stipp_pane, BorderLayout.CENTER);
        this.getContentPane().add(po_stipp_pane, BorderLayout.CENTER);
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

    /**
     * @version 1.0 11/09/98 Кнопка
     */

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;
        private int row;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
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
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                int answer = JOptionPane.showConfirmDialog(button, "Удалить выбранный товар из заказа?");
                if(answer == JOptionPane.YES_OPTION)
                {
                    System.out.println(super.getComponent().getClass().toString());
                    System.out.println("before: ordertabSIZE: "+mainThread.getOrderTabMod().getRowCount()+" | row index: "+row);
                    mainThread.getOrderTabMod().removeRow(row);

                    System.out.println("after: ordertabSIZE: " + mainThread.getOrderTabMod().getRowCount() + " | row index: " + row);
                }


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
}
