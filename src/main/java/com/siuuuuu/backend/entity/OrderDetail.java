package com.siuuuuu.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_detail")
@EntityListeners(AuditingEntityListener.class)
public class OrderDetail {
    /**
     * Unique identifier for the order detail.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * The order associated with this detail.
     */
    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    private Order order;

    /**
     * The product variant associated with this detail.
     */
    @ManyToOne
    @JoinColumn(name = "id_product_variant", referencedColumnName = "id")
    private ProductVariant productVariant;

    /**
     * The quantity of the product variant in the order.
     */
    @Column(name = "quantity", nullable = false)
    private int quantity;

    /**
     * The price of the product variant at the time of the order.
     */
    @Column(name = "price", nullable = false)
    private int price;

    /**
     * The total price of the product variant in the order.
     */
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    /**
     * The date and time the order detail was created.
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * The date and time the order detail was last updated.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}