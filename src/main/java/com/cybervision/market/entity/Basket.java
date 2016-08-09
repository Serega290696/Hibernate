package com.cybervision.market.entity;


import lombok.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "basket", schema = "market")
@NamedQueries({
        @NamedQuery(
                name = "getAllBaskets",
                query = "from Basket b"
        )
})
@NoArgsConstructor
@ToString(exclude = {"itemOrders"})
@EqualsAndHashCode(exclude = {"id", "dateCreated", "itemOrders", "deleted", "bought"})
public class Basket implements Serializable {

    private static final long serialVersionUID = 255002L;

    @Setter
    private Long id;

    @Setter
    private Timestamp dateCreated;

    @Setter
    private String title;

    @Setter
    private User user;

    @Setter
    private Set<ItemOrder> itemOrders = new HashSet<>();


    @Setter
    private boolean deleted;

    @Setter
    private boolean bought;

    public Basket(String title, User user, Timestamp dateCreated) {
        this.title = title;
        this.user = user;
        user.addBasket(this);
        this.dateCreated = dateCreated;
    }

    public Double calculateSummaryCost() {
        return getItemOrders().stream().reduce(
                0d,
                (sum, item) ->
                        sum + item.getQuantity() * item.getPk().getItem().getCost(),
                (sum1, sum2) -> sum1 + sum2
        );
    }

    @Id
    @Column(name = "basket_id", unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    public Long getId() {
        return id;
    }

    @Column(name = "date_created", nullable = false)
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @OneToMany(mappedBy = "pk.basket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<ItemOrder> getItemOrders() {
        return itemOrders;
    }

    @Column(name = "deleted")
    public boolean isDeleted() {
        return deleted;
    }

    @Column(name = "bought")
    public boolean isBought() {
        return bought;
    }

    @Transient
    public Set<Item> getItems() {
        return itemOrders.stream().map(a -> a.getPk().getItem()).collect(Collectors.toSet());
    }

}
