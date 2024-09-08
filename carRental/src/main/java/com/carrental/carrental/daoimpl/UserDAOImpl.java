package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.UserDAO;
import com.carrental.carrental.entity.Car;
import com.carrental.carrental.entity.Role;
import com.carrental.carrental.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
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
        int roleCode = 4;//default for client
        if("SELLER".equals(role)) {
            roleCode = 6;
        }else if("ADMIN".equals(role)){
            roleCode = 3;
        }

        Role roleFound = em.find(Role.class, roleCode);
        Collection<Role> roles = new ArrayList<Role>();
        roles.add(roleFound);
        user.setRoles(roles);
//        em.merge(user);
    }

    @Override
    public boolean checkUsername(String username) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM User WHERE userName = :username", Long.class);
        query.setParameter("username", username);
        return query.getSingleResult() > 0;
    }

    @Override
    public User getUserByUsername(String username) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void addCar(User user, Car car) {
        user.addCar(car);
        em.merge(user);
    }

    @Override
    public List<Car> getOwnedCars(User user) {
        TypedQuery<Car> query = em.createQuery(
                "SELECT c FROM Car c WHERE c.owner.id = :ownerId", Car.class);

        query.setParameter("ownerId", user.getId());
        return query.getResultList();
    }


}
