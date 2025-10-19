package com.siuuuuu.backend.controller.api;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.dto.request.CreateOrderRequest;
import com.siuuuuu.backend.dto.response.OrderHistoryResponse;
import com.siuuuuu.backend.dto.response.OrderResponse;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.repository.OrderRepository;
import com.siuuuuu.backend.service.OrderApiService;
import com.siuuuuu.backend.service.OrderHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderApiController {

    private final OrderApiService orderApiService;
    private final OrderHistoryService orderHistoryService;
    private final OrderRepository orderRepository;
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> list = orderApiService.getAllOrders();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{email}/me")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getMine(@PathVariable String email) {
        List<OrderResponse> all = orderApiService.getAllOrdersAccount(email);
        return ResponseEntity.ok(all);
    }


    @GetMapping("/{email}/history/{id}")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<OrderHistoryResponse>> getHistory(@PathVariable String id, @PathVariable String email) {
        List<OrderHistory> histories = orderHistoryService.getOrderHistoriesByOrderId(id);
        return ResponseEntity.ok(orderHistoryService.mapToListDto(histories));
    }

    /** POST /api/orders */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        OrderResponse created = orderApiService.createOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    // STATUS TRANSITIONS

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> confirm(@PathVariable String id) {
        orderApiService.confirmOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    /** POST /api/orders/{id}/shipping */
    @PostMapping("/{id}/shipping")
    public ResponseEntity<OrderResponse> shipping(@PathVariable String id) {
        orderApiService.shippingOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    /** POST /api/orders/{id}/complete */
    @PostMapping("/{id}/complete")
    public ResponseEntity<OrderResponse> complete(@PathVariable String id) {
        orderApiService.completeOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    /** POST /api/orders/{id}/cancel */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable String id) {
        orderApiService.cancelOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

//     //PAYMENT
//
//    /** POST /api/orders/{id}/payment/success  body: {"transactionId":"..."} */
//    @PostMapping("/{id}/payment/success")
//    public ResponseEntity<OrderResponse> paymentSuccess(@PathVariable String id, @Valid @RequestBody PaymentSuccessRequest req) {
//        Order updated = orderApiService.updateOrderWhenPaymentSuccess(id, req.getTransactionId());
//        return ResponseEntity.ok(orderMapper.toResponse(updated));
//    }
//
//    /** POST /api/orders/{id}/payment/fail */
//    @PostMapping("/{id}/payment/fail")
//    public ResponseEntity<Void> paymentFail(@PathVariable String id) {
//        orderApiService.updateOrderWhenPaymentFailed(id);
//        return ResponseEntity.noContent().build();
//    }

}

