package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.service.OrderHistoryService;
import com.siuuuuu.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("clientProfileController")
@RequestMapping("/profile")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProfileController {

    OrderService orderService;

    OrderHistoryService orderHistoryService;

    @RequestMapping("")
    public String index() {
        return "profile/index";
    }

    @RequestMapping("/purchase")
    public String purchase(Model model) {
        List<Order> orders = orderService.getOrdersForCurrentUser();
        model.addAttribute("title", "Lịch sử mua hàng");
        model.addAttribute("orders", orders);
        return "profile/purchase";
    }

    @RequestMapping("/purchase/order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") String id) {
        orderService.cancelOrder(id);
        return "redirect:/profile/purchase";
    }

    @RequestMapping("/purchase/detail/{id}")
    public String detailOrder(@PathVariable("id") String id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderHistory> orderHistories = orderHistoryService.getOrderHistoriesByOrderId(id);
        model.addAttribute("title", "Chi tiết đơn hàng");
        model.addAttribute("order", order);
        model.addAttribute("orderHistories", orderHistories);
        return "profile/order-detail";
    }
}
