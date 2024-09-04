package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
@RequestMapping("/car")
public class CarController {
    private final CarService carService;
    private final UserService userService;

    @Autowired
    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("/addCar")
    public String addCar(Model model, Principal principal) {
        model.addAttribute("car", new Car());
        return "add-car";
    }

    @PostMapping("/addCar")
    public String addCar(@Valid @ModelAttribute("car") Car car, BindingResult carBindingResult, @RequestParam("photo") MultipartFile photo, Model model, Principal principal) {
        if(carBindingResult.hasErrors()) {
            return "add-car";
        }

        String photoPath = savePhoto(photo);
        car.setPhotoUrl(photoPath);
        car.setOwner(userService.getUserByUsername(principal.getName()));

        carService.saveCar(car);
        userService.getUserByUsername(principal.getName()).addCar(car);
        userService.updateUser(userService.getUserByUsername(principal.getName()));
        return "redirect:/";
    }

    private String savePhoto(MultipartFile photo) {
        // Define the folder where photos will be saved
        String folder = "C:\\Users\\tudy1\\OneDrive\\Desktop\\car-rental-project\\carRental\\src\\main\\resources\\static\\photos";
        String fileName = photo.getOriginalFilename();
        Path filePath = Paths.get(folder, fileName);

        // Create the directory if it does not exist
        File directory = new File(folder);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory structure
        }

        try {
            // Save the uploaded file
            photo.transferTo(filePath.toFile());
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        // Return the path to save in the database
        return "/photos/" + fileName;
    }

    @GetMapping("/{id}")
    public String getCarDetails(@PathVariable("id") int id, Model model) {
        Car car = carService.findCarById(id); // Make sure you have this method in your CarService
        model.addAttribute("car", car);
        return "car-details"; // This should be the name of your car details HTML template
    }


}
