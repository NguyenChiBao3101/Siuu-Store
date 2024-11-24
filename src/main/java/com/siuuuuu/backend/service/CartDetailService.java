package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.repository.CartDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartDetailService {

    CartDetailRepository cartDetailRepository;
    public List<CartDetail>  getCartDetailsByIds(List<String> cartDetailIds) {
        return cartDetailRepository.findAllById(cartDetailIds);
    }

    public void deleteCartDetails(List<String> cartDetailIds) {
        cartDetailRepository.deleteAllById(cartDetailIds);
    }

}
