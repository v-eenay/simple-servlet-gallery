package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.TagDAO;
import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for handling tag-related business logic.
 */
public class TagService {

    /**
     * Creates a new tag in the system.
     * @param tagName The name of the tag
     * @return The created tag, or null if creation failed
     */
    public static Tag createTag(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            return null;
        }
        
        // Clean the tag name
        tagName = tagName.toLowerCase().trim();
        
        // Check if tag already exists
        Tag existingTag = TagDAO.getTagByName(tagName);
        if (existingTag != null) {
            return existingTag;
        }
        
        // Create new tag
        Tag newTag = new Tag(tagName);
        int tagId = TagDAO.createTag(newTag);
        if (tagId > 0) {
            newTag.setId(tagId);
            return newTag;
        }
        
        return null;
    }
    
    /**
     * Gets all tags in the system.
     * @return List of all tags
     */
    public static List<Tag> getAllTags() {
        return TagDAO.getAllTags();
    }
    
    /**
     * Gets all tags for a specific image.
     * @param imageId The ID of the image
     * @return List of tags associated with the image
     */
    public static List<Tag> getTagsForImage(int imageId) {
        return TagDAO.getTagsForImage(imageId);
    }
    
    /**
     * Updates the tags for an image.
     * @param imageId The ID of the image
     * @param tagString Comma-separated list of tag names
     * @return true if successful, false otherwise
     */
    public static boolean updateImageTags(int imageId, String tagString) {
        try {
            // Remove all existing tags
            TagDAO.removeAllTagsFromImage(imageId);
            
            // If no new tags, we're done
            if (tagString == null || tagString.trim().isEmpty()) {
                return true;
            }
            
            // Parse and add new tags
            String[] tagNames = tagString.split(",");
            for (String tagName : tagNames) {
                tagName = tagName.trim();
                if (!tagName.isEmpty()) {
                    Tag tag = createTag(tagName);
                    if (tag != null) {
                        TagDAO.addTagToImage(imageId, tag.getId());
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error updating image tags: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates the tags for a gallery item.
     * @param item The gallery item to update
     * @param tagString Comma-separated list of tag names
     * @return true if successful, false otherwise
     */
    public static boolean updateGalleryItemTags(GalleryItem item, String tagString) {
        if (item == null || item.getId() <= 0) {
            return false;
        }
        
        boolean success = updateImageTags(item.getId(), tagString);
        if (success) {
            // Update the item's tags list
            item.setTags(getTagsForImage(item.getId()));
        }
        
        return success;
    }
    
    /**
     * Parses a comma-separated tag string into a list of tags.
     * @param tagString Comma-separated list of tag names
     * @return List of Tag objects
     */
    public static List<Tag> parseTagString(String tagString) {
        List<Tag> tags = new ArrayList<>();
        
        if (tagString == null || tagString.trim().isEmpty()) {
            return tags;
        }
        
        String[] tagNames = tagString.split(",");
        for (String tagName : tagNames) {
            tagName = tagName.trim();
            if (!tagName.isEmpty()) {
                Tag tag = new Tag(tagName);
                tags.add(tag);
            }
        }
        
        return tags;
    }
    
    /**
     * Loads tags for a list of gallery items.
     * @param items List of gallery items
     */
    public static void loadTagsForGalleryItems(List<GalleryItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        for (GalleryItem item : items) {
            item.setTags(getTagsForImage(item.getId()));
        }
    }
    
    /**
     * Gets all images with a specific tag.
     * @param tagName The name of the tag
     * @return List of image IDs with the tag
     */
    public static List<Integer> getImagesWithTag(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        Tag tag = TagDAO.getTagByName(tagName.toLowerCase().trim());
        if (tag == null) {
            return new ArrayList<>();
        }
        
        return TagDAO.getImagesWithTag(tag.getId());
    }
}
