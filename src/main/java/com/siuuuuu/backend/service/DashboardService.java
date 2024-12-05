package com.siuuuuu.backend.service;

import com.siuuuuu.backend.constant.OrderStatus;
import com.siuuuuu.backend.dto.response.TopProductSold;
import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class DashboardService {
    OrderRepository orderRepository;

    AccountRepository accountRepository;

    public Map<String, Long> getCompletedOrdersCountByDate(String filter) {
        if (filter == null) {
            filter = "today";
        }

        LocalDateTime startDate = LocalDateTime.now();
        List<Object[]> results = new ArrayList<>();
        switch (filter) {
            case "today":
                // Start from the beginning of the day: 00:00:00
                startDate = startDate.truncatedTo(ChronoUnit.DAYS);
                results = orderRepository.countCompletedOrdersGroupedByDate(OrderStatus.COMPLETED, startDate);
                break;
            case "week":
                startDate = startDate.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
                results = orderRepository.countCompletedOrdersGroupedByDate(OrderStatus.COMPLETED, startDate);
                break;
            case "month":
                // Start from the first day of the month. Start from the beginning of the day: 00:00:00
                startDate = startDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                System.out.println(startDate);
                results = orderRepository.countCompletedOrdersGroupedByMonth(OrderStatus.COMPLETED, startDate);
                break;
            case "year":
                startDate = startDate.withDayOfYear(1);
                results = orderRepository.countCompletedOrdersGroupedByYear(OrderStatus.COMPLETED, startDate);
                break;

        }

        Map<String, Long> data = new HashMap<>();
        for (Object[] result : results) {
            data.put(result[0].toString(), (Long) result[1]);
        }
        System.out.println(data);
        return data;
    }

    public int getTotalProductsSold() {
        return orderRepository.getTotalProductsSold();
    }

    public int getTotalOrdersPending() {
        return orderRepository.getTotalOrdersPending();
    }

    public int getTotalCustomers() {
        return accountRepository.getTotalCustomer();
    }

    public List<TopProductSold> getTopProductSolds(String filter) {
        if (filter == null) {
            filter = "today";
        }

        LocalDateTime startDate = LocalDateTime.now();

        switch (filter) {
            case "today":
                startDate = startDate.truncatedTo(ChronoUnit.DAYS);
                break;
            case "week":
                startDate = startDate.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
                break;
            case "month":
                startDate = startDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                break;
            case "year":
                startDate = startDate.withDayOfYear(1);
                break;
        }

        List<Object[]> results = orderRepository.getTop5BestSellingProducts(startDate);
        List<TopProductSold> topProductSolds = new ArrayList<>();
        for (Object[] result : results) {
            Product product = (Product) result[0];
            Long total = (Long) result[1];
            topProductSolds.add(new TopProductSold(product, total));
        }
        return topProductSolds;
    }

    public Map<String, Double> getOrderStatusPercentages(String filter) {
        LocalDateTime startDate = LocalDateTime.now();
        if (filter == null) {
            filter = "today";
        }

        switch (filter) {
            case "today":
                startDate = startDate.truncatedTo(ChronoUnit.DAYS);
                break;
            case "week":
                startDate = startDate.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
                break;
            case "month":
                startDate = startDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                break;
            case "year":
                startDate = startDate.withDayOfYear(1);
                break;
        }

        List<Object[]> results = orderRepository.countOrdersGroupedByStatus(startDate);
        long totalOrders = orderRepository.countTotalOrders(startDate);
        Map<String, Double> data = new HashMap<>();
        for (Object[] result : results) {
            String status = result[0].toString();
            long count = (Long) result[1];
            double percentage = (count * 100.0) / totalOrders;
            data.put(status, percentage);
        }
        return data;
    }

}