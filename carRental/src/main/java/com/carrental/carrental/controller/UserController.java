package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.entity.UserDetail;
import com.carrental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getUserProfile(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);
        List<Car> rentedCars = userService.getUsersCars(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("rentedCars", rentedCars);
        return "user-page";
    }

    @GetMapping("/edit-profile")
    public String showEditProfileForm(@RequestParam("id") int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editUserProfile(@ModelAttribute("user") User user) {

        // Update the existing user's details
        userService.updateUser(user); // Assume this method handles both user and userDetail

        return "redirect:/user/profile"; // Redirect to the profile page after successful update
    }


}
