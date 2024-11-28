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
        List<String> imageUrls = new ArrayList<>();
        for (Product product : productList) {
            imageUrls.add(productService.getProductThumbnail(product.getProductImageColours().get(0)));
        }

//  Take image for example
        List<ProductImageColour> imageColours = productImageColourService.findAllProductImageColours();
        List<String> imageColourUrls = new ArrayList<>();
        for (ProductImageColour imageColour : imageColours) {
            imageColourUrls.add(imageColour.getProductImages().get(0).getImageUrl());
        }
        model.addAttribute("imageColourUrls", imageColourUrls);
        System.out.println(imageUrls);



        model.addAttribute("imageUrls", imageUrls);
        model.addAttribute("title", "Siuu Store");
        return "index";
    }
}
