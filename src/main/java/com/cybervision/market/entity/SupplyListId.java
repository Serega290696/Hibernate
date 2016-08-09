package com.cybervision.market.entity;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@ToString(exclude = "item")
public class SupplyListId implements Serializable {

    private static final long serialVersionUID = 255025L;
    @Setter
    private Item item;

    @Setter
    private Supply supply;

    @ManyToOne
    public Item getItem() {
        return item;
    }

    @ManyToOne
    public Supply getSupply() {
        return supply;
    }
}
