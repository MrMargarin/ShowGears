package dao.dao_impl;

import dao.dao_interface.stus_dao;
import mai_n.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import tabs_gen.OrderlistTableEntity;
import tabs_gen.StippTableEntity;
import tabs_gen.StusTableEntity;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitrey on 07.09.2015.
 */
public class stus_daoImpl implements stus_dao {

    @Override
    public void addStus(StusTableEntity stus) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(stus);
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
    public void updateStus(Integer stus_id, StusTableEntity stus) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(stus);
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
    public StusTableEntity getStusById(Integer stus_id) throws SQLException {
        Session session = null;
        StusTableEntity stus = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            stus = (StusTableEntity) session.load(StusTableEntity.class, stus_id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'findById'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return stus;
    }

    @Override
    public Collection getAllStus() throws SQLException {
        Session session = null;
        List stuses = new ArrayList<StusTableEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            stuses = session.createCriteria(StusTableEntity.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'getAll'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return stuses;
    }

    @Override
    public void deleteStus(StusTableEntity stus) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(stus);
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
    public StusTableEntity getStusByStipp(StippTableEntity stipp) throws SQLException {
        return getStusById(stipp.getStusId());
    }

    @Override
    public StusTableEntity getStusByOrderListProdID(Integer orderListPrID) throws SQLException {
        return getStusById(orderListPrID);
    }
}
