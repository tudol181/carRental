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
    public CommandLineRunner commandLineRunner(UserService userService, CarService carService, ReviewService reviewService, EntityManager em) {
        return runner -> {
            try {
                User user = userService.getUserById(28);
                System.out.println(user.getRoles());
                user.setRoles(List.of(new Role("ROLE_ADMIN")));
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
    }

};
