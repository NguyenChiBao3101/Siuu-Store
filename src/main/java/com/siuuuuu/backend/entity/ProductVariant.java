package com.siuuuuu.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_variant")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "product_image_colour_id", nullable = false)
    @ToString.Exclude
    private ProductImageColour productImageColour;


    @ManyToOne()
    @JoinColumn(name = "size_id", nullable = false)
    @ToString.Exclude
    private Size size;

    @Column(name = "quantity", nullable = false)
    private int quantity = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
