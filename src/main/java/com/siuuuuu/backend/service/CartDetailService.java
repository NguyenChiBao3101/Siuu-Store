package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.response.CartDetailResponse;
import com.siuuuuu.backend.entity.CartDetail;
import com.siuuuuu.backend.repository.CartDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartDetailService {

    CartDetailRepository cartDetailRepository;
    ProductVariantService service;
    public List<CartDetail>  getCartDetailsByIds(List<String> cartDetailIds) {
        return cartDetailRepository.findAllById(cartDetailIds);
    }

    public void deleteCartDetails(List<String> cartDetailIds) {
        cartDetailRepository.deleteAllById(cartDetailIds);
    }

    public CartDetailResponse mapToDto(CartDetail cartDetail) {
        CartDetailResponse response = new CartDetailResponse();
        response.setId(cartDetail.getId());
        response.setProductVariantResponse(service.mapToDto(cartDetail.getProductVariant()));
        response.setQuantity(cartDetail.getQuantity());
        return response;
    }

    public List<CartDetailResponse> mapToListDto(List<CartDetail> cartDetails) {
        List<CartDetailResponse> responseList = new ArrayList<>();
        for(CartDetail cartDetail : cartDetails) {
            CartDetailResponse cartDetailResponse = mapToDto(cartDetail);
            responseList.add(cartDetailResponse);
        }
        return responseList;
    }

}
