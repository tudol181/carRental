package com.carrental.carrental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Rental")
@IdClass(RentalId.class)
public class Rental {

    @Id
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;


    public Rental() {
    }

    public Rental(User user, Car car, LocalDate pickupDate, LocalDate returnDate) {
        this.user = user;
        this.car = car;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "user=" + user.getId() +
                ", car=" + car.getId() +
                ", pickupDate=" + pickupDate +
                ", returnDate=" + returnDate +
                '}';
    }

}
