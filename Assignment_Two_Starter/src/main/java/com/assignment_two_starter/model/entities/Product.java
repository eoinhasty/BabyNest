package com.assignment_two_starter.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Alan.Ryan
 */
@Entity
@Table(name = "products")
@Data
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "product_id")
    private Integer productId;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    // @Max(value=?)  @Min(value=?)//if you know the range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "price")
    private Double price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "feature_image")
    private String feature_image;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
    private List<Review> reviewList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    @ToString.Exclude
    private List<CartItem> cartItemList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
    private List<OrderItems> orderItemsList;

    @JsonManagedReference
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne
    @ToString.Exclude
    private Category category;
}
