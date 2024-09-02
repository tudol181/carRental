package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Role;
import com.carrental.carrental.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    private EntityManager em;

    @Autowired
    public UserDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void saveUser(User user) {
        em.persist(user);
    }

    @Override
    public User getUserById(int id) {
        User user = em.find(User.class, id);
        if (user != null) {
            user.getCars().size();
        }
        return user;
    }

    @Override
    public void deleteUser(int id) {
        em.remove(em.find(User.class, id));
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public List<Car> getUsersCars(int id) {
        User user = em.find(User.class, id);
        return user.getCars();
    }

    @Override
    public void addRole(User user, String role) {
        int roleCode = 1;
        if("SELLER".equals(role)) {
            roleCode = 2;
        }
        Role roleFound = em.find(Role.class, roleCode);
        Collection<Role> roles = new ArrayList<Role>();
        roles.add(roleFound);
        user.setRoles(roles);
//        em.merge(user);
    }


}
