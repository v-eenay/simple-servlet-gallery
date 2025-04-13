package com.example.verysimpleimagegallery.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to create the tag-related database tables.
 */
public class CreateTagTables {

    /**
     * Creates the tags and image_tags tables if they don't exist.
     * @return true if successful, false otherwise
     */
    public static boolean createTables() {
        try (Connection conn = DbConnectionUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create tags table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tags (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL UNIQUE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")"
            );
            
            // Create image_tags relationship table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS image_tags (" +
                "image_id INT NOT NULL, " +
                "tag_id INT NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (image_id, tag_id), " +
                "FOREIGN KEY (image_id) REFERENCES gallery_items(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Create indexes for faster lookups
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_image_tags_image_id ON image_tags(image_id)");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_image_tags_tag_id ON image_tags(tag_id)");
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating tag tables: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Main method to run the table creation directly.
     */
    public static void main(String[] args) {
        boolean success = createTables();
        if (success) {
            System.out.println("Tag tables created successfully!");
        } else {
            System.out.println("Failed to create tag tables.");
        }
    }
}
