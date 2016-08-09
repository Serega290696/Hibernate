package com.cybervision.market.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "item", schema = "market")
@NamedQueries(
        @NamedQuery(
                name = "getAllItems",
                query = "from Item i"
        )
)
@NoArgsConstructor
@ToString(exclude = {"itemOrders", "supplyList"})
@EqualsAndHashCode(exclude = {"id", "cost", "description", "itemOrders", "supplyList"})
public class Item implements Serializable {

    private static final long serialVersionUID = 255003L;

    @Setter
    private Long id;

    @Setter
    private String title;

    @Setter
    private Double cost;

    @Setter
    private String description;

    @Setter
    private Set<ItemOrder> itemOrders = new HashSet<ItemOrder>();

    @Setter
    private Set<SupplyList> supplyList = new HashSet<>();

    public Item(String title, Double cost) {
        this.title = title;
        this.cost = cost;
    }

    @Id
    @Column(name = "item_id", unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    public Long getId() {
        return id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "cost")
    public Double getCost() {
        return cost;
    }

    @OneToMany(mappedBy = "pk.item", cascade = CascadeType.ALL)
    public Set<ItemOrder> getItemOrders() {
        return itemOrders;
    }

    @OneToMany(mappedBy = "supList.item", cascade = CascadeType.ALL)
    public Set<SupplyList> getSupplyList() {
        return supplyList;
    }

    public void addBasket(Basket basket, int quantity) {
        ItemOrder itemOrder = new ItemOrder(this, basket, quantity);
        this.itemOrders.add(itemOrder);
    }

    public void addBasket(Basket basket) {
        addBasket(basket, 1);
    }

}
