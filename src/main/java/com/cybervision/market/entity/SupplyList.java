package com.cybervision.market.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "supply_list", schema = "market")
@AssociationOverrides({
        @AssociationOverride(name = "supList.supply",
                joinColumns = @JoinColumn(name = "supply_id")),
        @AssociationOverride(name = "supList.item",
                joinColumns = @JoinColumn(name = "item_id"))})
@NoArgsConstructor
public class SupplyList  implements Serializable{
    private static final long serialVersionUID = 255023L;

    private SupplyListId supList = new SupplyListId();

    @Setter
    private Integer quantity;

    public SupplyList(Item item, Supply supply, int quantity) {
        this.setItem(item);
        this.setSupply(supply);
        this.setQuantity(quantity);
    }

    @EmbeddedId
    public SupplyListId getSupList() {
        return supList;
    }

    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setSupList(SupplyListId supList) {
        this.supList = supList;
    }

    @Transient
    public Supply getSupply() {
        return getSupList().getSupply();
    }

    public void setSupply(Supply supply) {
        getSupList().setSupply(supply);
    }

    @Transient
    public Item getItem() {
        return getSupList().getItem();
    }

    public void setItem(Item item) {
        this.getSupList().setItem(item);
    }
}
