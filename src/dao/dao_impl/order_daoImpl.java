package dao.dao_impl;
import dao.dao_interface.order_dao;
import mai_n.HibernateUtil;
import org.hibernate.Session;
import table_models.OrderTable;
import tabs_gen.OrderTableEntity;
import tabs_gen.OrderlistTableEntity;
import tabs_gen.StusTableEntity;
;import javax.swing.*;
import java.sql.SQLException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public class order_daoImpl implements order_dao {
    @Override
    public void addOrder(OrderTableEntity ord) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(ord);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка при вставке", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {

                session.close();
            }
        }
    }

    @Override
    public void updateOrder(Integer ord_id, OrderTableEntity ord) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(ord);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка при вставке", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public OrderTableEntity getOrderById(Integer ord_id) throws SQLException {
        Session session = null;
        OrderTableEntity order = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            order = (OrderTableEntity) session.load(OrderTableEntity.class, ord_id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'findById'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public Collection getAllOrder() throws SQLException {
        Session session = null;
        List orders = new ArrayList<OrderTableEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            orders = session.createCriteria(OrderTableEntity.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'getAll'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return orders;
    }

    @Override
    public void deleteOrder(OrderTableEntity order) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка при удалении", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public OrderTableEntity getOrderByOrderList(OrderlistTableEntity ordLst) throws SQLException {
        return getOrderById(ordLst.getOrderId());
    }

    @Override
    public Collection getOrdLsts() throws SQLException {
        return null;
    }


}
