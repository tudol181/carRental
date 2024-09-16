package com.carrental.carrental.dao;

import com.carrental.carrental.entity.Rental;
import com.carrental.carrental.entity.RentalId;

import java.time.LocalDate;
import java.util.List;

public interface RentalDAO {
    void saveRental(Rental rental);
    void updateRental(Rental rental);
    void updateRentalDates(int userId, int carId, LocalDate pickupDate, LocalDate returnDate);
    List<Rental> findRentalsByCarId(int id);
    List<Rental> findRentalsByUserId(int id);
    List<Rental> getAllRentals();
    Rental findRentalById(RentalId id);
    Rental findRentalByUserIdAndCarId(int userId, int carId);
    void deleteRental(Rental rental);
}
