package dao.dao_interface;

import tabs_gen.OrderlistTableEntity;
import tabs_gen.StippTableEntity;
import tabs_gen.StusTableEntity;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public interface orderList_dao {
    public void addOrdList(OrderlistTableEntity ordLst) throws SQLException;
    public void updateOrdLst(Integer ordLst_id, OrderlistTableEntity ordLst) throws SQLException;
    public OrderlistTableEntity getOrdLstById(Integer ordLst_id) throws SQLException;
    public Collection getAllOrdLst() throws SQLException;
    public void deleteOrdLst(OrderlistTableEntity ordLst) throws SQLException;

    public StusTableEntity getProd() throws SQLException;
}
