package com.carrental.carrental.service;

import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Transactional
    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }

    @Transactional
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public List<Car> getUsersCars(int id) {
        return userDAO.getUsersCars(id);
    }

    @Transactional
    public void addRole(User user, String role) {
        userDAO.addRole(user, role);
    }

    public boolean checkUsername(String username) {
        return userDAO.checkUsername(username);
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Transactional
    public void addCar(User user, Car car) {
        userDAO.addCar(user, car);
    }

    public List<Car> getOwnedCars(User user) {
        return userDAO.getOwnedCars(user);
    }

}
