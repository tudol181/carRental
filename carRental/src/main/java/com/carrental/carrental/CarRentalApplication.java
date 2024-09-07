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
                Car car = carService.findCarById(26);
                Review review = car.getReviews().getFirst();
                System.out.println(review);
//                carService.removeReview(car.getId(), review.getId());
//                reviewService.deleteReview(review);
//                System.out.println(car.getReviews().getFirst());
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

};
