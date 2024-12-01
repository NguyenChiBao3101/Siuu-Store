package com.siuuuuu.backend.entity;

import com.siuuuuu.backend.constant.OrderStatus;
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
@ToString(exclude = "order")
@Table(name = "order_history")
@EntityListeners(AuditingEntityListener.class)
public class OrderHistory {
    /**
     * Unique identifier for the order history.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * The order associated with this history.
     */
    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id")
    private Order order;

    /**
     * The status of the order at the time of the history.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
    /**
     * The employee who recorded the history.
     */
    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id")
    private Account actionBy;

    /**
     * The date when the order was created.
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * The date when the order was last updated.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
