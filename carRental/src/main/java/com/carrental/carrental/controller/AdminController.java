package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.UserService;
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

    @Autowired
    public AdminController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
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
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }
        // Retain old values if the new values are empty
        if (user.getUserDetail().getCountry() == null || user.getUserDetail().getCountry().isEmpty()) {
            user.getUserDetail().setCountry(existingUser.getUserDetail().getCountry());
        }

        if (user.getUserDetail().getCity() == null || user.getUserDetail().getCity().isEmpty()) {
            user.getUserDetail().setCity(existingUser.getUserDetail().getCity());
        }

        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdateGET(@RequestParam("id") int id,Model model) {

        // get the employee from the service
        User user = userService.getUserById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);

        // send over to our form
        return "admin-update-form";
    }


    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("id") int id,
                                    Model theModel) {

        // get the employee from the service
        User user = userService.getUserById(id);

        // set employee as a model attribute to pre-populate the form
        theModel.addAttribute("user", user);

        // send over to our form
        return "admin-update-form";
    }

}
