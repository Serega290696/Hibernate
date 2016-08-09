package com.cybervision.market.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class GenericDaoImpl<E, K extends Serializable> implements GenericDao<E, K> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class<? extends E> daoType;

    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        daoType = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public K save(E entity) {
        return (K) getCurrentSession().save(entity);
    }

    @Override
    public void update(E entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public E get(K id) {
        return (E) getCurrentSession().get(daoType, id);
    }

    @Override
    public void delete(E entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public List<E> getAll() {
        return getCurrentSession().createCriteria(daoType).list();
    }

    @Override
    public void removeAll() {
        Query query = getCurrentSession().createQuery("DELETE FROM " + daoType.getSimpleName());
        query.executeUpdate();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
