package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.*;
import com.siuuuuu.backend.repository.FeedbackRepository;
import com.siuuuuu.backend.repository.OrderDetailRepository;
import com.siuuuuu.backend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackImageService feedbackImageService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProductService productService;

    public List<Feedback> getFeedbacksByProduct(Product product) {
        return feedbackRepository.findAllByProductOrderByCreatedAtDesc(product);
    }

    public long countfeedbacksByProduct(String productId) {
        return feedbackRepository.countByProductId(productId);
    }

    public void createFeedback(String id, MultipartFile[] images, String comment, int rate) {
        OrderDetail orderDetail = orderDetailRepository.findOneById(id);
        Feedback feedback = new Feedback();
        feedback.setOrderDetail(orderDetail);
        feedback.setProduct(orderDetail.getProductVariant().getProduct());
        feedback.setProfile(profileRepository.findByAccount_Id(orderDetail.getOrder().getCustomer().getId()));
        feedback.setRate(rate);
        feedback.setComment(comment);

        System.out.print(feedback);

        Feedback feedbackCreated = feedbackRepository.save(feedback);

        productService.solvingRating(feedbackCreated);

        try {
            if (images != null && images.length > 0) {
                List<MultipartFile> validFiles = Arrays.stream(images)
                        .filter(Objects::nonNull)
                        .filter(file -> !file.isEmpty())
                        .collect(Collectors.toList());

                if (!validFiles.isEmpty()) {
                    List<String> imageUrls = cloudinaryService.uploadFiles(validFiles.toArray(new MultipartFile[0]))
                            .stream()
                            .map(map -> map.get("url").toString())
                            .collect(Collectors.toList());

                    imageUrls.forEach(imageUrl -> feedbackImageService.createFeedbackImage(feedbackCreated, imageUrl));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
