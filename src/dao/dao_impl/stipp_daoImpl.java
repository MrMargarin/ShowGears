package dao.dao_impl;

import dao.dao_interface.stipp_dao;
import mai_n.HibernateUtil;
import org.hibernate.Session;
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
public class stipp_daoImpl implements stipp_dao {
    @Override
    public void addStipp(StippTableEntity stipp) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(stipp);
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
    public void updateStipp(Integer stipp_id, StippTableEntity stipp) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(stipp);
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
    public StippTableEntity getStippById(Integer stipp_id) throws SQLException {
        Session session = null;
        StippTableEntity stipp = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            stipp = (StippTableEntity) session.load(StippTableEntity.class, stipp_id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'findById'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return stipp;
    }

    @Override
    public Collection getAllStipp() throws SQLException {
        Session session = null;
        List stippes = new ArrayList<StippTableEntity>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            stippes = session.createCriteria(StippTableEntity.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'getAll'", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return stippes;
    }

    @Override
    public void deleteStipp(StippTableEntity stipp) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(stipp);
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
    public List getStippsByStus(StusTableEntity stus) throws SQLException {
        return stus.getStipps();
    }
}
