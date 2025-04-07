package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GalleryItemDAO {

    public static int addGalleryItem(GalleryItem item) {
        String sql = "INSERT INTO gallery_items (title, image, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getTitle());
            ps.setBytes(2, item.getImage());
            ps.setInt(3, item.getUserId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public static List<GalleryItem> getUserGalleryItems(int userId) {
        List<GalleryItem> items = new ArrayList<>();
        String sql = "SELECT gi.id, gi.title, gi.image, gi.user_id, u.full_name FROM gallery_items gi JOIN users u ON gi.user_id = u.id WHERE user_id = ? ORDER BY gi.created_at DESC";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(createGalleryItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return items;
    }

    public static List<GalleryItem> getAllGalleryItems() {
        List<GalleryItem> items = new ArrayList<>();
        String sql = "SELECT gi.id, gi.title, gi.image, gi.user_id, u.full_name FROM gallery_items gi JOIN users u ON gi.user_id = u.id ORDER BY gi.created_at DESC";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(createGalleryItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return items;
    }

    public static GalleryItem getGalleryItemById(int itemId) {
        String sql = "SELECT gi.id, gi.title, gi.image, gi.user_id, u.full_name FROM gallery_items gi JOIN users u ON gi.user_id = u.id WHERE gi.id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return createGalleryItemFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static boolean deleteGalleryItem(int itemId) {
        String sql = "DELETE FROM gallery_items WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static int getImageCount() {
        String sql = "SELECT COUNT(*) as count FROM gallery_items";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private static GalleryItem createGalleryItemFromResultSet(ResultSet rs) throws SQLException {
        GalleryItem item = new GalleryItem();
        item.setId(rs.getInt("id"));
        item.setTitle(rs.getString("title"));
        item.setImage(rs.getBytes("image"));
        item.setUserId(rs.getInt("user_id"));
        item.setUserName(rs.getString("full_name"));
        return item;
    }
    private static String getUserName(GalleryItem item) {
        String sql = "SELECT full_name FROM users WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getUserId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static List<GalleryItem> getRecentActivities(int limit) {
        List<GalleryItem> items = new ArrayList<>();
        String sql = "SELECT gi.id, gi.title, gi.image, gi.user_id, u.full_name, gi.created_at " +
                    "FROM gallery_items gi " +
                    "JOIN users u ON gi.user_id = u.id " +
                    "ORDER BY gi.created_at DESC LIMIT ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GalleryItem item = createGalleryItemFromResultSet(rs);
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return items;
    }
}
