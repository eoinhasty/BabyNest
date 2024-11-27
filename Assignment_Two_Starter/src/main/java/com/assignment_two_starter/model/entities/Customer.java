package com.assignment_two_starter.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 * @author Alan.Ryan
 */
@Entity
@Table(name = "users")
@Data
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;

    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;

    @Basic(optional = false)
    @Column(name = "email")
    @NotBlank(message = "Email is required")
    private String email;

    @Basic(optional = false)
    @Column(name = "password")
    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    @JsonManagedReference("shoppingCartCustomerReference")
    private List<ShoppingCart> shoppingCartList;

    @OneToMany(mappedBy = "userId")
    @ToString.Exclude
    @JsonManagedReference("addressCustomerReference")
    private List<Address> addressesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    @JsonManagedReference("reviewCustomerReference")
    private List<Review> reviewList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    @JsonManagedReference("ordersCustomerReference")
    private List<Orders> ordersList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), //The foreign key column in the join table (user_roles) that refers to the Customer entity.
            inverseJoinColumns = @JoinColumn(name = "role_id")) //The foreign key column in the join table that refers to the Role entity.
    //I'm using a Set here so that roles are unique
    //This prevents duplicate entries for the same role on a user.
    private Set<Role> roles = new HashSet<>();
}
