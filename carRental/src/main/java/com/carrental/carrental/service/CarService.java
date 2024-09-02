package com.carrental.carrental.service;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    private final CarDAO carDAO;

    @Autowired
    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    public void saveCar(Car car) {
        carDAO.save(car);
    }
}
