package com.assignment_two_starter.model.entities;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Alan.Ryan
 */
@Entity
@Table(name = "users")
@Data
public class Customer implements Serializable, UserDetails {

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
    @JsonIgnoreProperties({"customer", "ordersList"})
    private List<Address> addressesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    @JsonIncludeProperties({"reviewId", "rating", "reviewText", "createdAt", "approved"})
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
    @JsonIgnoreProperties({"customers"})
    private Set<Role> roles = new HashSet<>();

    @Column(name = "locked", nullable = false)
    private boolean locked = false;

    // Override methods from UserDetails

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Handle expiration logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Handle credential expiration logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Handle enabled logic if needed
    }
}