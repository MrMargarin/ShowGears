package Graphical_UI;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.Vector;
import mai_n.*;
import table_models.*;

public class MainFrameProgram extends JFrame {

    private long start, stop, resulttime;
    private final String namField = "prodName"; //название поля наименований товаров, по которому проводится поиск
    
    private MainSQLTable mainTableMod;
    private SubSQLTable subTableMod;
    private OrderTable orderTabMod;     //модели таблиц
    
    private ArrayList<JLabel> Colums;
    private JPanel workPanel; //панель с кнопками и поиском
    private JPanel atrzPanel; //панель для подключения к БД
    private JPanel filtPan;
    private JTable STUSTable, STIPPTable, OrderTable; //Таблицы

    private String urlToDB = "jdbc:mysql://localhost/main_db";
    private String userName; //current user
    private JLabel pass, login;
    private JTextField user;
    private JPasswordField password; //authorization

    private JTextField search;
    private String srch;

    private JComboBox Categories, Kategorii;
    private ParamList cats;     //one of parameters of products

    private JButton connect, show, discon, mkOder;

    private JSplitPane stus_stipp_pane;
    private JPanel inputPane, outputPane, ordPane; // panel for make orders;
    private JScrollPane scPfSTUSTable, scPfSTIPPTable, scPfORDERTable; //   "scPf" - scroll panel for



    private MySQLConnector connector;

    public MainFrameProgram() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


        orderTabMod = null;


        Colums = new ArrayList();   //Colum names
        inputPane = new JPanel();
        filtPan = new JPanel();
        atrzPanel = new JPanel();
        workPanel = new JPanel();


        ordPane = new JPanel(new BorderLayout());

