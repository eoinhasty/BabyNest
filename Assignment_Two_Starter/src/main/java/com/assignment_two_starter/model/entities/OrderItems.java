package com.assignment_two_starter.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Alan.Ryan
 */
@Entity
@Table(name = "order_items")
@Data
public class OrderItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;

    // @Max(value=?)  @Min(value=?)//if you know the range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Basic(optional = false)
    @Column(name = "total_price")
    private Double totalPrice;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @ManyToOne(optional = false)
    @ToString.Exclude
    @JsonBackReference("orderItemsReference")
    private Orders order;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(optional = false)
    @ToString.Exclude
//    @JsonManagedReference("orderItemsProductReference")
    @JsonIgnoreProperties({"reviewList", "cartItemList", "orderItemList"})
    private Product product;
}
