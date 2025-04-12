package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.ActivityLog;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling activity log operations in the database.
 */
public class ActivityLogDAO {

    /**
     * Logs a new activity in the system.
     * @param activity Description of the activity
     * @param activityType Type of activity (upload, delete, login, etc.)
     * @param userId ID of the user who performed the activity
     * @return true if logging was successful, false otherwise
     */
    public static boolean logActivity(String activity, String activityType, int userId) {
        String sql = "INSERT INTO activity_logs (activity, activity_type, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activity);
            ps.setString(2, activityType);
            ps.setInt(3, userId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error logging activity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves recent activity logs with user information.
     * @param limit Maximum number of logs to retrieve
     * @return List of recent activity logs
     */
    public static List<ActivityLog> getRecentActivityLogs(int limit) {
        String sql = "SELECT al.id, al.activity, al.activity_type, al.user_id, u.full_name, al.created_at " +
                     "FROM activity_logs al " +
                     "JOIN users u ON al.user_id = u.id " +
                     "ORDER BY al.created_at DESC " +
                     "LIMIT ?";

        List<ActivityLog> activityLogs = new ArrayList<>();

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActivityLog log = new ActivityLog();
                    log.setId(rs.getInt("id"));
                    log.setActivity(rs.getString("activity"));
                    log.setActivityType(rs.getString("activity_type"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setUserName(rs.getString("full_name"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));

                    activityLogs.add(log);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving activity logs: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for better debugging
        }

        // If no logs were found, try to get logs without joining to users table
        // This handles cases where user might have been deleted but logs remain
        if (activityLogs.isEmpty()) {
            String fallbackSql = "SELECT id, activity, activity_type, user_id, 'Unknown User' as full_name, created_at " +
                               "FROM activity_logs " +
                               "ORDER BY created_at DESC " +
                               "LIMIT ?";

            try (Connection conn = DbConnectionUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(fallbackSql)) {
                ps.setInt(1, limit);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ActivityLog log = new ActivityLog();
                        log.setId(rs.getInt("id"));
                        log.setActivity(rs.getString("activity"));
                        log.setActivityType(rs.getString("activity_type"));
                        log.setUserId(rs.getInt("user_id"));
                        log.setUserName(rs.getString("full_name"));
                        log.setCreatedAt(rs.getTimestamp("created_at"));

                        activityLogs.add(log);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving activity logs with fallback query: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return activityLogs;
    }

    /**
     * Retrieves activity logs for a specific user.
     * @param userId ID of the user
     * @param limit Maximum number of logs to retrieve
     * @return List of activity logs for the user
     */
    public static List<ActivityLog> getUserActivityLogs(int userId, int limit) {
        String sql = "SELECT al.id, al.activity, al.activity_type, al.user_id, u.full_name, al.created_at " +
                     "FROM activity_logs al " +
                     "JOIN users u ON al.user_id = u.id " +
                     "WHERE al.user_id = ? " +
                     "ORDER BY al.created_at DESC " +
                     "LIMIT ?";

        List<ActivityLog> activityLogs = new ArrayList<>();

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActivityLog log = new ActivityLog();
                    log.setId(rs.getInt("id"));
                    log.setActivity(rs.getString("activity"));
                    log.setActivityType(rs.getString("activity_type"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setUserName(rs.getString("full_name"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));

                    activityLogs.add(log);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user activity logs: " + e.getMessage());
            e.printStackTrace();
        }

        // If no logs were found, try to get logs without joining to users table
        if (activityLogs.isEmpty()) {
            String fallbackSql = "SELECT id, activity, activity_type, user_id, 'Unknown User' as full_name, created_at " +
                               "FROM activity_logs " +
                               "WHERE user_id = ? " +
                               "ORDER BY created_at DESC " +
                               "LIMIT ?";

            try (Connection conn = DbConnectionUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(fallbackSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, limit);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ActivityLog log = new ActivityLog();
                        log.setId(rs.getInt("id"));
                        log.setActivity(rs.getString("activity"));
                        log.setActivityType(rs.getString("activity_type"));
                        log.setUserId(rs.getInt("user_id"));
                        log.setUserName(rs.getString("full_name"));
                        log.setCreatedAt(rs.getTimestamp("created_at"));

                        activityLogs.add(log);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving user activity logs with fallback query: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return activityLogs;
    }

    /**
     * Gets the total count of activity logs in the database.
     * @return The number of activity logs
     */
    public static int getActivityLogCount() {
        String sql = "SELECT COUNT(*) as count FROM activity_logs";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error counting activity logs: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
