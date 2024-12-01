package com.siuuuuu.backend.controller.client;

import com.siuuuuu.backend.entity.Product;
import com.siuuuuu.backend.entity.ProductImageColour;
import com.siuuuuu.backend.repository.ProductRepository;
import com.siuuuuu.backend.service.ProductImageColourService;
import com.siuuuuu.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageColourService productImageColourService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> productList = productService.findAllProduct();
        productList = productList.stream().filter(product -> !product.getProductVariants().isEmpty()).toList();
        model.addAttribute("productList", productList);
        model.addAttribute("title", "Siuu Store");
        return "index";
    }
}
