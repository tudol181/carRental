package com.carrental.carrental.service;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Review;
import com.carrental.carrental.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public boolean isAvailable(Car car, LocalDate pickupDate, LocalDate returnDate) {
        return carDAO.isAvailable(car, pickupDate, returnDate);
    }

    public List<Car> sortCarsByPrice(List<Car> cars, boolean ascending) {
        return carDAO.sortCarsByPrice(cars, ascending);
    }

    @Transactional
    public void deleteCar(int id){
        carDAO.deleteCar(id);
    }

    public List<User> findRentersByCarId(int id) {
        return carDAO.findRentersByCarId(id);
    }

    public void removeReview(int carId, int reviewId) {
        carDAO.removeReview(carId, reviewId);
    }

    public List<Car> findCarsByType(String type) {
        return carDAO.findCarsByType(type);
    }
}
