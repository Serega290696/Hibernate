package com.cybervision.market.dao.impl;

import com.cybervision.market.dao.GenericDao;
import com.cybervision.market.dao.GenericDaoImpl;
import com.cybervision.market.entity.Vendor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public class VendorDaoImpl extends GenericDaoImpl<Vendor, Long> implements GenericDao<Vendor, Long> {

}
