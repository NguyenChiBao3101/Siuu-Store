package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.response.OrderDetailResponse;
import com.siuuuuu.backend.entity.Order;
import com.siuuuuu.backend.entity.OrderDetail;
import com.siuuuuu.backend.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public OrderDetailResponse mapToDto(OrderDetail orderDetail) {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        String nameProduct = orderDetail.getProductVariant().getProduct().getName();
        orderDetailResponse.setProductName(nameProduct);
        orderDetailResponse.setPrice(orderDetail.getPrice());
        orderDetailResponse.setQuantity(orderDetail.getQuantity());
        return orderDetailResponse;
    }

    public List<OrderDetailResponse> mapToListDto(List<OrderDetail> list) {
        List<OrderDetailResponse> detailResponseList = new ArrayList<>();
        for(OrderDetail orderDetail : list) {
            OrderDetailResponse response = mapToDto(orderDetail);
            detailResponseList.add(response);
        }
        return detailResponseList;
    }
}
