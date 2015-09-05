package Graphical_UI;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import mai_n.*;
import table_models.*;

public class MainFrameProgram extends JFrame {

    private final String namField = "Name"; //название поля наименований товаров, по которому проводится поиск
    
    private MainSQLTable mainTableMod;
    private SubSQLTable subTableMod;
    
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
    private JButton connect, show, discon;
    private JLabel pass, login;
    private JSplitPane outputPane;
    private JPanel inputPane;
    private JScrollPane scPfSTUSTable, scPfSTIPPTable; //   "scPf" - scroll panel for
    
    private ParamList cats;
    //private String urlToDB = "jdbc:mysql://192.168.1.158/metlgears_db";
    private String urlToDB = "jdbc:mysql://localhost/metgears_db";
    //private String[] tables = {"drill", "tapndie", "another" }; //difrent tables
    private String[] tables = {"tabledrill", "tabletapndie", "another" }; //difrent tables
    private JButton showt;
    private JButton mkoder;
    
    public MainFrameProgram() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Categories = new JComboBox(tables); //
        
        Colums = new ArrayList();   //Colum names
        inputPane = new JPanel();
        filtPan = new JPanel();
        atrzPanel = new JPanel();
        workPanel = new JPanel();
        workPanel.setLayout(new FlowLayout());
        
        tabs = new JTabbedPane();
        //mPane = new JPanel(new GridLayout(1,2));
        //outputPane = new JPanel();
        //outputPane.setLayout(new BoxLayout(outputPane, BoxLayout.Y_AXIS));
        outputPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        //outputPane.setDividerLocation(0.6);
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
                connector = new MySQLConnector(urlToDB, user.getText(), password.getText());
                mainTableMod = new MainSQLTable(connector);
                subTableMod = new SubSQLTable(connector);
                
                atrzPanel.setVisible(false);
                workPanel.setVisible(true);
                search.requestFocus();
                
                //inputPane.remove(atrzPanel);
                //inputPane.add(workPanel);
                
                cats = new ParamList("CatName", Categories.getSelectedItem().toString(), connector);
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
            
            //if(outputPane.isAncestorOf(scPfSTUSTable)){outputPane.remove(scPfSTUSTable);}
            if(outputPane.isAncestorOf(scPfSTIPPTable)){outputPane.remove(scPfSTIPPTable);}
            //================Поле поиска===============================
            System.out.print("Search field is: ");
            //srch = "select * from " + Categories.getSelectedItem() + " where Name like \'%" + search.getText() + "%\'";
            srch = "select * from " + Categories.getSelectedItem() + " where "+namField+" like \'%" + search.getText() + "%\' and CatName like \'"+Kategorii.getSelectedItem()+"\'";
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
            ListSelectionModel selModel = STUSTable.getSelectionModel();
            selModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    try {
                        //if(outputPane.isAncestorOf(scPfSTIPPTable)){outputPane.remove(scPfSTIPPTable);}
                        int[] selectedRows = STUSTable.getSelectedRows();
                        //for(int i = 0; i<selectedRows.length; i++)
                        if(selectedRows.length!=0)
                        {
                            int selIndex = selectedRows[0];
                            TableModel model = STUSTable.getModel();
                            Object value = model.getValueAt(selIndex, 0);
                            subTableMod.fillTable(value.toString(), Categories.getSelectedItem().toString());
                            TableRowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(subTableMod.getSubTab());
                            STIPPTable = new JTable(subTableMod.getSubTab());
                            STIPPTable.setRowSorter(sorter2);
                            STIPPTable.setFillsViewportHeight(true);
                            scPfSTIPPTable = new JScrollPane(STIPPTable);
                            outputPane.setBottomComponent(scPfSTIPPTable);
                        }
                        double divider =(double) STUSTable.getRowCount()/((double) STUSTable.getRowCount()+(double) STIPPTable.getRowCount());
                        outputPane.setDividerLocation(0.5);
                        outputPane.revalidate();
                        
                    } catch (SQLException ex) {
                        
                        Logger.getLogger(MainFrameProgram.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }); 
            
            outputPane.setTopComponent(scPfSTUSTable); //панель с таблицей результатов товаров
            //scPfSTUSTable.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            //outputPane.setDividerLocation(0.7);
            outputPane.validate();
            
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
                outputPane.removeAll();
                outputPane.revalidate();
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

        Categories.addActionListener (new ActionListener () {
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
        });
        
        
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
        //workPanel.add(mkoder);
        
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
        this.getContentPane().add(new JLabel("2"), BorderLayout.EAST);
        this.getContentPane().add(outputPane, BorderLayout.CENTER);
    }
}
