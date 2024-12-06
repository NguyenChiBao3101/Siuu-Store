package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderDetail;
import com.siuuuuu.backend.entity.OrderHistory;
import com.siuuuuu.backend.entity.Profile;
import com.siuuuuu.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller("clientProfileController")
@RequestMapping("/profile")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProfileController {

    OrderService orderService;

    OrderHistoryService orderHistoryService;

    FeedbackService feedbackService;

    OrderDetailService orderDetailService;

    ProfileService profileService;

    @RequestMapping("")
    public String index(Model model) {
        Profile profile = profileService.getProfileCurrentUser();
        model.addAttribute("title", "Thông tin cá nhân");
        model.addAttribute("profile", profile);
        model.addAttribute("currentPage", "profile");
        return "profile/index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateProfile(@ModelAttribute Profile profile) {
        profileService.updateProfile(profile);
        return "redirect:/profile";
    }

    @RequestMapping(value = "/update-password", method = RequestMethod.POST)
    public String updatePassword(@RequestParam("password") String password, Model model) {
        profileService.updatePassword(password);
        return "redirect:/profile";
    }


    @RequestMapping("/purchase")
    public String purchase(@RequestParam(value = "status", required = false) OrderStatus status, Model model) {
        List<Order> orders;
        if (status != null) {
            orders = orderService.getOrdersByStatusAndUser(status);
        } else {
            orders = orderService.getOrdersForCurrentUser();
        }
        Profile profile = profileService.getProfileCurrentUser();
        model.addAttribute("title", "Lịch sử mua hàng");
        model.addAttribute("orders", orders);
        model.addAttribute("profile", profile);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("currentPage", "purchase");
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

    @GetMapping("/purchase/feedback/{id}")
    public String feedbackForm(@PathVariable("id") String id, Model model) {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        List<OrderHistory> orderHistories = orderHistoryService.getOrderHistoriesByOrderId(id);
        model.addAttribute("title", "Đánh giá sản phẩm");
        model.addAttribute("orderDetail", orderDetail);
        return "profile/feedback-form";
    }

    @PostMapping("/purchase/feedback/{id}")
    public String createFeedback(@PathVariable("id") String id, @RequestParam("comment") String comment, @RequestParam("rate") int rate, @RequestParam("images") MultipartFile[] images, Model model) {
        feedbackService.createFeedback(id, images, comment, rate);
        return "redirect:/profile/purchase";
    }
}
