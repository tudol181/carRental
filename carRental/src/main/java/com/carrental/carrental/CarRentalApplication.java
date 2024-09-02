package com.carrental.carrental;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.entity.UserDetail;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserDAO userDAO, CarDAO carDAO) {
        return args -> {
            try {
                User user = userDAO.getUserById(1);
                UserDetail userDetail = new UserDetail("0726482997", "user@example.com", "CityName", "CountryName");
                user.setUserDetail(userDetail);
                System.out.println(user);
            } catch (Exception e) {
                e.printStackTrace();  // Print the stack trace for debugging
            }

        };
    }

};
