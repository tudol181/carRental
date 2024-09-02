package com.carrental.carrental.dao;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Review;

import java.util.List;

public interface CarDAO {
    void save(Car car);
    List<Review> findReviewByCarId(int id);
    Car findCarById(int id);
    List<Car> findCarsByUserId(int id);
    void updateCar(Car car);
    List<Car> findAllCars();
}
