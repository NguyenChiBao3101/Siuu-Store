package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.service.CartService;
import com.siuuuuu.backend.service.EmailService;
import com.siuuuuu.backend.service.OrderService;
import com.siuuuuu.backend.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CheckoutController {

    CartService cartService;

    VNPayService vnPayService;

    OrderService orderService;

    EmailService emailService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String checkout(@RequestParam("selectedItems") List<String> selectedItems, Model model) {
        List<CartDetail> cartDetails = new ArrayList<>();
        selectedItems.forEach(selectedItem -> {
            CartDetail cartDetail = cartService.getCartDetailById(selectedItem);
            cartDetails.add(cartDetail);
        });
        // Calculate total price
        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getProductVariant().getProduct().getPrice() * cartDetail.getQuantity();
        }
        model.addAttribute("title", "Thanh toán");
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "checkout/index";
    }

    @RequestMapping(value = "/vnpay", method = RequestMethod.GET)
    public String vnpayPayment(@RequestParam("totalPrice") int totalPrice,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("address") String address,
                               @RequestParam("phone") String phone,
                               @RequestParam("paymentMethod") String paymentMethod,
                               @RequestParam("cartDetailIds") List<String> cartDetailIds,
                               HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Order order = orderService.createOrder(name, email, address, phone, totalPrice, cartDetailIds, paymentMethod);
        String paymentUrl = vnPayService.createOrder(totalPrice, order.getId(), baseUrl + "/checkout");
        orderService.updatePaymentUrl(order.getId(), paymentUrl);
        return "redirect:" + paymentUrl;
    }

    @RequestMapping(value = "/vnpay-return", method = RequestMethod.GET)
    public String vnpayReturn(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderId = request.getParameter("vnp_OrderInfo");
        String transactionId = request.getParameter("vnp_TransactionNo");

        if (paymentStatus == 1) {
            model.addAttribute("title", "Đặt hàng thành công!");
            Order order = orderService.updateOrderWhenPaymentSuccess(orderId, transactionId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            String subject = "Thông báo đơn hàng #" + order.getId() + " của quý khách đã được tiếp nhận";
            emailService.sendOrderConfirmationEmail("phucnh203@gmail.com", subject, order);
            return "redirect:/checkout/success/" + orderId;
        } else if (paymentStatus == 0) {
            model.addAttribute("title", "Đặt hàng thất bại!");
            orderService.updateOrderWhenPaymentFailed(orderId);
            return "redirect:/checkout/failure/" + orderId;
        } else {
            model.addAttribute("title", "Đặt hàng thất bại!");
            orderService.updateOrderWhenPaymentFailed(orderId);
            return "redirect:/checkout/failure/" + orderId;
        }
    }

    @RequestMapping(value = "/success/{orderId}", method = RequestMethod.GET)
    public String orderSuccess(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("title", "Đặt hàng thành công!");
        return "checkout/success";
    }

    @RequestMapping(value = "/failure/{orderId}", method = RequestMethod.GET)
    public String orderFailure(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("title", "Đặt hàng thành công!");
        return "checkout/failure";
    }
}