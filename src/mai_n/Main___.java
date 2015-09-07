package mai_n;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import tabs_gen.StusTableEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

//import mai_n.HibernateUtil;

public final class Main___ {

    private Main___() {
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        Collection stus_objs = Factory.getInstance().get_stus_dao().getAllStus();
        Iterator it = stus_objs.iterator();
        while (it.hasNext())
        {
            StusTableEntity st = (StusTableEntity) it.next();
            System.out.println("STUS NAME: " + st.getProdName());
        }
    }
       /*EventQueue.invokeLater(() -> {

            MainFrameProgram frame = null;
            try {
                frame = new MainFrameProgram();
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


        });*/
    //}
}
