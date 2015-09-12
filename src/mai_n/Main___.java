package mai_n;

import Graphical_UI.Graphic_main;
import Graphical_UI.MainFrameProgram2;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;


public final class Main___ {

    //private Main___() {

    /*
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        Collection stus_objs = Factory.getInstance().get_stus_dao().getAllStus();
        Iterator it = stus_objs.iterator();
        while (it.hasNext())
        {
            StusTableEntity st = (StusTableEntity) it.next();
            System.out.println("STUS NAME: " + st.getProdName());
        }
    }//*/
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
       EventQueue.invokeLater(() -> {

           //MainFrameProgram2 frame = null;
           //Graphic_main frame = null;
           try {
               Graphic_main frame = new Graphic_main();
               //MainFrameProgram2 frame = new MainFrameProgram2();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
           } catch (SQLException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           } catch (InstantiationException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (UnsupportedLookAndFeelException e) {
               e.printStackTrace();
           }


       });
    }
}//*/
