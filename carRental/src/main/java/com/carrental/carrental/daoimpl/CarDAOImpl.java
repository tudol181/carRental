package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.CarDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Review;
import com.carrental.carrental.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CarDAOImpl implements CarDAO {

    private EntityManager em;

    @Autowired
    public CarDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void save(Car car) {
        em.persist(car);
    }

    @Override
    public List<Review> findReviewByCarId(int id) {
        // create the query
        TypedQuery<Review> query = em.createQuery("from Review where Car.id =:data", Review.class);
        query.setParameter("data", id);

        // execute query
        return query.getResultList();
    }

    @Override
    public Car findCarById(int id) {
        return em.find(Car.class, id);
    }

    @Override
    public List<Car> findCarsByUserId(int id) {
        return em.find(User.class, id).getCars();
    }

    @Override
    public void updateCar(Car car) {
        em.merge(car);
    }

    @Override
    public List<Car> findAllCars() {
        TypedQuery<Car> query = em.createQuery("from Car", Car.class);
        return query.getResultList();
    }

    @Override
    public boolean isAvailable(Car car, LocalDate pickupDate, LocalDate returnDate) {
        TypedQuery<Long> query = em.createQuery("select count(*) from Rental where car.id =:carId and " +
                "(:pickupDate <= returnDate and :returnDate >= pickupDate)", Long.class);
        query.setParameter("carId", car.getId());
        query.setParameter("pickupDate", pickupDate);
        query.setParameter("returnDate", returnDate);

        return query.getSingleResult() == 0;
    }

    @Override
    public List<Car> sortCarsByPrice(List<Car> cars, boolean ascending) {
        return cars.stream()
                .sorted(ascending ? Comparator.comparing(Car::getPrice) : Comparator.comparing(Car::getPrice).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCar(int id) {
        Car car = findCarById(id);
        em.remove(car);
    }

    @Override
    public List<User> findRentersByCarId(int id) {
        TypedQuery<User> query = em.createQuery("select r.user from Rental r where r.car.id =:id", User.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public void removeReview(int carId, int reviewId) {
        Car car = findCarById(carId);
        car.getReviews().removeIf(review -> review.getId() == reviewId);
    }

    @Override
    public List<Car> findCarsByType(String type) {
        TypedQuery<Car> query = em.createQuery("from Car where type =:type", Car.class);
        query.setParameter("type", type);
        return query.getResultList();
    }
}
