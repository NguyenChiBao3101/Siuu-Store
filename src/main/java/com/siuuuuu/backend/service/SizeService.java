package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.Size;
import com.siuuuuu.backend.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> findAllSize() {
        return sizeRepository.findAll();
    }
}
