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

    private final String namField = "prodName"; //название поля наименований товаров, по которому проводится поиск
    
    private MainSQLTable mainTableMod;
    private SubSQLTable subTableMod;
    private OrderTable orderTabMod;
    
    private ArrayList<JLabel> Colums;
    private JPanel workPanel; //панель с кнопками и поиском
    private JPanel atrzPanel; //панель для подключения к БД
    private JPanel filtPan;
    private JTable STUSTable, STIPPTable, OrderTable;
    
    private ArrayList<JTable> prov;
    private JTabbedPane tabs;
    private JTextField url;
    private JTextField user;
    private JPasswordField password;
    private JTextField sql, search;
    private JComboBox Categories, Kategorii;
    private String srch;
    private MySQLConnector connector;
    private JButton connect, show, discon, mkOder;
    private JLabel pass, login;
    private JSplitPane stus_stipp_pane;
    private JPanel inputPane, outputPane;
    private JScrollPane scPfSTUSTable, scPfSTIPPTable, scPfORDERTable; //   "scPf" - scroll panel for
    
    private ParamList cats;
    //private String urlToDB = "jdbc:mysql://192.168.1.158/metlgears_db";
    private String urlToDB = "jdbc:mysql://localhost/main_db";
    //private String[] tables = {"drill", "tapndie", "another" }; //difrent tables
    private String[] tables = {"tabledrill", "tabletapndie", "another" }; //difrent tables
    private JButton showt;
    private String userName;
    private JPanel ordPane;
    private TableColumn addColumn;
    
    public MainFrameProgram() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Categories = new JComboBox(tables); //

        orderTabMod = null;


        Colums = new ArrayList();   //Colum names
        inputPane = new JPanel();
        filtPan = new JPanel();
        atrzPanel = new JPanel();
        workPanel = new JPanel();


        ordPane = new JPanel(new BorderLayout());
        tabs = new JTabbedPane();
        //mPane = new JPanel(new GridLayout(1,2));
        //stus_stipp_pane = new JPanel();
        //stus_stipp_pane.setLayout(new BoxLayout(stus_stipp_pane, BoxLayout.Y_AXIS));
        stus_stipp_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        //stus_stipp_pane.setDividerLocation(0.6);
        search = new JTextField("72", 10); //-----------------search field
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
               
        this.setTitle("MetallGearsViewer");
        this.setSize(1000, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            try {
                //connector.disconnect();
                mainTableMod.closeConec();
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
            try {
                try{
                connector = new MySQLConnector(urlToDB, user.getText(), password.getText());}
                catch(SQLException ex)
                {JOptionPane.showMessageDialog(this, "ex.getMessage()", "Ошибка авторизации", WIDTH);

                }
                userName = user.getText();
                mainTableMod = new MainSQLTable(connector);
                subTableMod = new SubSQLTable(connector);
                orderTabMod = new OrderTable(connector, userName);

                atrzPanel.setVisible(false);
                workPanel.setVisible(true);
                search.requestFocus();
                
                //inputPane.remove(atrzPanel);
                //inputPane.add(workPanel);
                
                //cats = new ParamList("CatName", Categories.getSelectedItem().toString(), connector);
                cats = new ParamList("CatName", "stus_table", connector);
                Kategorii = new JComboBox(cats.getValues());
                workPanel.add(Kategorii);
                inputPane.revalidate();
                
                
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //mkoder = new JButton("Oder");
        //mkoder.addActionListener((ActionEvent e) -> {
        //
        //    OrderTable = new JTable
        //    
        //    
        //});
//====================================="Show"===================================
        show = new JButton("Search");
        //show.setIcon(new ImageIcon("images\\search_icon.png"));
        
        show.addActionListener((ActionEvent e) -> {        
            
        try {
            
            //if(stus_stipp_pane.isAncestorOf(scPfSTUSTable)){stus_stipp_pane.remove(scPfSTUSTable);}
            if(stus_stipp_pane.isAncestorOf(scPfSTIPPTable)){
                stus_stipp_pane.remove(scPfSTIPPTable);}
            //================Поле поиска===============================
            System.out.print("Search field is: ");
            //srch = "select * from " + Categories.getSelectedItem() + " where Name like \'%" + search.getText() + "%\'";
            //srch = "select * from " + Categories.getSelectedItem() + " where "+namField+" like \'%" + search.getText() + "%\' and CatName like \'"+Kategorii.getSelectedItem()+"\'";
            srch = "select * from stus_table where "+namField+" like \'%" + search.getText() + "%\' and CatName like \'"+Kategorii.getSelectedItem()+"\'";
            System.out.println("global search"+srch);
            //==========================================================
            
            mainTableMod.fillTable(srch);
            if(mainTableMod.getNumOfItFnd()==0) JOptionPane.showMessageDialog(this, "По заданным параметрам не найдено ни одного товара.", "Нету!", WIDTH);
            STUSTable = new JTable(mainTableMod.getMainTab()){
                @Override
                public boolean isCellEditable(int arg0, int arg1){return false;}};
            //================Установка Сортировщика====================
            TableRowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(mainTableMod.getMainTab());
            STUSTable.setRowSorter(sorter1);
            
            //================Установка Ширины Колонок==================
            STUSTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            STUSTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            STUSTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            scPfSTUSTable = new JScrollPane(STUSTable);
            
            //==========================Table-subTable======================================

            STUSTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table =(JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    TableModel model = STUSTable.getModel();
                    if (me.getClickCount() == 1) {
                        try{

                                    Object value = model.getValueAt(row, 0);
                                    //subTableMod.fillTable(value.toString(), Categories.getSelectedItem().toString());
                                    subTableMod.fillTable(value.toString(), "stipp_table");
                                    TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(subTableMod.getSubTab());
                                    STIPPTable = new JTable(subTableMod.getSubTab());
                                    STIPPTable.setRowSorter(sorter2);
                                    STIPPTable.setFillsViewportHeight(true);
                                    scPfSTIPPTable = new JScrollPane(STIPPTable);
                                    stus_stipp_pane.setBottomComponent(scPfSTIPPTable);

                                double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                                stus_stipp_pane.setDividerLocation(0.5);
                                stus_stipp_pane.revalidate();

                            } catch (SQLException ex) {

                                Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    else if (me.getClickCount() == 2) {
                           if(OrderTable!=null)
                           {
                               Vector ordProd = new Vector();
                               ordProd.add(model.getValueAt(row, 0));
                               ordProd.add(model.getValueAt(row, 1));
                               ordProd.add(model.getValueAt(row, 2));
                               ordProd.add(new Integer(0));
                               ordProd.add("Жмак!");
                               orderTabMod.addRow(ordProd);
                           }
                    }
                }
            });

            /*ListSelectionModel selModel = STUSTable.getSelectionModel();

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
            }); */
            
            stus_stipp_pane.setTopComponent(scPfSTUSTable); //панель с таблицей результатов товаров
            //scPfSTUSTable.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            //stus_stipp_pane.setDividerLocation(0.7);
            stus_stipp_pane.validate();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", WIDTH);
            //Logger.getLogger(SimpleFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    });
//====================================="Disconnect"=============================
        discon = new JButton("Discon");
        discon.addActionListener((ActionEvent e) -> {        
            if(mainTableMod==null) {System.out.println("tab is NULL");}
            else try {
                stus_stipp_pane.removeAll();
                stus_stipp_pane.revalidate();
                mainTableMod.closeConec();
                subTableMod.closeConec();
                
                atrzPanel.setVisible(true);
                workPanel.setVisible(false);
                //inputPane.add(atrzPanel);
                //inputPane.remove(workPanel);
                inputPane.revalidate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//====================================="Make Oder"=============================

        mkOder = new JButton("Заказ");
        mkOder.addActionListener((ActionEvent e) -> {
            if(ordPane.getComponentCount()!=0)
            {   JOptionPane.showMessageDialog(this, "Завершите, уже существующий заказ.", "Заказ не завершен!", WIDTH);

            }

            else
            {



                ordPane.add(new JLabel("Менеджер: "+userName), BorderLayout.NORTH);

                OrderTable = new JTable(orderTabMod);
                scPfORDERTable = new JScrollPane(OrderTable);
                ordPane.add(scPfORDERTable, BorderLayout.CENTER);

                JTextField commentTextField = new JTextField("Комментарий...");
                JButton sendOrder = new JButton("Отправить");
                sendOrder.addActionListener((ActionEvent ev) -> {
                    orderTabMod.setComment(commentTextField.getText());
                    orderTabMod.exportTable();
                    ordPane.removeAll();
                    stus_stipp_pane.revalidate();
                });
                JPanel intputOrderPane = new JPanel();
                intputOrderPane.add(commentTextField, BorderLayout.SOUTH);
                intputOrderPane.add(sendOrder);
                ordPane.add(intputOrderPane, BorderLayout.SOUTH);
                ordPane.revalidate();
            }
        });

        /*Categories.addActionListener (new ActionListener () {
        public void actionPerformed(ActionEvent e) {
            try {
                if(workPanel.isAncestorOf(Kategorii)) workPanel.remove(Kategorii);
                cats = new ParamList("CatName", Categories.getSelectedItem().toString(), connector);
                Kategorii = new JComboBox(cats.getValues());
                workPanel.add(Kategorii);
                inputPane.revalidate();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        });*/
        
        
        atrzPanel.add(login);
        atrzPanel.add(user);
        atrzPanel.add(pass);
        atrzPanel.add(password);
        atrzPanel.add(connect);
        
        workPanel.add(Categories);
        workPanel.add(new JLabel("Найти:"));
        workPanel.add(search);
        workPanel.add(show);
        workPanel.add(discon);
        workPanel.add(mkOder);
        
        inputPane.add(atrzPanel);
        inputPane.add(workPanel);
        atrzPanel.setVisible(true);
        workPanel.setVisible(false);
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
        //inputPane.add(atrzPanel);
        //inputPane.validate();
        //Image img = new ImageIcon("C:\\Users\\rtert\\Documents\\NetBeansProjects\\JavaApplication1\\images\\Mini_logo.png").getImage();
        Image img = new ImageIcon("images\\Mini_logo.png").getImage();
        this.setIconImage(img);
        this.getContentPane().add(inputPane, BorderLayout.SOUTH);
        this.getContentPane().add(new JLabel("1"), BorderLayout.WEST);
        this.getContentPane().add(ordPane, BorderLayout.EAST);
        this.getContentPane().add(stus_stipp_pane, BorderLayout.CENTER);
    }

    private void add_column_constructor()
    {
        addColumn = new TableColumn(1,30 , new ButtonRenderer(), new ButtonEditor(new JCheckBox()));
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
