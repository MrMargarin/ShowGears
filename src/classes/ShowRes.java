
package classes;

import classes.DataConnection;
import classes.MainWindow;

public class ShowRes extends DataConnection {

    private MainWindow parent=null;
    
    public ShowRes(String p_user, String p_password, String p_database, MainWindow p_parent) {
        super(p_user, p_password, p_database);
        parent = p_parent;
    }
    
    @Override
    public void run()
    {
        parent.threadlock.lock();
        parent.acces_MTable.setModel(this.getTab());
        parent.threadlock.unlock();
        
    
    }
    
}
