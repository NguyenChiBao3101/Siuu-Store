package com.siuuuuu.backend.entity;

import lombok.*;
import jakarta.persistence.*;
import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.constant.PaymentMethod;
import com.siuuuuu.backend.constant.PaymentStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@ToString(exclude = "orderDetails")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    /**
     * Unique identifier for the order.
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * The customer who placed the order.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Account customer;

    /**
     * The user that confirmed the order.
     */
    @ManyToOne
    @JoinColumn(name = "confirmed_by", referencedColumnName = "id")
    private Account confirmedBy;

    /**
     * The total price of the order.
     */
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    /**
     * The shipping fee for the order.
     */
    @Column(name = "shipping_fee", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int shippingFee;

    /**
     * The discount applied to the order.
     */
    @Column(name = "discount", nullable = false, columnDefinition = "INT DEFAULT 0")
    private double discount;

    /**
     * The current status of the order.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    /**
     * The payment method used for the order.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    /**
     * The payment status of the order.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    /**
     * The payment date of the order if using vnpay.
     */
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    /**
     * The transaction ID for VNPay.
     */
    @Column(name = "vnpay_transaction_no")
    private String vnpayTransactionNo;

    /**
     * The Payment URl.
     */
    @Column(name = "payment_url", length = 1000)
    private String paymentUrl;

    /**
     * The shipping address for the order.
     */
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    /**
     * The shipping phone number for the order.
     */
    @Column(name = "shipping_phone", nullable = false)
    private String shippingPhone;

    /**
     * The email of the person receiving the order.
     */
    @Column(name = "shipping_email", nullable = false)
    private String shippingEmail;

    /**
     * The name of the person receiving the order.
     */
    @Column(name = "shipping_name", nullable = false)
    private String shippingName;

    /**
     * Additional notes for the shipping.
     */
    @Column(name = "shipping_note")
    private String shippingNote;

    /**
     * The date when the order was confirmed.
     */
    @Column(name = "confirmation_date")
    private LocalDateTime confirmationDate;

    /**
     * The date when the order will be received.
     */
    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    /**
     * The date when the order was delivered to the shipping company.
     */
    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    /**
     * The date when the order was completed.
     */
    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    /**
     * The date when the order was canceled.
     */
    @Column(name = "canceled_date")
    private LocalDateTime canceledDate;

    /**
     * Reason for canceling the order.
     */
    @Column(name = "cancel_reason")
    private String cancelReason;

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

    /**
     * The list of items in the order.
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails;

    /**
     * Sets the default status of the order to TAO_HOA_DON before persisting.
     * Sets the default payment status of the order to CHUA_THANH_TOAN before persisting.
     */
    @PrePersist
    public void onPrePersist() {
        this.id = generateOrderId();

        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.UNPAID;
        }
    }

    /**
     * Generate Order ID.
     */
    private String generateOrderId() {
        return "DH" + String.format("%09d", System.currentTimeMillis() % 1_000_000_000);
    }
}