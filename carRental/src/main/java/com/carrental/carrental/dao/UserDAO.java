package com.carrental.carrental.dao;

import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.User;

import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    User getUserById(int id);
    void deleteUser(int id);
    void updateUser(User user);
    List<Car> getUsersCars(int id);
    void addRole(User user, String role);
    boolean checkUsername(String username);
    User getUserByUsername(String username);
    List<User> getAllUsers();
}
