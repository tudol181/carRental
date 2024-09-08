package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Rental;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.RentalService;
import com.carrental.carrental.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CarService carService;
    private final UserService userService;
    private final RentalService rentalService;

    @Autowired
    public AdminController(CarService carService, UserService userService, RentalService rentalService) {
        this.carService = carService;
        this.userService = userService;
        this.rentalService = rentalService;
    }

    @GetMapping("")
    public String showAdmin(Model model) {
        List<User> users = userService.getAllUsers();
        List<Car> cars = carService.findAllCars();

        model.addAttribute("users", users);
        model.addAttribute("cars", cars);
        return "admin";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        User existingUser = userService.getUserById(user.getId());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = "{noop}" + user.getPassword();
            user.setPassword(encodedPassword);
        } else {
            // Retain the old password
            user.setPassword(existingUser.getPassword());
        }
        // Retain old values if the new values are empty
        if (user.getUserDetail().getCountry() == null || user.getUserDetail().getCountry().isEmpty()) {
            user.getUserDetail().setCountry(existingUser.getUserDetail().getCountry());
        }

        if (user.getUserDetail().getCity() == null || user.getUserDetail().getCity().isEmpty()) {
            user.getUserDetail().setCity(existingUser.getUserDetail().getCity());
        }

        user.setEnabled(true);
        user.setRoles(userService.getUserById(existingUser.getId()).getRoles());
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam("id") int id) {
        carService.deleteCar(id);
        return "redirect:/admin";
    }

    @GetMapping("/showUserFormForUpdate")
    public String showUserFormForUpdateGET(@RequestParam("id") int id,Model model) {


        // get the employee from the service
        User user = userService.getUserById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);

        // send over to our form
        return "admin-update-form";
    }


    @PostMapping("/showUserFormForUpdate")
    public String showUserFormForUpdate(@RequestParam("id") int id,
                                    Model theModel) {

        // get the employee from the service
        User user = userService.getUserById(id);

        // set employee as a model attribute to pre-populate the form
        theModel.addAttribute("user", user);

        // send over to our form
        return "admin-update-form";
    }

    // car section

    @GetMapping("/showCarFormForUpdate")
    public String showCarFormForUpdateGET(@RequestParam("id") int id,Model model) {

        // get the car
        Car car = carService.findCarById(id);
        // prepopulate with car info
        model.addAttribute("car", car);
        model.addAttribute("carTypes",List.of("Small car", "SUV", "Break", "Sport", "Roadster", "Van"));
        // send over to our form
        return "admin-edit-car";
    }


    @PostMapping("/showCarFormForUpdate")
    public String showCarFormForUpdate(@RequestParam("id") int id,
                                        Model model) {

        // get the employee from the service
        Car car = carService.findCarById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("car", car);

        // send over to our form
        return "admin-edit-car";
    }

    @PostMapping("/saveCar")
    public String saveCar(@ModelAttribute("car") Car car) {

        Car existingCar = carService.findCarById(car.getId());
        System.out.println(car);
        existingCar.setName(car.getName());
        existingCar.setModel(car.getModel());
        existingCar.setYear(car.getYear());
        existingCar.setYear(car.getYear());
        existingCar.setSeats(car.getSeats());
        existingCar.setCapacity(car.getCapacity());
        existingCar.setMinimumDriverAge(car.getMinimumDriverAge());
        existingCar.setPrice(car.getPrice());
        existingCar.setPhotoUrl(car.getPhotoUrl());
        existingCar.setType(car.getType());
        carService.updateCar(existingCar);

        return "redirect:/admin";
    }

}
