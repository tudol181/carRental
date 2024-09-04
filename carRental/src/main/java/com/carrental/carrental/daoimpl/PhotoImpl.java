package com.carrental.carrental.daoimpl;

import com.carrental.carrental.dao.PhotoDAO;
import com.carrental.carrental.entity.Photo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoImpl implements PhotoDAO {
    EntityManager entityManager;

    @Autowired
    public PhotoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void savePhoto(Photo photo) {
        entityManager.persist(photo);
    }
}
