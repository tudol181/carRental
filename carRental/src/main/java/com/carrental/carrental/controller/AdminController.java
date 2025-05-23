package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.RentalService;
import com.carrental.carrental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(CarService carService, UserService userService, RentalService rentalService, PasswordEncoder passwordEncoder) {
        this.carService = carService;
        this.userService = userService;
        this.rentalService = rentalService;
        this.passwordEncoder = passwordEncoder;
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
//            String encodedPassword = "{noop}" + user.getPassword();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        } else {
            //get old password
            user.setPassword(existingUser.getPassword());
        }
        //get the old values if nothing is changed
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
    public String showUserFormForUpdateGET(@RequestParam("id") int id, Model model) {

        // get the employee from the service
        User user = userService.getUserById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);


        return "admin-update-form";
    }

    @PostMapping("/showUserFormForUpdate")
    public String showUserFormForUpdate(@RequestParam("id") int id,
                                        Model theModel) {

        // get the employee from the service
        User user = userService.getUserById(id);

        // set employee as a model attribute to prepopulate the form
        theModel.addAttribute("user", user);

        return "admin-update-form";
    }

    // car section

    @GetMapping("/showCarFormForUpdate")
    public String showCarFormForUpdateGET(@RequestParam("id") int id, Model model) {

        // get the car
        Car car = carService.findCarById(id);
        // prepopulate with car info
        model.addAttribute("car", car);
        model.addAttribute("carTypes", List.of("Small car", "SUV", "Break", "Sport", "Roadster", "Van"));
        return "admin-edit-car";
    }


    @PostMapping("/showCarFormForUpdate")
    public String showCarFormForUpdate(@RequestParam("id") int id,
                                       Model model) {

        // get the employee from the service
        Car car = carService.findCarById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("car", car);

        return "admin-edit-car";
    }

    @PostMapping("/saveCar")
    public String saveCar(@ModelAttribute("car") Car car) {

        Car existingCar = carService.findCarById(car.getId());
//        System.out.println(car);
        CarController.existCarSetter(car, existingCar);
        carService.updateCar(existingCar);

        return "redirect:/admin";
    }

}
