package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GalleryItemDAO {
    public static int createGalleryItem(GalleryItem galleryItem) {
        String sql = "INSERT INTO gallery_items (title, image, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, galleryItem.getTitle());
            stmt.setBytes(2, galleryItem.getImage());
            stmt.setInt(3, galleryItem.getUserId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public static ArrayList<GalleryItem> getGalleryItems() {
        ArrayList<GalleryItem> galleryItems = new ArrayList<>();
        String sql = "SELECT * FROM gallery_items";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GalleryItem galleryItem = new GalleryItem();
                galleryItem.setId(rs.getInt("id"));
                galleryItem.setTitle(rs.getString("title"));
                galleryItem.setImage(rs.getBytes("image"));
                galleryItem.setUserId(rs.getInt("user_id"));
                galleryItems.add(galleryItem);
            }
            return galleryItems;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static GalleryItem getGalleryItem(int id) {
        String sql = "SELECT * FROM gallery_items WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    GalleryItem galleryItem = new GalleryItem();
                    galleryItem.setId(rs.getInt("id"));
                    galleryItem.setTitle(rs.getString("title"));
                    galleryItem.setImage(rs.getBytes("image"));
                    galleryItem.setUserId(rs.getInt("user_id"));
                    return galleryItem;
                }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean deleteGalleryItem(GalleryItem galleryItem) {
        String sql = "DELETE FROM gallery_items WHERE id = ?";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, galleryItem.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
