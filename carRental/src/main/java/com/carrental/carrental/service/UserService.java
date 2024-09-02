package com.carrental.carrental.service;

import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

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
}
