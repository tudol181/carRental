package com.carrental.carrental.dao;

import com.carrental.carrental.entity.Review;

import java.util.List;

public interface ReviewDAO {
    List<Review> getReviewByUserId(int id);
    List<Review> getReviewByCarId(int id);
    void saveReview(Review review);
    void updateReview(Review review);
    void deleteReview(int id);
    Review getReviewById(int id);

}
