package com.cybervision.market.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "item_order", schema = "market")
@AssociationOverrides({
        @AssociationOverride(name = "pk.basket",
                joinColumns = @JoinColumn(name = "basket_id")),
        @AssociationOverride(name = "pk.item",
                joinColumns = @JoinColumn(name = "item_id"))})
@NoArgsConstructor
@ToString
public class ItemOrder implements Serializable {

    private static final long serialVersionUID = 255010L;

    private ItemOrderId pk = new ItemOrderId();

    @Setter
    private Integer quantity;

    public ItemOrder(Item item, Basket basket, int quantity) {
        pk.setBasket(basket);
        pk.setItem(item);
        this.quantity = quantity;
    }

    @EmbeddedId
    public ItemOrderId getPk() {
        return pk;
    }

    public void setPk(ItemOrderId pk) {
        this.pk = pk;
    }

    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @Transient
    public Basket getBasket() {
        return getPk().getBasket();
    }

    public void setBasket(Basket basket) {
        getPk().setBasket(basket);
    }

    @Transient
    public Item getItem() {
        return getPk().getItem();
    }

    public void setItem(Item item) {
        this.getPk().setItem(item);
    }
}
