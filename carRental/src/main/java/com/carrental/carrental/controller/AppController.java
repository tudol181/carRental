package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.entity.UserDetail;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class AppController {
    private final CarService carService;
    private final UserService userService;

    @Autowired
    public AppController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "minSeats", required = false) Integer minSeats,
            @RequestParam(value = "maxSeats", required = false) Integer maxSeats,
            @RequestParam(value = "pickupDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
            @RequestParam(value = "returnDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(value = "sort", required = false) String sort,
            Model model) {

        // retrieve all cars
        List<Car> cars = carService.findAllCars();

        // filter cars based on search criteria
        if (search != null && !search.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        if (minPrice != null) {
            cars = cars.stream()
                    .filter(car -> car.getPrice().compareTo(minPrice) >= 0)
                    .toList();
        }

        if (maxPrice != null) {
            cars = cars.stream()
                    .filter(car -> car.getPrice().compareTo(maxPrice) <= 0)
                    .toList();
        }

        if (minSeats != null) {
            cars = cars.stream()
                    .filter(car -> car.getSeats() >= minSeats)
                    .toList();
        }

        if (maxSeats != null) {
            cars = cars.stream()
                    .filter(car -> car.getSeats() <= maxSeats)
                    .toList();
        }

        //pickup and return date
        if (pickupDate != null && returnDate != null) {
            cars = cars.stream()
                    .filter(car -> carService.isAvailable(car, pickupDate, returnDate))
                    .toList();
        }
        if (sort != null) {
            if (sort.equals("priceAsc")) {
                cars = carService.sortCarsByPrice(cars, true); // Sort ascending
            } else if (sort.equals("priceDesc")) {
                cars = carService.sortCarsByPrice(cars, false); // Sort descending
            }
        }
        // add filtered cars to the model
        model.addAttribute("cars", cars);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about-us";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userDetail", new UserDetail());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult userBindingResult, @Valid @ModelAttribute UserDetail userDetail, BindingResult userDetailBindingResult, @RequestParam("role") String roleName, Model model) {
        if (userBindingResult.hasErrors() || userDetailBindingResult.hasErrors()) {
            return "register"; // registration page if errors are found
        }
        if (userService.checkUsername(user.getUserName())) {
            model.addAttribute("errorMessage", "Username already exists. Please choose a different username.");
            return "register";  // return registration cause username already exists
        }

        user.setUserDetail(userDetail);
        user.setEnabled(true);
        user.setPassword("{noop}" + user.getPassword());
        userService.addRole(user, roleName);
        userService.saveUser(user);

        model.addAttribute("successMessage", "User registered successfully! Please log in.");
        return "login";
    }


}
