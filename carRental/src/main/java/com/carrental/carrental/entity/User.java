package com.carrental.carrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "is required")
    @Size(min = 3, message = "username must have at least 3 characters")
    @Column(name = "user_name")
    private String userName;

    @NotNull(message = "is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First name must contain only letters")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Last name must contain only letters")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Password is required")
    @Size(min = 4, message = "Password must have at least 4 characters")
    @Size(max = 100, message = "Password must not exceed 100 characters") // Optional: Add maximum length
    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rental",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id"))
    private List<Car> cars;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Car> ownedCars;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id")
    private UserDetail userDetail;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, String password, boolean enabled, Collection<Role> roles, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.userName = userName;
    }

    /**
     * a car owner ads a car to his list
     *
     * @param car car owned
     */
    public void addOwnedCar(Car car) {
        if (ownedCars == null) {
            ownedCars = new ArrayList<>();
        }
        ownedCars.add(car);
        car.setOwner(this);
    }

    /**
     * adds a car to the user, if the user rents it
     *
     * @param car the rented car
     */
    public void addCar(Car car) {
        if (cars == null) {
            cars = new ArrayList<>();
        }
        cars.add(car);
    }

    //remove car from user
    public void removeCar(Car car) {
        cars.remove(car);
    }


    public void addOwnerCar(Car car) {
        if (ownedCars == null) {
            ownedCars = new ArrayList<>();
        }
        ownedCars.add(car);
        car.setOwner(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<Car> getOwnedCars() {
        return ownedCars;
    }

    public void setOwnedCars(List<Car> ownedCars) {
        this.ownedCars = ownedCars;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userDetail=" + userDetail +
                ", enabled=" + enabled +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", userName='" + userName + '\'' +
                ", id=" + id +
                ", roles=" + roles +
                '}';
    }
}