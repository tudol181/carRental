package com.carrental.carrental.controller;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Photo;
import com.carrental.carrental.entity.Rental;
import com.carrental.carrental.entity.User;
import com.carrental.carrental.service.CarService;
import com.carrental.carrental.service.PhotoService;
import com.carrental.carrental.service.RentalService;
import com.carrental.carrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car")
public class CarController {
    private final CarService carService;
    private final UserService userService;
    private final PhotoService photoService;
    private final RentalService rentalService;

    @Autowired
    public CarController(CarService carService, UserService userService, PhotoService photoService, RentalService rentalService) {
        this.carService = carService;
        this.userService = userService;
        this.photoService = photoService;
        this.rentalService = rentalService;
    }

    @GetMapping("/addCar")
    public String addCar(Model model, Principal principal) {
        model.addAttribute("car", new Car());
        return "add-car";
    }

    @PostMapping("/addCar")
    public String addCar(@Valid @ModelAttribute("car") Car car, BindingResult carBindingResult, @RequestParam("photo") MultipartFile photo, Model model, Principal principal) {
        if (carBindingResult.hasErrors()) {
            return "add-car";
        }

        car.setOwner(userService.getUserByUsername(principal.getName()));
        carService.saveCar(car);

        String photoPath = savePhoto(photo, car);

        car.setPhotoUrl(photoPath);
        carService.updateCar(car);

        userService.getUserByUsername(principal.getName()).addOwnedCar(car);
        userService.updateUser(userService.getUserByUsername(principal.getName()));
        return "redirect:/car/addCarPhotos/" + car.getId();
    }

    private String savePhoto(MultipartFile photo, Car car) {
        // Define the folder base path
        String baseFolder = "C:\\Users\\tudy1\\OneDrive\\Desktop\\car-rental-project\\carRental\\src\\main\\resources\\static\\photos";

        // Create folder name using car ID and name
        String folderName = car.getId() + "_" + car.getName().replaceAll("\\s+", "_"); // Replace spaces with underscores
        String folderPath = Paths.get(baseFolder, folderName).toString();

        // Create the directory if it does not exist
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory structure
        }

        // Create the file path for the photo
        String fileName = photo.getOriginalFilename();
        Path filePath = Paths.get(folderPath, fileName);

        try {
            // Save the uploaded file
            photo.transferTo(filePath.toFile());
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        // Return the path to save in the database
        return "/photos/" + folderName + "/" + fileName; // Updated path to reflect folder structure
    }


    @GetMapping("/{id}")
    public String getCarDetails(@PathVariable("id") int id, Model model, Principal principal) {
        Car car = carService.findCarById(id);
        if (car == null) {
            model.addAttribute("errorMessage", "Car not found");
            return "error-page";
        }

        if (principal != null) {
            User currentUser = userService.getUserByUsername(principal.getName());
            boolean isRented = currentUser.getCars().contains(car);
            model.addAttribute("isRented", isRented);
        }

        List<Photo> photos = car.getPhotos(); // all car photos
        // not display the null entries(while testing)
        List<Rental> rentals = rentalService.findRentalsByCarId(car.getId()).stream()
                .filter(rental -> rental.getPickupDate() != null && rental.getReturnDate() != null)
                .collect(Collectors.toList());

        model.addAttribute("car", car);
        model.addAttribute("photos", photos);
        model.addAttribute("rentals", rentals);
        return "car-details";
    }


    // car form
    @GetMapping("/addCarPhotos/{id}")
    public String addCarPhotosPage(@PathVariable("id") Integer id, Model model) {
        Car car = carService.findCarById(id);
        model.addAttribute("car", car);
        return "add-car-photo-page";
    }

    @PostMapping("/addCarPhotos/{id}")
    public String addCarPhotos(@PathVariable("id") Integer id, @RequestParam(value = "photos", required = false) MultipartFile[] photos) {
        Car car = carService.findCarById(id);

        for (MultipartFile photo : photos) {
            if (photo != null && !photo.isEmpty()) {
                String photoUrl = savePhoto(photo, car);//also get the url

                Photo picture =new Photo(photoUrl, car);
                photoService.savePhoto(picture);
            }
        }

        return "redirect:/";
    }

    @PostMapping("/{id}/rent")
    public String rentCar(@PathVariable("id") int id,
                          @RequestParam("pickupDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
                          @RequestParam("returnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                          Principal principal,
                          Model model) {

        User user = userService.getUserByUsername(principal.getName());

        Car car = carService.findCarById(id);
        if (car == null) {
            return "redirect:/car/" + id + "?error=CarNotFound";
        }

        boolean isAvailable = carService.isAvailable(car, pickupDate, returnDate);
        if (!isAvailable) {
            model.addAttribute("errorMessage", "Car not available on that date.");
            return getCarDetails(id, model, principal);
        }

        // Add the car to the user's rented cars
        user.addCar(car);
        userService.updateUser(user);  // Update the user
        rentalService.updateRentalDates(user.getId(), car.getId(), pickupDate, returnDate);
        return "redirect:/car/" + id + "?success=CarRented";
    }


    @PostMapping("/{id}/remove-rented-car")
    public String removeRentedCar(@PathVariable("id") int id, Principal principal) {
        // Get the user
        User user = userService.getUserByUsername(principal.getName());

        // Find the car by id
        Car car = carService.findCarById(id);
        if (car == null) {
            return "redirect:/car/" + id + "?error=CarNotFound";
        }

        // Remove the car from the user's rented cars
        user.removeCar(car);  // Assuming you have a `removeCar` method in the `User` entity
        userService.updateUser(user);  // Update the user

        return "redirect:/user/profile";  // Redirect to the user's profile after removal
    }

}
