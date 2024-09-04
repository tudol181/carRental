package com.carrental.carrental;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.*;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.ReviewService;
import com.carrental.carrental.service.UserService;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userService, CarService carService, ReviewService reviewService) {
        return runner -> {
            try {
                User user = userService.getUserById(24);
                Car car = carService.findCarById(4);
                car.setPhotoUrl("/photos/composture.jpg");
                carService.updateCar(car);
//                userService.addCar(user, car);
                // Save the user with the new role
                System.out.println(car);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

};
