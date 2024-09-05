package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.RentalDAO;
import com.carrental.carrental.entity.Rental;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RentalDAOImpl implements RentalDAO {
    private EntityManager em;

    @Autowired
    public RentalDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void saveRental(Rental rental) {
        em.persist(rental);
    }

    @Override
    public void updateRentalDates(int userId, int carId, LocalDate pickupDate, LocalDate returnDate) {
        TypedQuery<Rental> query = em.createQuery(
                "SELECT r FROM Rental r WHERE r.user.id = :userId AND r.car.id = :carId", Rental.class);
        query.setParameter("userId", userId);
        query.setParameter("carId", carId);

        Rental rental;
        try {
            rental = query.getSingleResult();  // Fetch the single rental
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Rental not found for user " + userId + " and car " + carId);
        }

        rental.setPickupDate(pickupDate);
        rental.setReturnDate(returnDate);

        em.merge(rental);
    }

    @Override
    public List<Rental> findRentalsByCarId(int id) {
        TypedQuery<Rental> query = em.createQuery(
                "SELECT r FROM Rental r WHERE r.car.id = :id", Rental.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

    @Override
    public List<Rental> findRentalsByUserId(int id) {
        TypedQuery<Rental> query = em.createQuery(
                "SELECT r FROM Rental r WHERE r.user.id = :id", Rental.class);
        query.setParameter("id", id);

        return query.getResultList();
    }
}
