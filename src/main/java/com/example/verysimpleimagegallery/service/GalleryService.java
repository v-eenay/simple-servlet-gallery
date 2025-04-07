package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.GalleryItemDAO;
import com.example.verysimpleimagegallery.model.GalleryItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for handling gallery-related business logic.
 * Acts as an intermediary between controllers and the DAO layer.
 */
public class GalleryService {
    
    /**
     * Adds a new gallery item to the system.
     * @param galleryItem The item to add
     * @return The ID of the newly added item, or -1 if operation failed
     */
    public static int addGallery(GalleryItem galleryItem) {
        try {
            return GalleryItemDAO.addGalleryItem(galleryItem);
        } catch (Exception e) {
            System.err.println("Error in addGallery: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves gallery items for a specific user.
     * @param userId The ID of the user
     * @return List of gallery items, or empty list if operation failed
     */
    public static ArrayList<GalleryItem> getGalleryItem(int userId) {
        try {
            return new ArrayList<>(GalleryItemDAO.getUserGalleryItems(userId));
        } catch (Exception e) {
            System.err.println("Error in getGalleryItem: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a specific gallery item by its ID.
     * @param id The ID of the gallery item
     * @return The gallery item if found, null otherwise
     */
    public static GalleryItem getGalleryItemById(int id) {
        try {
            return GalleryItemDAO.getGalleryItemById(id);
        } catch (Exception e) {
            System.err.println("Error in getGalleryItemById: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a gallery item from the system.
     * @param galleryItem The item to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteGalleryItem(GalleryItem galleryItem) {
        try {
            return GalleryItemDAO.deleteGalleryItem(galleryItem.getId());
        } catch (Exception e) {
            System.err.println("Error in deleteGalleryItem: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves recent gallery activities.
     * @param limit Maximum number of activities to retrieve
     * @return List of recent gallery items, or empty list if operation failed
     */
    public static ArrayList<GalleryItem> getRecentActivities(int limit) {
        try {
            return new ArrayList<>(GalleryItemDAO.getRecentActivities(limit));
        } catch (Exception e) {
            System.err.println("Error in getRecentActivities: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
