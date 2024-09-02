package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.entity.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
