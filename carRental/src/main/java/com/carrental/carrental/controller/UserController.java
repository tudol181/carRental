package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Rental;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.service.RentalService;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RentalService rentalService;

    @Autowired
    public UserController(UserService userService, RentalService rentalService) {
        this.userService = userService;
        this.rentalService = rentalService;
    }

    @GetMapping("/profile")
    public String getUserProfile(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);
        List<Car> rentedCars = userService.getUsersCars(user.getId());
        List<Car> ownedCars = userService.getOwnedCars(user);
        List<Rental> rentals = rentalService.findRentalsByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("rentedCars", rentedCars);
        model.addAttribute("ownedCars", ownedCars);
        model.addAttribute("rentals", rentals);
        return "user-page";
    }

    @GetMapping("/edit-profile")
    public String showEditProfileForm(Principal principal, Model model) {
        // authenticated user
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        //populate user form
        model.addAttribute("user", currentUser);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editUserProfile(@Valid @ModelAttribute("user") User user,
                                  BindingResult bindingResult,
                                  Principal principal) {
        // authenticated user
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (!Objects.equals(currentUser.getId(), user.getId())) {
            return "redirect:/edit-profile"; //  edit another user's profile
        }

        if (bindingResult.hasErrors()) {
            return "edit-profile"; // validation errors
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = "{noop}" + user.getPassword();
            user.setPassword(encodedPassword);
        } else {
            // old password
            user.setPassword(currentUser.getPassword());
        }

        user.setCars(currentUser.getCars());
        user.setEnabled(true);
        user.setRoles(userService.getUserById(currentUser.getId()).getRoles());
        userService.updateUser(user);

        return "redirect:/user/profile";
    }



}
