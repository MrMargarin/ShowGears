package classes;

import table_models.MainSQLTable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MainWindow {
    public final String TAB_NAME = "_sverla_";
    
    private JPanel main_win;
    private MainSQLTable tab;
    private ArrayList<JLabel> Colums;
    private JPanel panel;
    private JPanel filtPan;
    private JTable table;
    private JTextField url;
    private JTextField user;
    private JPasswordField password;
    private JTextField sql;
    
    private JButton bu_connect;
    private JButton bu_show;
    private JButton bu_reserve;

    private DataConnection db;
    private Thread db_thread;
    private String pw;
    
    public JTable acces_MTable=this.table;
    public MainWindow self=this;
   

    /**
     * Holds the last return status set for testing the reason for a
     * soft method failure
     * */
    
    public Lock threadlock = new ReentrantLock();
    

    /**
     * Primary GUI
     */
    public MainWindow(){
        
        url = new JTextField("jdbc:mysql://192.168.1.158/sverlamg", 18);
        user = new JTextField("admin", 4);
        password = new JPasswordField("pool",7);
        sql = new JTextField("select * from _sverla_", 12);

        url.setToolTipText("URL");
        user.setToolTipText("USER");
        password.setToolTipText("PASSWORD");
        sql.setToolTipText("SQL");
        try{
            bu_connect.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent actionEvent) {
                     connectAction();
                 }
            });

            bu_show.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    showAction();
                }
            });
            }
        catch(IllegalThreadStateException ex) {
            System.err.println("Caught: "+ex.toString());
            System.err.println("Thread state: "+db_thread.getState());
            System.err.println("Ignoring command.");
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     */
    private void showAction()
    {
        char[] pw=password.getPassword();
        String str_pw = new String(pw);
        String us=user.getText();
        
        //String city_to_delete=tb_delete.getText();
        db=new ShowRes(us,str_pw,"sverlamg",self);
        db_thread=new Thread(db);
        db_thread.start();
    }

    
    private void connectAction()
    {
        String str_pw = new String(password.getPassword());
        String us=user.getText();
        db =new DataConnection(us,str_pw,"sverlamg");
        db_thread=new Thread(db);
        db_thread.start();
    }

   
    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().main_win);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
