package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.GalleryItemDAO;
import com.example.verysimpleimagegallery.model.GalleryItem;
import java.util.ArrayList;

public class GalleryService {
    public static int addGallery(GalleryItem galleryItem) {
        try{
            return GalleryItemDAO.addGalleryItem(galleryItem);
        } catch (Exception e) {
            System.err.println(e.getMessage());;
        }
        return -1;
    }
    public static ArrayList<GalleryItem> getGalleryItem(int id) {
        try{
            return (ArrayList<GalleryItem>) GalleryItemDAO.getUserGalleryItems(id);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());;
        }
        return null;
    }
    public static GalleryItem getGalleryItemById(int id) {
        try{
            return GalleryItemDAO.getGalleryItemById(id);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());;
        }
        return null;
    }
    public static boolean deleteGalleryItem(GalleryItem galleryItem) {
        try{
            return GalleryItemDAO.deleteGalleryItem(galleryItem.getId());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
