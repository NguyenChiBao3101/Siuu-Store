package com.siuuuuu.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "product_image_colour")
@NoArgsConstructor
@ToString
public class ProductImageColour {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;


    @OneToMany(mappedBy = "productImageColour", fetch = FetchType.EAGER)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "productImageColour", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<ProductVariant> productVariants;

    public ProductImageColour(Product product) {
        this.product = product;
    }

    public String getThumbnail() {
        return productImages.get(0).getImageUrl();
    }
}
