package com.assignment_two_starter.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Product name must not be blank")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    // @Max(value=?)  @Min(value=?)//if you know the range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "price")
    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be greater than or equal 0")
    private Double price;

    @Column(name = "stock_quantity")
    @NotNull(message = "Stock quantity must not be null")
    @Min(value = 0, message = "Stock quantity must be greater than or equal 0")
    private Integer stockQuantity;

    @Column(name = "feature_image")
    private String feature_image;

    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
    @JsonIgnoreProperties({"product"})
    private List<Review> reviewList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
    @JsonIgnore
    private List<CartItem> cartItemList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
//    @JsonBackReference("orderItemsProductReference")
    @JsonIgnore
    private List<OrderItems> orderItemsList;

    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne
    @ToString.Exclude
    private Category category;
}
