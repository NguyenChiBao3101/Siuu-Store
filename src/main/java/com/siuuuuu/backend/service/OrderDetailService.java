package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.OrderDetail;
import com.siuuuuu.backend.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    public OrderDetail getOrderDetail(String id) {
        return orderDetailRepository.findOneById(id);
    };

    public long countOrderDetailsByProduct(String productId) {
        return orderDetailRepository.countByProductId(productId);
    }
}
