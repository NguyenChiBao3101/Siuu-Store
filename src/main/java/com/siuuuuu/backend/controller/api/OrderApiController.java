package com.siuuuuu.backend.controller.api;

import com.siuuuuu.backend.dto.request.CreateOrderRequest;
import com.siuuuuu.backend.dto.response.OrderHistoryResponse;
import com.siuuuuu.backend.dto.response.OrderResponse;
import com.siuuuuu.backend.entity.OrderHistory;
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
    @GetMapping("/{email}/history")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<OrderHistoryResponse>> getHistoryByEmail(@PathVariable String email) {
        List<OrderHistory> histories = orderHistoryService.getOrderHistoriesByCustomerEmail(email);
        return ResponseEntity.ok(orderHistoryService.mapToListDto(histories));
    }

    @PostMapping("/{email}/create")
//    @PreAuthorize("#email == authentication.name")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable String email,@Valid @RequestBody CreateOrderRequest req) {
        OrderResponse created = orderApiService.createOrder(email, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    // STATUS TRANSITIONS

    @PostMapping("/{id}/confirm")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> confirm(@PathVariable String id) {
        orderApiService.confirmOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/shipping")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> shipping(@PathVariable String id) {
        orderApiService.shippingOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> complete(@PathVariable String id) {
        orderApiService.completeOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("#email == authentication.name or hasAnyAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> cancel(@PathVariable String id) {
        orderApiService.cancelOrder(id);
        OrderResponse response = orderApiService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

}

