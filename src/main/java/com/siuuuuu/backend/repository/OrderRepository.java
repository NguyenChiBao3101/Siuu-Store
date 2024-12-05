package com.siuuuuu.backend.repository;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByCustomerOrderByCreatedAtDesc(Account account);

    Order findOneById(String id);

    @Query("SELECT o FROM Order o WHERE o.id LIKE %:keyword%")
    List<Order> searchOrdersById(@Param("keyword") String keyword);

    // findByCustomerAndStatus
    List<Order> findByCustomerAndStatus(Account account, OrderStatus status);


    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    Page<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    Page<Order> findOrdersByStatus(@Param("status") OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    Page<Order> findOrdersByStatusAndDateRange(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT DATE(o.createdAt) as date, COUNT(o) as count FROM Order o WHERE o.status = :status AND o.createdAt >= :startDate GROUP BY DATE(o.createdAt)")
    List<Object[]> countCompletedOrdersGroupedByDate(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT MONTH(o.createdAt) as month, COUNT(o) as count FROM Order o WHERE o.status = :status AND o.createdAt >= :startDate GROUP BY MONTH(o.createdAt)")
    List<Object[]> countCompletedOrdersGroupedByMonth(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT YEAR(o.createdAt) as year, COUNT(o) as count FROM Order o WHERE o.status = :status AND o.createdAt >= :startDate GROUP BY YEAR(o.createdAt)")
    List<Object[]> countCompletedOrdersGroupedByYear(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(od.quantity) as count FROM OrderDetail od JOIN od.order o WHERE o.status = :status AND o.createdAt >= :startDate GROUP BY DATE(o.createdAt)")
    int countTotalProductsSoldGroupedByDate(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(od.quantity) as count FROM OrderDetail od JOIN od.order o WHERE o.status = :status AND o.createdAt >= :startDate GROUP BY MONTH(o.createdAt)")
    int countTotalProductsSoldGroupedByMonth(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(od.quantity) as count FROM OrderDetail od JOIN od.order o WHERE o.status = :status AND o.createdAt >= :startDate GROUP BY YEAR(o.createdAt)")
    int countTotalProductsSoldGroupedByYear(@Param("status") OrderStatus status, @Param("startDate") LocalDateTime startDate);

    // Get Total Product Sold
    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od JOIN od.order o WHERE o.status = 'COMPLETED'")
    int getTotalProductsSold();

    // Get total order is pending
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PENDING'")
    int getTotalOrdersPending();

    // Get top 5 best-selling products in today, week, month, year
    @Query("SELECT od.productVariant.product, SUM(od.quantity) as total FROM OrderDetail od JOIN od.order o WHERE o.status = 'COMPLETED' AND o.createdAt >= :startDate GROUP BY od.productVariant.product ORDER BY total DESC")
    List<Object[]> getTop5BestSellingProducts(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.createdAt >= :startDate GROUP BY o.status")
    List<Object[]> countOrdersGroupedByStatus(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :startDate")
    long countTotalOrders(@Param("startDate") LocalDateTime startDate);
}
