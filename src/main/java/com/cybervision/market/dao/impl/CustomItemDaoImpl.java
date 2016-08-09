package com.cybervision.market.dao.impl;

import com.cybervision.market.dao.GenericDaoImpl;
import com.cybervision.market.dao.CustomItemDao;
import com.cybervision.market.entity.Item;
import com.cybervision.market.entity.Vendor;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public class CustomItemDaoImpl extends GenericDaoImpl<Item, Long> implements CustomItemDao {

    @Override
    public void removeAll() {
        getCurrentSession().createQuery("DELETE FROM ItemOrder").executeUpdate();
        getCurrentSession().createQuery("DELETE FROM SupplyList").executeUpdate();
        getCurrentSession().createQuery("DELETE FROM Item").executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getItemsDistinctWithMoreThenOneSupplyWithMoreThenTwoQuantityInEachSupplyBy(Vendor vendor) {
//        DetachedCriteria innerQuery = DetachedCriteria.forClass(SupplyList.class, "i2").
//                setProjection(Projections.rowCount()).
//                add(Restrictions.eqProperty("item_id", "i1.item_id")).
//                add(Restrictions.ge("quantity", 100));
//        return getCurrentSession().createCriteria(Item.class, "i1").
//                add(Subqueries.ge(2, innerQuery)).list();

        //language=MySQL
        String query = "SELECT i1.item_id, i1.cost, i1.title, i1.description FROM item i1\n" +
                "WHERE 2 <=\n" +
                "(\n" +
                "\tSELECT count(*)\n" +
                "\tFROM item i2, supply_list\n" +
                "\tWHERE supply_list.item_id = i2.item_id\n" +
                "\tAND i1.item_id = i2.item_id\n" +
                "\tAND supply_list.quantity >= 100\n" +
                ")";
        String query2 = "\n" +
                "SELECT *\n" +
                "FROM item\n" +
                "WHERE 2 <=\n" +
                "(\n" +
                "\tSELECT count(*)\n" +
                "\tFROM item i2, supply_list\n" +
                "\tWHERE supply_list.item_id = i2.item_id\n" +
                "\tAND item.item_id = i2.item_id\n" +
                "\tAND supply_list.quantity >= 100\n" +
                ")";
        List<Item> list = getCurrentSession().createSQLQuery(query2).setResultTransformer(Transformers.aliasToBean(Item.class)).list();
        list.stream().forEach(i -> System.out.println("Item: " + i));
        return list;
    }

}
