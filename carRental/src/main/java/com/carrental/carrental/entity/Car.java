package com.carrental.carrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "Name")
    @NotNull(message = "is required")
    private String name;

    @Column(name = "model")
    @NotNull(message = "Model is required")
    private String model;

    @Column(name = "year")
    @NotNull(message = "Year is required")
//    @Size(max = 50, message = "Year must not exceed 50 characters")
//    @Pattern(regexp = "^\\d{10}$", message = "Year must be numeric and only contain digits")
    private Integer year;

    @Column(name = "seats")
    @NotNull(message = "Year is required")
    @Min(value = 1, message = "At least a seat")
    private Integer seats;

    @Column(name = "capacity")
    @NotNull(message = "Capacity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Capacity must be greater than 0")
    private Float capacity;

    @Column(name = "minimum_driver_age")
    @NotNull(message = "Minimum driver age is required")
    @Min(value = 18, message = "Minimum driver age must be at least 18")
    private Integer minimumDriverAge;

    @Column(name = "price")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String type;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "rental",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "car_id")
    private List<Review> reviews;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Photo> photos;

    public Car() {

    }

    public Car(String name, String model, Integer year, Integer seats, Float capacity, Integer minimumDriverAge, BigDecimal price, String photoUrl, String type) {
        this.name = name;
        this.model = model;
        this.year = year;
        this.seats = seats;
        this.capacity = capacity;
        this.minimumDriverAge = minimumDriverAge;
        this.price = price;
        this.photoUrl = photoUrl;
        this.type = type;
    }

    /**
     * add the review to the car
     *
     * @param review the review to be added
     */
    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new ArrayList<Review>();
        }
        reviews.add(review);
    }

    /**
     * add a user to the car
     *
     * @param user user that rents the acr
     */
    public void addUser(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
        //user.getCars().add(this);
    }

    public void addPhoto(Photo photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(photo);
        photo.setCar(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Float getCapacity() {
        return capacity;
    }

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    public Integer getMinimumDriverAge() {
        return minimumDriverAge;
    }

    public void setMinimumDriverAge(Integer minimumDriverAge) {
        this.minimumDriverAge = minimumDriverAge;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User ownerId) {
        this.owner = ownerId;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", seats=" + seats +
                ", capacity=" + capacity +
                ", minimumDriverAge=" + minimumDriverAge +
                ", price=" + price +
                ", photoUrl='" + photoUrl + '\'' +
                ", owner=" + owner +
                ", users=" + users +
                '}';
    }
}