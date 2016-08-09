package com.cybervision.market.dao.impl;

import com.cybervision.market.dao.GenericDao;
import com.cybervision.market.dao.GenericDaoImpl;
import com.cybervision.market.entity.Basket;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public class BasketDaoImpl  extends GenericDaoImpl<Basket, Long> implements GenericDao<Basket, Long> {

}
