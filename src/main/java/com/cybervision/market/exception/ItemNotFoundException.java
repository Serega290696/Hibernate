package com.cybervision.market.exception;

import com.cybervision.market.entity.Item;
import org.hibernate.ObjectNotFoundException;

public class ItemNotFoundException extends ObjectNotFoundException {

    public ItemNotFoundException(final Long identifier) {
        super(identifier, Item.class.getName());
        System.err.printf("Item with id %d doesn't found.", identifier);
    }

}
