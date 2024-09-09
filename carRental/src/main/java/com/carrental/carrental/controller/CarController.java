package com.carrental.carrental.controller;

import com.carrental.carrental.entity.*;
import com.carrental.carrental.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car")
public class CarController {
    private final CarService carService;
    private final UserService userService;
    private final PhotoService photoService;
    private final RentalService rentalService;
    private final ReviewService reviewService;

    @Autowired
    public CarController(CarService carService, UserService userService, PhotoService photoService, RentalService rentalService, ReviewService reviewService) {
        this.carService = carService;
        this.userService = userService;
        this.photoService = photoService;
        this.rentalService = rentalService;
        this.reviewService = reviewService;
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
        car.setType(car.getType());
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
        List<Review> reviews = reviewService.getReviewByCarId(id);

        if (car == null) {
            model.addAttribute("errorMessage", "Car not found");
            return "error-page";
        }

        if (principal != null) {
            User currentUser = userService.getUserByUsername(principal.getName());
            boolean isRented = currentUser.getCars().contains(car);
            model.addAttribute("isRented", isRented);
            model.addAttribute("currentUserId", currentUser.getId());

            List<Review> userReviews = reviews.stream()
                    .filter(review -> Objects.equals(review.getUser().getId(), currentUser.getId()))
                    .collect(Collectors.toList());
            model.addAttribute("userReviews", userReviews);
        }

        List<Photo> photos = car.getPhotos(); // all car photos
        // not display the null entries(while testing)
        List<Rental> rentals = rentalService.findRentalsByCarId(car.getId()).stream()
                .filter(rental -> rental.getPickupDate() != null && rental.getReturnDate() != null)
                .collect(Collectors.toList());

        model.addAttribute("car", car);
        model.addAttribute("photos", photos);
        model.addAttribute("rentals", rentals);
        model.addAttribute("reviews", reviews.subList(0, Math.min(reviews.size(), 20)));
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

                Photo picture = new Photo(photoUrl, car);
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

        //pickup date < return date
        if (pickupDate.isAfter(returnDate)) {
            model.addAttribute("errorMessage", "Pickup date must be before return date.");
            return getCarDetails(id, model, principal);
        }

        boolean isAvailable = carService.isAvailable(car, pickupDate, returnDate);
        if (!isAvailable) {
            model.addAttribute("errorMessage", "Car not available on that date.");
            return getCarDetails(id, model, principal);
        }

        // Check for existing rentals for the user
        List<Rental> existingRentals = rentalService.findRentalsByUserId(user.getId());

        // Ensure that no existing rental conflicts with the new one
        for (Rental rental : existingRentals) {
            if (rental.getCar().getId() == id) {
                model.addAttribute("errorMessage", "You already have a rental for this car.");
                return getCarDetails(id, model, principal);
            }
        }

        // Create a new rental if no existing conflict is found
        Rental newRental = new Rental(user, car, pickupDate, returnDate);
        rentalService.saveRental(newRental); // Save the new rental
        car.setNrRenters(car.getNrRenters() + 1);
        carService.updateCar(car);
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

        car.setNrRenters(car.getNrRenters() - 1);
        carService.updateCar(car);
        // remove the car from the user's rented cars
        user.removeCar(car);
        userService.updateUser(user);

        return "redirect:/user/profile";  // Redirect to the user's profile after removal
    }

    @PostMapping("/delete")
    public String deleteCar(@RequestParam("id") int id, Principal principal, RedirectAttributes redirectAttributes) {
        Car car = carService.findCarById(id);
        if (car == null || !car.getOwner().getUserName().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Car not found or you do not have permission to delete it.");
            return "redirect:/error"; // Redirect if car not found or not the owner
        }

        carService.deleteCar(id);
        redirectAttributes.addFlashAttribute("successMessage", "Car deleted successfully.");
        return "redirect:/user/profile"; // Redirect to the user's profile after deletion
    }

    @GetMapping("/showCarFormForUpdate")
    public String showCarFormForUpdateGET(@RequestParam("id") int id,
                                          Principal principal,
                                          Model model) {

        // get the car
        Car car = carService.findCarById(id);
        if (!Objects.equals(car.getOwner().getId(), userService.getUserByUsername(principal.getName()).getId())) {
            // Redirect or show error message
            return "redirect:/error"; // Redirect to an error page or show a message
        }
        // prepopulate with car info
        model.addAttribute("car", car);

        // send over to our form
        return "seller-edit-file";
    }


    @PostMapping("/showCarFormForUpdate")
    public String showCarFormForUpdate(@RequestParam("id") int id,
                                       Principal principal,
                                       Model model) {

        // get the employee from the service
        Car car = carService.findCarById(id);

        if (!Objects.equals(car.getOwner().getId(), userService.getUserByUsername(principal.getName()).getId())) {
            // Redirect or show error message
            return "redirect:/error"; // Redirect to an error page or show a message
        }

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("car", car);

        // send over to our form
        return "seller-edit-file";
    }

    @PostMapping("/saveCar")
    public String saveCar(@ModelAttribute("car") Car car,
                          Principal principal) {
        Car existingCar = carService.findCarById(car.getId());
        if (!Objects.equals(existingCar.getOwner().getId(), userService.getUserByUsername(principal.getName()).getId())) {
            // Redirect or show error message
            return "redirect:/error"; // Redirect to an error page or show a message
        }
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

        return "redirect:/user/profile";
    }

    @PostMapping("/{carId}/review")
    public String postReview(@PathVariable int carId, @RequestParam String comment, Principal principal) {
        System.out.println(principal);
        User user = userService.getUserByUsername(principal.getName());
        Review review = new Review();
        review.setComment(comment);
        review.setCar(carService.findCarById(carId));
        review.setUser(user);
        System.out.println(user);
        reviewService.saveReview(review);
        return "redirect:/car/" + carId;
    }

    @GetMapping("/review/edit")
    public String editReview(@RequestParam("id") int id, Model model, Principal principal) {
        Review review = reviewService.getReviewById(id);
        if (review == null || !review.getUser().getUserName().equals(principal.getName())) {
            return "redirect:/error"; // Redirect or show error message
        }
        model.addAttribute("review", review);
        return "edit-review"; // Create an edit-review.html template for this
    }

    @PostMapping("/review/edit/{id}")
    public String saveEditedReview(@ModelAttribute Review review, Principal principal) {
        Review existingReview = reviewService.getReviewById(review.getId());
        if (existingReview == null || !existingReview.getUser().getUserName().equals(principal.getName())) {
            return "redirect:/error"; // Redirect or show error message
        }
        existingReview.setComment(review.getComment());
        reviewService.saveReview(existingReview);
        return "redirect:/car/" + existingReview.getCar().getId();
    }

    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam("id") int id, Principal principal) {
        Review review = reviewService.getReviewById(id);
        if (review == null || !review.getUser().getUserName().equals(principal.getName())) {
            return "redirect:/error"; // Redirect or show error message
        }
        System.out.println(review);
        carService.removeReview(review.getCar().getId(), review.getId());
        reviewService.deleteReview(review.getId());
        System.out.println(review);
        return "redirect:/car/" + review.getCar().getId();
    }

}
