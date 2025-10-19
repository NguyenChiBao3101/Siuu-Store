package com.siuuuuu.backend.entity;

import com.siuuuuu.backend.util.SlugUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

import com.siuuuuu.backend.constant.ProductStatus;

@Data
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductVariant> productVariants;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<ProductImageColour> productImageColours;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;

    @Column(name = "rate", columnDefinition = "INT DEFAULT 0")
    private Float rate;

    @Column(name = "rated_total" , columnDefinition = "INT DEFAULT 0")
    private int ratedTotal;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @PrePersist
    public void onPrePersist() {
        // Generate the slug before persisting
        this.slug = SlugUtil.toSlug(this.name);

        // Set the default status to ACTIVE if not set
        if (this.status == null) {
            this.status = ProductStatus.ACTIVE;
        }
    }

    // PreUpdate: Called before the entity is updated (on update)
    @PreUpdate
    public void onPreUpdate() {
        // Regenerate slug if the name has changed
        if (this.name != null) {
            this.slug = SlugUtil.toSlug(this.name);
        }
    }
}
