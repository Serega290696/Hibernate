package com.cybervision.market.dao;

import com.cybervision.market.entity.User;

public interface CustomUserDao extends GenericDao<User, Long> {

    User getByEmail(String email);

}
