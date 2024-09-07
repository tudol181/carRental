package com.carrental.carrental.dao;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Review;

import java.time.LocalDate;
import java.util.List;

public interface CarDAO {
    void save(Car car);
    List<Review> findReviewByCarId(int id);
    Car findCarById(int id);
    List<Car> findCarsByUserId(int id);
    void updateCar(Car car);
    List<Car> findAllCars();
    boolean isAvailable(Car car, LocalDate pickupDate, LocalDate returnDate);
    List<Car> sortCarsByPrice(List<Car> cars, boolean ascending);
    void deleteCar(int id);
}
