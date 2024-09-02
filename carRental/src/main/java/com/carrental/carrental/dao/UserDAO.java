package com.carrental.carrental.dao;

import com.carrental.carrental.entity.User;

public interface UserDAO {
    void saveUser(User user);
    User getUserById(int id);

}
