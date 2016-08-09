package com.cybervision.market.dao.impl;

import com.cybervision.market.dao.GenericDao;
import com.cybervision.market.dao.GenericDaoImpl;
import com.cybervision.market.entity.Supply;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public class SupplyDaoImpl extends GenericDaoImpl<Supply, Long> implements GenericDao<Supply, Long> {
    // todo. Here is empty. Is it normal ?
}
