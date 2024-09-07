package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.ReviewDAO;
import com.carrental.carrental.entity.Review;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDAOImpl implements ReviewDAO {
    private EntityManager em;

    @Autowired
    public ReviewDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Review getReviewById(int id) {
        return em.find(Review.class, id);
    }
}
