package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public String showEditProfileForm(Principal principal, Model model) {
        // Get the currently authenticated user
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        // Add the current user to the model to populate the edit form
        model.addAttribute("user", currentUser);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editUserProfile(@Valid @ModelAttribute("user") User user,
                                  BindingResult bindingResult,
                                  Principal principal) {
        // Get the currently authenticated user
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        // Ensure the user is trying to edit their own profile
        if (currentUser.getId() != user.getId()) {
            return "redirect:/access-denied"; // Redirect if they are trying to edit another user's profile
        }

        if (bindingResult.hasErrors()) {
            return "edit-profile"; // Return to the form if there are validation errors
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(userService.getUserById(user.getId()).getPassword());
        }

        user.setPassword("{noop}" + user.getPassword());
        userService.updateUser(user); // Update user details

        return "redirect:/user/profile"; // Redirect to the profile page after successful update
    }



}
