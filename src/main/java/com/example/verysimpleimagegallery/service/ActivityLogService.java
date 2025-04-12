package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.ActivityLogDAO;
import com.example.verysimpleimagegallery.model.ActivityLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for handling activity log business logic.
 */
public class ActivityLogService {

    /**
     * Logs a new activity in the system.
     * @param activity Description of the activity
     * @param activityType Type of activity (upload, delete, login, etc.)
     * @param userId ID of the user who performed the activity
     * @return true if logging was successful, false otherwise
     */
    public static boolean logActivity(String activity, String activityType, int userId) {
        try {
            return ActivityLogDAO.logActivity(activity, activityType, userId);
        } catch (Exception e) {
            System.err.println("Error in logActivity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves recent activity logs.
     * @param limit Maximum number of logs to retrieve
     * @return List of recent activity logs
     */
    public static List<ActivityLog> getRecentActivityLogs(int limit) {
        try {
            return ActivityLogDAO.getRecentActivityLogs(limit);
        } catch (Exception e) {
            System.err.println("Error in getRecentActivityLogs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves activity logs for a specific user.
     * @param userId ID of the user
     * @param limit Maximum number of logs to retrieve
     * @return List of activity logs for the user
     */
    public static List<ActivityLog> getUserActivityLogs(int userId, int limit) {
        try {
            return ActivityLogDAO.getUserActivityLogs(userId, limit);
        } catch (Exception e) {
            System.err.println("Error in getUserActivityLogs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Checks if there are any activity logs in the database.
     * @return true if there are activity logs, false otherwise
     */
    public static boolean hasActivityLogs() {
        try {
            return ActivityLogDAO.getActivityLogCount() > 0;
        } catch (Exception e) {
            System.err.println("Error checking for activity logs: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a sample activity log if none exist.
     * This is useful for testing and ensuring the activity log section is populated.
     * @param userId ID of the user to associate with the log
     * @return true if successful, false otherwise
     */
    public static boolean createSampleActivityLogIfNeeded(int userId) {
        if (!hasActivityLogs()) {
            return logActivity("System initialized", "system", userId);
        }
        return true;
    }
}
