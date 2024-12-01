package com.siuuuuu.backend.controller.admin;

import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("title", "Đơn Hàng");
        model.addAttribute("orders", orderService.getAllOrders());
        System.out.println(orderService.getAllOrders());
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
