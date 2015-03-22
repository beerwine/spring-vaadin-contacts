package com.contacts.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO interface. Defines basic actions for all DAO classes.
 */
public interface GenericDao<T, ID extends Serializable> {

    public void save(T entity);

    public T merge(T entity);

    public void update(T entity);

    public void remove(T entity);

    public void refresh(T entity);

    public void evict(T entity);

    public void flush();

    public List<T> getAll();

    public T getByID(ID id);
}
