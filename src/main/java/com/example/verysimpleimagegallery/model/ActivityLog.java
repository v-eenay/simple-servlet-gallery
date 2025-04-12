package com.example.verysimpleimagegallery.model;

import java.sql.Timestamp;

/**
 * Model class representing an activity log entry in the system.
 */
public class ActivityLog {
    private int id;
    private String activity;
    private String activityType;
    private int userId;
    private String userName;
    private Timestamp createdAt;
    
    public ActivityLog() {
    }
    
    public ActivityLog(int id, String activity, String activityType, int userId, String userName, Timestamp createdAt) {
        this.id = id;
        this.activity = activity;
        this.activityType = activityType;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getActivity() {
        return activity;
    }
    
    public void setActivity(String activity) {
        this.activity = activity;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Returns a formatted time string for display in the UI.
     * @return A user-friendly time string
     */
    public String getFormattedTime() {
        if (createdAt == null) {
            return "Unknown time";
        }
        
        // Get current time and the activity time
        long now = System.currentTimeMillis();
        long activityTime = createdAt.getTime();
        long diff = now - activityTime;
        
        // Convert to appropriate time format
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days == 1 ? "Yesterday" : days + " days ago";
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else {
            return "Just now";
        }
    }
}
