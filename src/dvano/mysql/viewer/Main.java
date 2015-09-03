package dvano.mysql.viewer;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author dvano
 */
public final class Main {

    private Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            MySQLConnector.registerDriver();
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(SimpleProgram.class.getName()).log(Level.SEVERE, null, ex);
//
//            System.exit(1);
//        }

        EventQueue.invokeLater(() -> {
            try {
                MainFrameProgram frame = new MainFrameProgram();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
