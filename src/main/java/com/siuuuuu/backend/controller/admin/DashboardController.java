package com.siuuuuu.backend.controller.admin;

import com.siuuuuu.backend.dto.response.TopProductSold;
import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class DashboardController {
    DashboardService dashboardService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String dashboard(@RequestParam(value = "filter", required = false) String filter, Model model) {
        Map<String, Long> completedOrdersData = dashboardService.getCompletedOrdersCountByDate(filter);
        int totalProductsSold = dashboardService.getTotalProductsSold();
        int totalOrdersPending = dashboardService.getTotalOrdersPending();
        int totalCustomers = dashboardService.getTotalCustomers();
        List<TopProductSold> topProductSolds = dashboardService.getTopProductSolds(filter);
        Map<String, Double> orderStatusPercentages = dashboardService.getOrderStatusPercentages(filter);

        System.out.println(orderStatusPercentages);

        model.addAttribute("title", "Dashboard");
        model.addAttribute("filter", filter);
        model.addAttribute("completedOrdersData", completedOrdersData);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalProductsSold", totalProductsSold);
        model.addAttribute("totalOrdersPending", totalOrdersPending);
        model.addAttribute("topProductSolds", topProductSolds);
        model.addAttribute("orderStatusPercentages", orderStatusPercentages);
        return "admin/index";
    }
}
