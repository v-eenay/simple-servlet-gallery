package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling gallery item operations in the database.
 * Provides methods for CRUD operations and queries on gallery items.
 */
public class GalleryItemDAO {
    // SQL query constants
    private static final String BASE_SELECT_QUERY =
        "SELECT gi.id, gi.title, gi.image, gi.user_id, u.full_name " +
        "FROM gallery_items gi JOIN users u ON gi.user_id = u.id";

    /**
     * Adds a new gallery item to the database.
     * @param item The gallery item to add
     * @return The generated ID of the new item, or -1 if insertion failed
     */
    public static int addGalleryItem(GalleryItem item) {
        String sql = "INSERT INTO gallery_items (title, image, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getTitle());
            ps.setBytes(2, item.getImage());
            ps.setInt(3, item.getUserId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding gallery item: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves all gallery items for a specific user.
     * @param userId The ID of the user
     * @return List of gallery items belonging to the user
     */
    public static List<GalleryItem> getUserGalleryItems(int userId) {
        String sql = BASE_SELECT_QUERY + " WHERE user_id = ? ORDER BY gi.created_at DESC";
        return executeGalleryQuery(sql, ps -> ps.setInt(1, userId));
    }

    /**
     * Retrieves all gallery items in the system.
     * @return List of all gallery items
     */
    public static List<GalleryItem> getAllGalleryItems() {
        String sql = BASE_SELECT_QUERY + " ORDER BY gi.created_at DESC";
        return executeGalleryQuery(sql, null);
    }

    /**
     * Retrieves a specific gallery item by its ID.
     * @param itemId The ID of the gallery item
     * @return The gallery item if found, null otherwise
     */
    public static GalleryItem getGalleryItemById(int itemId) {
        String sql = BASE_SELECT_QUERY + " WHERE gi.id = ?";
        List<GalleryItem> items = executeGalleryQuery(sql, ps -> ps.setInt(1, itemId));
        return items.isEmpty() ? null : items.get(0);
    }

    /**
     * Updates a gallery item's title in the database.
     * @param itemId The ID of the gallery item to update
     * @param title The new title
     * @return true if update was successful, false otherwise
     */
    public static boolean updateGalleryItemTitle(int itemId, String title) {
        String sql = "UPDATE gallery_items SET title = ? WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, itemId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating gallery item title: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a gallery item's image in the database.
     * @param itemId The ID of the gallery item to update
     * @param image The new image data
     * @return true if update was successful, false otherwise
     */
    public static boolean updateGalleryItemImage(int itemId, byte[] image) {
        String sql = "UPDATE gallery_items SET image = ? WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, image);
            ps.setInt(2, itemId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating gallery item image: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a gallery item from the database.
     * @param itemId The ID of the item to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteGalleryItem(int itemId) {
        String sql = "DELETE FROM gallery_items WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting gallery item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the total count of images in the gallery.
     * @return The total number of images
     */
    public static int getImageCount() {
        String sql = "SELECT COUNT(*) as count FROM gallery_items";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("count") : 0;
        } catch (SQLException e) {
            System.err.println("Error getting image count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Gets the most recent gallery activities.
     * @param limit Maximum number of activities to retrieve
     * @return List of recent gallery items
     */
    public static List<GalleryItem> getRecentActivities(int limit) {
        String sql = BASE_SELECT_QUERY + " ORDER BY gi.created_at DESC LIMIT ?";
        return executeGalleryQuery(sql, ps -> ps.setInt(1, limit));
    }

    /**
     * Helper method to execute gallery queries and map results to GalleryItem objects.
     * @param sql The SQL query to execute
     * @param paramSetter Lambda to set query parameters (can be null)
     * @return List of gallery items
     */
    private static List<GalleryItem> executeGalleryQuery(String sql, SqlParamSetter paramSetter) {
        List<GalleryItem> items = new ArrayList<>();
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (paramSetter != null) {
                paramSetter.setParameters(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(createGalleryItemFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing gallery query: " + e.getMessage());
        }
        return items;
    }

    /**
     * Creates a GalleryItem object from a ResultSet row.
     */
    private static GalleryItem createGalleryItemFromResultSet(ResultSet rs) throws SQLException {
        GalleryItem item = new GalleryItem();
        item.setId(rs.getInt("id"));
        item.setTitle(rs.getString("title"));
        item.setImage(rs.getBytes("image"));
        item.setUserId(rs.getInt("user_id"));
        item.setUserName(rs.getString("full_name"));
        return item;
    }

    /**
     * Functional interface for setting SQL parameters.
     */
    @FunctionalInterface
    private interface SqlParamSetter {
        void setParameters(PreparedStatement ps) throws SQLException;
    }
}
