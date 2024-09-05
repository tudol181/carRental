package com.carrental.carrental.entity;

import java.io.Serializable;
import java.util.Objects;

public class RentalId implements Serializable {
    private int user;
    private int car;

    // Default constructor, hashCode, equals, and getters/setters

    public RentalId() {
    }

    public RentalId(int user, int car) {
        this.user = user;
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalId rentalId = (RentalId) o;
        return user == rentalId.user && car == rentalId.car;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, car);
    }
}
