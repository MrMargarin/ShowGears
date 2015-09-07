package dao.dao_interface;


import tabs_gen.OrderlistTableEntity;
import tabs_gen.StippTableEntity;
import tabs_gen.StusTableEntity;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public interface stus_dao {

    public void addStus(StusTableEntity stus) throws SQLException;
    public void updateStus(Integer stus_id, StusTableEntity stus) throws SQLException;
    public StusTableEntity getStusById(Integer stus_id) throws SQLException;
    public Collection getAllStus() throws SQLException;
    public void deleteStus(StusTableEntity stus) throws SQLException;
    public StusTableEntity getStusByStipp(StippTableEntity stipp) throws SQLException;
    public StusTableEntity getStusByOrderListProdID(Integer orderListProdID) throws SQLException;
}
