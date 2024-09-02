package com.carrental.carrental.service;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {
    private final CarDAO carDAO;

    @Autowired
    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Transactional
    public void saveCar(Car car) {
        carDAO.save(car);
    }

    public Car findCarById(int id) {
        return carDAO.findCarById(id);
    }

    public List<Car> findCarsByUserId(int id) {
        return carDAO.findCarsByUserId(id);
    }

    public List<Review> findReviewByCarId(int id) {
        return carDAO.findReviewByCarId(id);
    }

    public List<Car> findAllCars() {
        return carDAO.findAllCars();
    }

    @Transactional
    public void updateCar(Car car) {
        carDAO.updateCar(car);
    }
}
