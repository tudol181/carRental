package com.carrental.carrental.dao;

import com.carrental.carrental.entity.Photo;

import java.util.List;

public interface PhotoDAO {
    void savePhoto(Photo photo);
    List<Photo> getPhotosByCarId(int id);
    void deletePhoto(int id);
}
