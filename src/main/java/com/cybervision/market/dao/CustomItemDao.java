package com.cybervision.market.dao;

import com.cybervision.market.entity.Item;
import com.cybervision.market.entity.Vendor;

import java.util.List;

public interface CustomItemDao extends GenericDao<Item, Long> {

    void removeAll();

    List getItemsDistinctWithMoreThenOneSupplyWithMoreThenTwoQuantityInEachSupplyBy(Vendor vendor);
}