        stus_stipp_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);

        search = new JTextField("72", 10); //-----------------search field

                
               
        this.setTitle("MetallGearsViewer");
        this.setSize(1000, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            try {
                if (connector!=null) connector.disconnect();
                } catch (SQLException ex) {
                      Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        
        login = new JLabel("Login");
        user = new JTextField("root", 4);
        pass = new JLabel("Pass:");
        password = new JPasswordField("",7);
        
//===================================="Connect"=================================
        connect = new JButton("Connect");
        connect.addActionListener((ActionEvent e) -> {
            start = System.nanoTime();
            try {
                try {
                    connector = new MySQLConnector(urlToDB, user.getText(), password.getText());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "ex.getMessage()", "Ошибка авторизации", WIDTH);
                }

                userName = user.getText(); //init user current name
                mainTableMod = new MainSQLTable(connector);
                subTableMod = new SubSQLTable(connector);
                orderTabMod = new OrderTable(connector, userName);

                atrzPanel.setVisible(false);
                workPanel.setVisible(true);
                search.requestFocus(); //focus search field


                cats = new ParamList("CatName", "stus_table", connector);
                Kategorii = new JComboBox(cats.getValues());
                workPanel.add(Kategorii);                           //there is categories initialization

                inputPane.revalidate();

                stop = System.nanoTime();
                resulttime = stop - start;
                System.out.println("connection is "+resulttime);

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
            }
        });


//====================================="Show"===================================
        show = new JButton("Search");
        show.addActionListener((ActionEvent e) -> {
        try {start = System.nanoTime();
            if(stus_stipp_pane.isAncestorOf(scPfSTIPPTable)){
                stus_stipp_pane.remove(scPfSTIPPTable);}
            //================Поисковик=================================
            srch = "select * from stus_table where "+namField+" like \'%" + search.getText() + "%\' and CatName like \'"+Kategorii.getSelectedItem()+"\'";
            mainTableMod.fillTable(srch);
            if(mainTableMod.getNumOfItFnd()==0) JOptionPane.showMessageDialog(this, "По заданным параметрам не найдено ни одного товара.", "Нету!", WIDTH);
            STUSTable = new JTable(mainTableMod.getMainTab()){
                @Override
                public boolean isCellEditable(int arg0, int arg1){return false;}};
            //==========================================================

            //================Установка Сортировщика====================
            TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainTableMod.getMainTab());
            STUSTable.setRowSorter(sorter1);
            //================Установка Ширины Колонок==================
            STUSTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            STUSTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            STUSTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            scPfSTUSTable = new JScrollPane(STUSTable);

            //==========================Table-Order-add_new_product=======================
            STUSTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table =(JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    TableModel model = STUSTable.getModel();
                    if (me.getClickCount() == 2) {
                           if(OrderTable!=null)
                           {
                               Vector ordProd = new Vector();
                               ordProd.add(model.getValueAt(row, 0));
                               ordProd.add(model.getValueAt(row, 1));
                               ordProd.add(model.getValueAt(row, 2));
                               ordProd.add(new Integer(0));
                               ordProd.add("Удалить");
                               orderTabMod.addRow(ordProd);
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
                        //if(stus_stipp_pane.isAncestorOf(scPfSTIPPTable)){stus_stipp_pane.remove(scPfSTIPPTable);}
                        int[] selectedRows = STUSTable.getSelectedRows();
                        //for(int i = 0; i<selectedRows.length; i++)
                        if(selectedRows.length!=0)
                        {
                            int selIndex = selectedRows[0];
                            TableModel model = STUSTable.getModel();
                            Object value = model.getValueAt(selIndex, 0);
                            //subTableMod.fillTable(value.toString(), Categories.getSelectedItem().toString());
                            subTableMod.fillTable(value.toString(), "stipp_table");
                            TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(subTableMod.getSubTab());
                            STIPPTable = new JTable(subTableMod.getSubTab());
                            STIPPTable.setRowSorter(sorter2);
                            STIPPTable.setFillsViewportHeight(true);
                            scPfSTIPPTable = new JScrollPane(STIPPTable);
                            stus_stipp_pane.setBottomComponent(scPfSTIPPTable);
                        }
                        double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                        stus_stipp_pane.setDividerLocation(0.5);
                        stus_stipp_pane.revalidate();
                        
                    } catch (SQLException ex) {
                        
                        Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            stus_stipp_pane.setTopComponent(scPfSTUSTable); //панель с таблицей результатов товаров
            stus_stipp_pane.validate();

            stop = System.nanoTime();
            resulttime = stop - start;
            System.out.println("show is " + resulttime);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "IN THE CONNECTION"+ex.getMessage(), "Error", WIDTH);
        }
            
    });
//====================================="Disconnect"=============================
        discon = new JButton("Discon");
        discon.addActionListener((ActionEvent e) -> {        
            if(mainTableMod==null) {System.out.println("tab is NULL");}
            else try {
                stus_stipp_pane.removeAll();
                stus_stipp_pane.revalidate();
                //mainTableMod.closeConec();
                //subTableMod.closeConec();
                connector.disconnect();
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
            if (ordPane.getComponentCount() != 0)
                JOptionPane.showMessageDialog(this, "Завершите, уже существующий заказ.", "Заказ не завершен!", WIDTH);
            else {
                ordPane.add(new JLabel("Менеджер: " + userName), BorderLayout.NORTH);
                OrderTable = new JTable(orderTabMod);
                OrderTable.getColumn("Удалить").setCellRenderer(new ButtonRenderer());
                OrderTable.getColumn("Удалить").setCellEditor(
                        new ButtonEditor(new JCheckBox()));
                scPfORDERTable = new JScrollPane(OrderTable);
                ordPane.add(scPfORDERTable, BorderLayout.CENTER);

                JTextField commentTextField = new JTextField("Комментарий...");
                JButton sendOrder = new JButton("Отправить");
                sendOrder.addActionListener((ActionEvent ev) -> {
                    orderTabMod.setComment(commentTextField.getText());
                    orderTabMod.exportTable();
                    ordPane.removeAll();
                    ordPane.revalidate();
                });
                JPanel intputOrderPane = new JPanel();
                intputOrderPane.add(commentTextField, BorderLayout.SOUTH);
                intputOrderPane.add(sendOrder);
                ordPane.add(intputOrderPane, BorderLayout.SOUTH);
                ordPane.revalidate();
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
        this.getContentPane().add(new JLabel("1"), BorderLayout.WEST);
        this.getContentPane().add(ordPane, BorderLayout.EAST);
        this.getContentPane().add(stus_stipp_pane, BorderLayout.CENTER);
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
     * @version 1.0 11/09/98
     */

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

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
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                JOptionPane.showMessageDialog(button, label + ": Ouch!");
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
