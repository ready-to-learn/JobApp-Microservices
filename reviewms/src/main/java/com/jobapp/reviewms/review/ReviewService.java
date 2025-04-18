package com.jobapp.reviewms.review;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {


    List<Review> getReviews(Long companyId);
    boolean addReview(Long companyId, Review review);

    Review getReviewById(Long reviewId);

    boolean updateReview(Review review, Long reviewId);

    boolean deleteReview(Long reviewId);
}
