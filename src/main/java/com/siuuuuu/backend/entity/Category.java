package com.siuuuuu.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status", columnDefinition = "BIT")
    private Boolean status;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Product> products;

}
