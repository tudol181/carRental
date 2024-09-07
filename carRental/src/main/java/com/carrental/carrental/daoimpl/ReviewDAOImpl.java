package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.ReviewDAO;
import com.carrental.carrental.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDAOImpl implements ReviewDAO {
    private final EntityManager em;

    @Autowired
    public ReviewDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Review> getReviewByUserId(int id) {
        TypedQuery<Review> query = em.createQuery("SELECT r FROM Review r WHERE r.user.id = :id", Review.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Review> getReviewByCarId(int id) {
        TypedQuery<Review> query = em.createQuery("SELECT r FROM Review r WHERE r.car.id = :id", Review.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public void saveReview(Review review) {
        em.persist(review);
    }

    @Override
    public void updateReview(Review review) {
        em.merge(review);
    }

    @Override
    public void deleteReview(int id) {
        Review review = em.find(Review.class, id);
        if (review == null) {
            throw new EntityNotFoundException("Review with id " + id + " not found");
        }
        em.remove(review);
    }

    @Override
    public Review getReviewById(int id) {
        return em.find(Review.class, id);
    }
}
