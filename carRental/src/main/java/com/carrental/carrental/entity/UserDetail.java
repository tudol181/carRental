package com.carrental.carrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user_details")
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull

    @Column(name = "country")
    @NotNull(message = "is required")
    @Size(max = 50, message = "Country name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Country must contain only letters")
    private String country;

    @Column(name = "city")
    @NotNull(message = "is required")
    @Size(max = 50, message = "City name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "City must contain only letters")
    private String city;

    @Column(name = "email")
    @NotNull(message = "is required")
    @Email(message = "Email should be valid")
    private String email;

    @OneToOne(mappedBy = "userDetail", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private User user;

    @Column(name = "phone_number")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be numeric and only contain digits")
    private String phoneNumber;

    public UserDetail(String phoneNumber, String email, String city, String country) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
        this.country = country;
    }

    public UserDetail() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^\\d{10}$", message = "Phone number must be numeric and only contain digits") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "^\\d{10}$", message = "Phone number must be numeric and only contain digits") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}