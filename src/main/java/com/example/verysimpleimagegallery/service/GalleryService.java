package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.GalleryItemDAO;
import com.example.verysimpleimagegallery.dao.TagDAO;
import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.Tag;

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
            ArrayList<GalleryItem> items = new ArrayList<>(GalleryItemDAO.getUserGalleryItems(userId));
            // Load tags for each item
            TagService.loadTagsForGalleryItems(items);
            return items;
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
            GalleryItem item = GalleryItemDAO.getGalleryItemById(id);
            if (item != null) {
                // Load tags for the item
                item.setTags(TagDAO.getTagsForImage(id));
            }
            return item;
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
            ArrayList<GalleryItem> items = new ArrayList<>(GalleryItemDAO.getRecentActivities(limit));
            // Load tags for each item
            TagService.loadTagsForGalleryItems(items);
            return items;
        } catch (Exception e) {
            System.err.println("Error in getRecentActivities: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Updates a gallery item's title.
     * @param itemId The ID of the gallery item
     * @param title The new title
     * @return true if update was successful, false otherwise
     */
    public static boolean updateGalleryItemTitle(int itemId, String title) {
        try {
            return GalleryItemDAO.updateGalleryItemTitle(itemId, title);
        } catch (Exception e) {
            System.err.println("Error in updateGalleryItemTitle: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a gallery item's image.
     * @param itemId The ID of the gallery item
     * @param image The new image data
     * @return true if update was successful, false otherwise
     */
    public static boolean updateGalleryItemImage(int itemId, byte[] image) {
        try {
            return GalleryItemDAO.updateGalleryItemImage(itemId, image);
        } catch (Exception e) {
            System.err.println("Error in updateGalleryItemImage: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a gallery item's tags.
     * @param itemId The ID of the gallery item
     * @param tagString Comma-separated list of tag names
     * @return true if update was successful, false otherwise
     */
    public static boolean updateGalleryItemTags(int itemId, String tagString) {
        try {
            return TagService.updateImageTags(itemId, tagString);
        } catch (Exception e) {
            System.err.println("Error in updateGalleryItemTags: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fully updates a gallery item (title, image, and tags).
     * @param itemId The ID of the gallery item
     * @param title The new title (or null to keep existing)
     * @param image The new image data (or null to keep existing)
     * @param tagString Comma-separated list of tag names (or null to keep existing)
     * @return true if all updates were successful, false otherwise
     */
    public static boolean updateGalleryItem(int itemId, String title, byte[] image, String tagString) {
        boolean success = true;

        // Update title if provided
        if (title != null && !title.trim().isEmpty()) {
            success = updateGalleryItemTitle(itemId, title) && success;
        }

        // Update image if provided
        if (image != null && image.length > 0) {
            success = updateGalleryItemImage(itemId, image) && success;
        }

        // Update tags if provided
        if (tagString != null) {
            success = updateGalleryItemTags(itemId, tagString) && success;
        }

        return success;
    }
}
