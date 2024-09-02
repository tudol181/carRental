package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Review;
import com.carrental.carrental.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CarImpl implements CarDAO {

    private EntityManager em;

    @Autowired
    public CarImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void save(Car car) {
        em.persist(car);
    }

    @Override
    public List<Review> findReviewByCarId(int id) {
        // create the query
        TypedQuery<Review> query = em.createQuery("from Review where Car.id =:data", Review.class);
        query.setParameter("data", id);

        // execute query
        return query.getResultList();
    }

    @Override
    public Car findCarById(int id) {
        return em.find(Car.class, id);
    }

    @Override
    public List<Car> findCarsByUserId(int id) {
        return em.find(User.class, id).getCars();
    }

    @Override
    public void updateCar(Car car) {
        em.merge(car);
    }

    @Override
    public List<Car> findAllCars() {
        TypedQuery<Car> query = em.createQuery("from Car", Car.class);
        return query.getResultList();
    }
}
