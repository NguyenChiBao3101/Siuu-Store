package com.siuuuuu.backend.controller.admin;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;

    @RequestMapping("")
    public String index(Model model, @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(value = "startDate", required = false) String startDateStr,
                        @RequestParam(value = "endDate", required = false) String endDateStr,
                        @RequestParam(value = "status", required = false) OrderStatus status) {
        Page<Order> orders;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (startDateStr != null && !startDateStr.isEmpty()) {
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate;
            if (endDateStr == null || endDateStr.isEmpty()) {
                endDate = LocalDate.now();
                endDateStr = endDate.format(formatter);
            } else {
                endDate = LocalDate.parse(endDateStr, formatter);
            }
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            if (status != null) {
                orders = orderService.findOrdersByStatusAndDateRange(status, startDateTime, endDateTime, page, size);
            } else {
                orders = orderService.findOrdersByDateRange(startDateTime, endDateTime, page, size);
            }
            model.addAttribute("startDate", startDateStr);
            model.addAttribute("endDate", endDateStr);
        } else {
            if (status != null) {
                orders = orderService.findOrdersByStatus(status, page, size);
            } else {
                orders = orderService.getOrders(page, size);
            }
        }
        model.addAttribute("title", "Đơn Hàng");
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("totalItems", orders.getTotalElements());
        model.addAttribute("status", status != null ? status.toString() : "");
        return "admin/order/index";
    }

    @RequestMapping("/search")
    public String searchOrdersById(@RequestParam("orderId") String orderId, Model model) {
        List<Order> orders = orderService.searchOrdersById(orderId);
        model.addAttribute("title", "Kết Quả Tìm Kiếm");
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", 1);
        model.addAttribute("size", orders.size());
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", orders.size());
        model.addAttribute("orderId", orderId);
        return "admin/order/index";
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderHistory> sortedHistories = order.getOrderHistories()
                .stream()
                .sorted(Comparator.comparing(OrderHistory::getCreatedAt)) // For ascending
                .toList();
        order.setOrderHistories(sortedHistories);
        model.addAttribute("title", "Chi Tiết Đơn Hàng");
        model.addAttribute("order", order);
        return "admin/order/detail";
    }

    @RequestMapping(value = "confirm/{id}", method = RequestMethod.POST)
    public String confirmOrder(@PathVariable("id") String id) {
        orderService.confirmOrder(id);
        return "redirect:/admin/order/detail/" + id;
    }

    @RequestMapping(value = "shipping/{id}", method = RequestMethod.POST)
    public String shippingOrder(@PathVariable("id") String id) {
        orderService.shippingOrder(id);
        return "redirect:/admin/order/detail/" + id;
    }

    @RequestMapping(value = "complete/{id}", method = RequestMethod.POST)
    public String completeOrder(@PathVariable("id") String id) {
        orderService.completeOrder(id);
        return "redirect:/admin/order/detail/" + id;
    }

    @RequestMapping(value = "cancel/{id}", method = RequestMethod.POST)
    public String cancelOrder(@PathVariable("id") String id) {
        orderService.cancelOrder(id);
        return "redirect:/admin/order/detail/" + id;
    }
}
