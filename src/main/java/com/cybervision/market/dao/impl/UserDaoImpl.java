package com.cybervision.market.dao.impl;

import com.cybervision.market.dao.GenericDaoImpl;
import com.cybervision.market.dao.CustomUserDao;
import com.cybervision.market.entity.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements CustomUserDao {

    @Override
    public User getByEmail(String email) {
        if (email == null) {
            return null;
        }
        Criteria criteria = getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        return (User) criteria.uniqueResult();
    }

}
