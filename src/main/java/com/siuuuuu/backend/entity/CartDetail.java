package com.siuuuuu.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "cart_detail")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_cart", referencedColumnName = "id")
    @ToString.Exclude
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "id_product_variant", referencedColumnName = "id")
    @ToString.Exclude
    private ProductVariant productVariant;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
