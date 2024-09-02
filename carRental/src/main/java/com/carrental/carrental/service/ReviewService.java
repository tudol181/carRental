package com.carrental.carrental.service;

import com.carrental.carrental.dao.ReviewDAO;
import com.carrental.carrental.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private ReviewDAO reviewDAO;

    @Autowired
    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public Review getReviewById(int id) {
        return reviewDAO.getReviewById(id);
    }
}
