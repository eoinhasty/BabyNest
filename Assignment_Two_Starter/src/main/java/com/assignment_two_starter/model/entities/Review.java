package com.assignment_two_starter.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
@Table(name = "reviews")
@Data
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "review_id")
    private Integer reviewId;

    @Basic(optional = false)
    @Column(name = "rating")
    private int rating;

    @Lob
    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "approved")
    private Boolean approved;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    @ToString.Exclude
    @JsonIncludeProperties({"userId", "firstName", "lastName"})
    private Customer customer;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(optional = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"reviewList", "cartItemList", "orderItemList"})
    private Product product;
}
