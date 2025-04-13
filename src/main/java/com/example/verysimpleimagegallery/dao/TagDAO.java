package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.Tag;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling tag operations in the database.
 */
public class TagDAO {

    /**
     * Creates a new tag in the database.
     * @param tag The tag to create
     * @return The ID of the newly created tag, or -1 if creation failed
     */
    public static int createTag(Tag tag) {
        String sql = "INSERT INTO tags (name) VALUES (?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tag.getName().toLowerCase().trim());
            
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating tag: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Gets a tag by its ID.
     * @param id The ID of the tag
     * @return The tag if found, null otherwise
     */
    public static Tag getTagById(int id) {
        String sql = "SELECT * FROM tags WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createTagFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting tag by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets a tag by its name.
     * @param name The name of the tag
     * @return The tag if found, null otherwise
     */
    public static Tag getTagByName(String name) {
        String sql = "SELECT * FROM tags WHERE name = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.toLowerCase().trim());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createTagFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting tag by name: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets all tags in the system.
     * @return List of all tags
     */
    public static List<Tag> getAllTags() {
        String sql = "SELECT * FROM tags ORDER BY name";
        List<Tag> tags = new ArrayList<>();
        
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                tags.add(createTagFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all tags: " + e.getMessage());
        }
        
        return tags;
    }
    
    /**
     * Gets all tags for a specific image.
     * @param imageId The ID of the image
     * @return List of tags associated with the image
     */
    public static List<Tag> getTagsForImage(int imageId) {
        String sql = "SELECT t.* FROM tags t " +
                     "JOIN image_tags it ON t.id = it.tag_id " +
                     "WHERE it.image_id = ? " +
                     "ORDER BY t.name";
        List<Tag> tags = new ArrayList<>();
        
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tags.add(createTagFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting tags for image: " + e.getMessage());
        }
        
        return tags;
    }
    
    /**
     * Associates a tag with an image.
     * @param imageId The ID of the image
     * @param tagId The ID of the tag
     * @return true if successful, false otherwise
     */
    public static boolean addTagToImage(int imageId, int tagId) {
        String sql = "INSERT IGNORE INTO image_tags (image_id, tag_id) VALUES (?, ?)";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.setInt(2, tagId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding tag to image: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Removes a tag association from an image.
     * @param imageId The ID of the image
     * @param tagId The ID of the tag
     * @return true if successful, false otherwise
     */
    public static boolean removeTagFromImage(int imageId, int tagId) {
        String sql = "DELETE FROM image_tags WHERE image_id = ? AND tag_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.setInt(2, tagId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing tag from image: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Removes all tag associations for an image.
     * @param imageId The ID of the image
     * @return true if successful, false otherwise
     */
    public static boolean removeAllTagsFromImage(int imageId) {
        String sql = "DELETE FROM image_tags WHERE image_id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error removing all tags from image: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Gets all images with a specific tag.
     * @param tagId The ID of the tag
     * @return List of image IDs with the tag
     */
    public static List<Integer> getImagesWithTag(int tagId) {
        String sql = "SELECT image_id FROM image_tags WHERE tag_id = ?";
        List<Integer> imageIds = new ArrayList<>();
        
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tagId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    imageIds.add(rs.getInt("image_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting images with tag: " + e.getMessage());
        }
        
        return imageIds;
    }
    
    /**
     * Creates a Tag object from a ResultSet row.
     */
    private static Tag createTagFromResultSet(ResultSet rs) throws SQLException {
        return new Tag(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
