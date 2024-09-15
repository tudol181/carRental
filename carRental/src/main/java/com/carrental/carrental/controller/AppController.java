package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.entity.UserDetail;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AppController(CarService carService, UserService userService, PasswordEncoder passwordEncoder) {
        this.carService = carService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
            @RequestParam(value = "carType", required = false) String carType,
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
                cars = carService.sortCarsByPrice(cars, true);
            } else if (sort.equals("priceDesc")) {
                cars = carService.sortCarsByPrice(cars, false);
            } else if (sort.equals("rentersAsc")) {
                cars = carService.sortCarsByRantings(cars, true);
            } else if (sort.equals("rentersDesc")) {
                cars = carService.sortCarsByRantings(cars, false);
            }
        }

        if (carType != null && !carType.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getType() != null && car.getType().equalsIgnoreCase(carType))
                    .toList();
        }

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
        //check constrains
        if (userBindingResult.hasErrors() || userDetailBindingResult.hasErrors()) {
            return "register";
        }
        //username already exists
        if (userService.checkUsername(user.getUserName())) {
            model.addAttribute("errorMessage", "Username already exists. Please choose a different username.");
            return "register";
        }

        user.setUserDetail(userDetail);
        user.setEnabled(true);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
//        user.setPassword("{noop}" + user.getPassword());//no encryption
//        userService.addRole(user, roleName);//set to admin if you want to add an admin
        userService.addRole(user, "ADMIN");//set to admin if you want to add an admin

        userService.saveUser(user);

        model.addAttribute("successMessage", "User registered successfully! Please log in.");
        return "login";
    }


}
