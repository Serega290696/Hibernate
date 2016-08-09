package com.cybervision.market.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<E, K> {

    K save(E entity);

    void update(E entity);

    E get(K id);

    void delete(E entity);

    List<E> getAll();

    void removeAll();

}
