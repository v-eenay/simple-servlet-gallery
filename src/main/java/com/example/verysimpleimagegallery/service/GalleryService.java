package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.GalleryItemDAO;
import com.example.verysimpleimagegallery.model.GalleryItem;
import java.util.ArrayList;

public class GalleryService {
    public static int addGallery(GalleryItem galleryItem) {
        try{
            return GalleryItemDAO.createGalleryItem(galleryItem);
        } catch (Exception e) {
            System.err.println(e.getMessage());;
        }
        return -1;
    }
    public static ArrayList<GalleryItem> getGalleryItem() {
        try{
            return GalleryItemDAO.getGalleryItems();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());;
        }
        return null;
    }
    public static GalleryItem getGalleryItemById(int id) {
        try{
            return GalleryItemDAO.getGalleryItem(id);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());;
        }
        return null;
    }
    public static boolean deleteGalleryItem(GalleryItem galleryItem) {
        try{
            return GalleryItemDAO.deleteGalleryItem(galleryItem);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
