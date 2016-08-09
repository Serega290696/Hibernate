package com.cybervision.market.entity;


import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "supply", schema = "market")
@NamedQueries(
        @NamedQuery(
                name = "getAllSupplies",
                query = "from Supply s"
        )
)
@NoArgsConstructor
@ToString(exclude = "supplyList")
@EqualsAndHashCode(exclude = {"id"})
public class Supply implements Serializable {

    private static final long serialVersionUID = 255021L;

    @Setter
    private Long id;

    @Setter
    private Timestamp supplyDate;

    @Setter
    private Vendor vendor;

    @Setter
    private Set<SupplyList> supplyList = new HashSet<>();

    public Supply(Vendor vendor, Timestamp supplyDate) {
        this.vendor = vendor;
        this.supplyDate = supplyDate;
    }

    @Id
    @Column(name = "supply_id", nullable = false, unique = true)
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Column(name = "supply_date")
    public Timestamp getSupplyDate() {
        return supplyDate;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "vendor_id")
    public Vendor getVendor() {
        return vendor;
    }

    @OneToMany(mappedBy = "supList.supply", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<SupplyList> getSupplyList() {
        return supplyList;
    }

    @Transient
    public Set<Item> getItems() {
        return supplyList.stream().map(a -> a.getSupList().getItem()).collect(Collectors.toSet());
    }
}
