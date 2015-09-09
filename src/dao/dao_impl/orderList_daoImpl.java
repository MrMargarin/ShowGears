package dao.dao_impl;

import dao.dao_interface.orderList_dao;
import tabs_gen.OrderlistTableEntity;
import tabs_gen.StusTableEntity;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public class orderList_daoImpl implements orderList_dao {
    @Override
    public void addOrdList(OrderlistTableEntity ordLst) throws SQLException {

    }

    @Override
    public void updateOrdLst(Integer ordLst_id, OrderlistTableEntity ordLst) throws SQLException {

    }

    @Override
    public OrderlistTableEntity getOrdLstById(Integer ordLst_id) throws SQLException {
        return null;
    }

    @Override
    public Collection getAllOrdLst() throws SQLException {
        return null;
    }

    @Override
    public void deleteOrdLst(OrderlistTableEntity ordLst) throws SQLException {

    }

    @Override
    public StusTableEntity getProd() throws SQLException {
        return null;
    }
}
