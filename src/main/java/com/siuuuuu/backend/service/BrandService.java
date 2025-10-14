package com.siuuuuu.backend.service;

import com.siuuuuu.backend.dto.response.BrandResponseDto;
import com.siuuuuu.backend.entity.Brand;
import com.siuuuuu.backend.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandById(String id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if(brand == null) {
            throw new NoSuchElementException("Không tìm thấy thương hiệu");
        }
        return brand;
    }

    public BrandResponseDto mapToDto(Brand brand) {
        BrandResponseDto brandResponseDto = new BrandResponseDto();
        brandResponseDto.setName(brand.getName());
        return brandResponseDto;
    }

}
