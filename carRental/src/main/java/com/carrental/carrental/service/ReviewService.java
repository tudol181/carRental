package com.carrental.carrental.service;

import com.carrental.carrental.dao.ReviewDAO;
import com.carrental.carrental.entity.Review;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewDAO reviewDAO;

    @Autowired
    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewByUserId(int id) {
        return reviewDAO.getReviewByUserId(id);
    }

    public List<Review> getReviewByCarId(int id) {
        return reviewDAO.getReviewByCarId(id);
    }

    @Transactional
    public void saveReview(Review review) {
        reviewDAO.saveReview(review);
    }

    @Transactional
    public void updateReview(Review review) {
        reviewDAO.updateReview(review);
    }

    @Transactional
    public void deleteReview(int id) {

        reviewDAO.deleteReview(id);
    }

    public Review getReviewById(int id) {
        return reviewDAO.getReviewById(id);
    }


}
