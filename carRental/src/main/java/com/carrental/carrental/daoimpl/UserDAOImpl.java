package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAOImpl implements UserDAO {
    private EntityManager em;

    @Autowired
    public UserDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        em.persist(user);
    }

    @Override
    public User getUserById(int id) {
        User user = em.find(User.class, id);
        if (user != null) {
            // Force initialization of cars collection if it’s lazy-loaded
            user.getCars().size();
        }
        return user;
    }


}
