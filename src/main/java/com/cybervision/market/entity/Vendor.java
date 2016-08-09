package com.cybervision.market.entity;


import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendor", schema = "market")
@NamedQueries(
        @NamedQuery(
                name = "getAllVendors",
                query = "from Vendor v"
        )
)
@NoArgsConstructor
@ToString(exclude = "supplies")
@EqualsAndHashCode(exclude = {"id"})
public class Vendor implements Serializable {

    private static final long serialVersionUID = 255020L;

    @Setter
    private Long id;

    @Setter
    private String title;

    @Setter
    private String address;

    @Setter
    private Set<Supply> supplies = new HashSet<Supply>();

    public Vendor(String title, String address) {
        this.title = title;
        this.address = address;
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
    public Set<Supply> getSupplies() {
        return supplies;
    }
}
