package com.cybervision.market.entity;

import com.cybervision.market.utils.Encryption;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "market")
@NamedQueries(
        @NamedQuery(
                name = "getAllUsers",
                query = "from User u"
        )
)
@NoArgsConstructor
@ToString(exclude = "baskets")
@EqualsAndHashCode(exclude = {"id", "password", "availableMoney", "baskets"})
public class User implements Serializable {

    private static final long serialVersionUID = 255001L;

    @Setter
    private Long id;

    @Setter
    private String email;

    private String password;

    @Setter
    private Double availableMoney;

    @Setter
    private Set<Basket> baskets = new HashSet<Basket>();

    public User(String email, String password, Double availableMoney) {
        this.email = email;
        this.password = Encryption.encryptUserPasswordBCrypt(password);
        this.availableMoney = availableMoney;
    }

    public void addBasket(Basket basket) {
        baskets.add(basket);
    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @Column(name = "available_money")
    public Double getAvailableMoney() {
        return availableMoney;
    }

    @OneToMany(mappedBy = "user")
    public Set<Basket> getBaskets() {
        return baskets;
    }

    public void setPassword(String password) {
        this.password = Encryption.encryptUserPasswordBCrypt(password);
    }
}
