package com.contacts.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import com.contacts.dao.GenericDao;

/**
 * @author martin.mecera
 */
public class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID>{
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Defines class which will be used by default.
     */
    protected Class<T> persistentClass;

    public GenericDaoImpl() {}

    protected GenericDaoImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }
    
    @Override
    public void save(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        return (T)sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void remove(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public void refresh(T entity) {
        sessionFactory.getCurrentSession().refresh(entity);
    }

    @Override
    public void evict(T entity) {
        sessionFactory.getCurrentSession().evict(entity);
    }

    @Override
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return sessionFactory.getCurrentSession().createQuery("FROM " + persistentClass.getName()).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getByID(Serializable id) {
        return (T)sessionFactory.getCurrentSession().get(persistentClass, id);
    }
}
