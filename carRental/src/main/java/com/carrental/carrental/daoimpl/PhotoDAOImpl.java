package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.PhotoDAO;
import com.carrental.carrental.entity.Photo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhotoDAOImpl implements PhotoDAO {
    EntityManager entityManager;

    @Autowired
    public PhotoDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void savePhoto(Photo photo) {
        entityManager.persist(photo);
    }

    @Override
    public List<Photo> getPhotosByCarId(int id) {
        TypedQuery<Photo> query = entityManager.createQuery(
                "SELECT p FROM Photo p WHERE p.car.id = :id", Photo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public void deletePhoto(int id) {
        entityManager.remove(entityManager.find(Photo.class, id));
    }
}
