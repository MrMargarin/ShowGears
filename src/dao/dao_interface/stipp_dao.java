package dao.dao_interface;

import tabs_gen.StippTableEntity;
import tabs_gen.StusTableEntity;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public interface stipp_dao {
    public void addStipp(StippTableEntity stipp) throws SQLException;
    public void updateStipp(Integer stipp_id, StippTableEntity stipp) throws SQLException;
    public StippTableEntity getStippById(Integer stipp_id) throws SQLException;
    public Collection getAllStipp() throws SQLException;
    public void deleteStipp(StippTableEntity stipp) throws SQLException;

    public Collection getStippsByStus(StusTableEntity stus) throws SQLException;

}
