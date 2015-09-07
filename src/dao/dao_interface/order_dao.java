package dao.dao_interface;

import table_models.OrderTable;
import tabs_gen.OrderTableEntity;
import tabs_gen.OrderlistTableEntity;
import tabs_gen.StippTableEntity;
import tabs_gen.StusTableEntity;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public interface order_dao {
    public void addOrder(OrderTableEntity ord) throws SQLException;
    public void updateOrder(Integer ord_id, OrderTableEntity ord) throws SQLException;
    public OrderTableEntity getOrderById(Integer ord_id) throws SQLException;
    public Collection getAllOrder() throws SQLException;
    public void deleteOrder(OrderTableEntity order) throws SQLException;
    public OrderTableEntity getOrderByOrderList(OrderlistTableEntity ordLst) throws SQLException;

    public Collection getOrdLsts() throws SQLException;
}
