package com.assignment_two_starter.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alan.Ryan
 */
@Entity
@Table(name = "cart_items")
@Data
public class CartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cart_item_id")
    private Integer cartItemId;

    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "added_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;

    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id")
    @ManyToOne(optional = false)
    @ToString.Exclude
    @JsonBackReference("cartItemCartReference")
    private ShoppingCart cart;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(optional = false)
    @ToString.Exclude
    private Product product;


}
