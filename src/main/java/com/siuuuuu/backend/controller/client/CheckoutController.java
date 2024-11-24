package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.service.CartService;
import com.siuuuuu.backend.service.OrderService;
import com.siuuuuu.backend.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                               @RequestParam("orderInfo") String orderInfo,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("address") String address,
                               @RequestParam("phone") String phone,
                               @RequestParam("paymentMethod") String paymentMethod,
                               @RequestParam("cartDetailIds") List<String> cartDetailIds,
                               HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String paymentUrl = vnPayService.createOrder(totalPrice, orderInfo, baseUrl + "/checkout");
        orderService.createOrder(name, email, address, phone, totalPrice, cartDetailIds, paymentMethod);
        return "redirect:" + paymentUrl;
    }

    @RequestMapping(value = "/vnpay-return", method = RequestMethod.GET)
    public String vnpayReturn(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        System.out.println("Order Info: " + orderInfo);
        System.out.println("Payment Time: " + paymentTime);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Total Price: " + totalPrice);
        System.out.println("Payment Status: " + paymentStatus);

        if (paymentStatus == 1) {
            model.addAttribute("message", "Payment successful!");
            return "checkout/success";
        } else if (paymentStatus == 0) {
            model.addAttribute("message", "Payment failed!");
            return "checkout/failure";
        } else {
            model.addAttribute("message", "Invalid payment signature!");
            return "checkout/failure";
        }
    }
}