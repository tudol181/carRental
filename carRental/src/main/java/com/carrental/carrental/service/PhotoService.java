package com.carrental.carrental.service;

import com.carrental.carrental.dao.PhotoDAO;
import com.carrental.carrental.entity.Photo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    private final PhotoDAO photoDAO;

    @Autowired
    public PhotoService(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    @Transactional
    public void savePhoto(Photo photo) {
        photoDAO.savePhoto(photo);
    }
}
