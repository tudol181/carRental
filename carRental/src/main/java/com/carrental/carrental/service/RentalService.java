package com.carrental.carrental.service;

import com.carrental.carrental.dao.RentalDAO;
import com.carrental.carrental.entity.Rental;
import com.carrental.carrental.entity.RentalId;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {
    public final RentalDAO rentalDAO;

    @Autowired
    public RentalService(RentalDAO rentalDAO) {
        this.rentalDAO = rentalDAO;
    }

    @Transactional
    public void saveRental(Rental rental) {
        rentalDAO.saveRental(rental);
    }

    @Transactional
    public void updateRentalDates(int userId, int carId, LocalDate pickupDate, LocalDate returnDate) {
        rentalDAO.updateRentalDates(userId, carId, pickupDate, returnDate);
    }

    public List<Rental> findRentalsByCarId(int id) {
        return rentalDAO.findRentalsByCarId(id);
    }

    public List<Rental> findRentalsByUserId(int id){
        return rentalDAO.findRentalsByUserId(id);
    }

    @Transactional
    public void updateRental(Rental rental){
        rentalDAO.updateRental(rental);
    }

    public Rental findRentalById(RentalId id){
        return rentalDAO.findRentalById(id);
    }

}
