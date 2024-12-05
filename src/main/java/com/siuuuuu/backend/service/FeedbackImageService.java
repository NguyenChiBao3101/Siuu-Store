package com.siuuuuu.backend.service;

import com.siuuuuu.backend.entity.Feedback;
import com.siuuuuu.backend.entity.FeedbackImage;
import com.siuuuuu.backend.repository.FeedbackImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackImageService {
    @Autowired
    private FeedbackImageRepository feedbackImageRepository;

    public void createFeedbackImage(Feedback feedback, String imageUrl) {
        FeedbackImage feedbackImage = new FeedbackImage();
        feedbackImage.setFeedback(feedback);
        feedbackImage.setImageUrl(imageUrl);

        feedbackImageRepository.save(feedbackImage);

    }
}
