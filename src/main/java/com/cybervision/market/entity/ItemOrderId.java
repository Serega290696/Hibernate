package com.cybervision.market.entity;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@ToString(exclude = "item")
public class ItemOrderId implements Serializable {

    private static final long serialVersionUID = 255011L;

    @Setter
    private Item item;

    @Setter
    private Basket basket;

    @ManyToOne
    public Item getItem() {
        return item;
    }

    @ManyToOne
    public Basket getBasket() {
        return basket;
    }

}
