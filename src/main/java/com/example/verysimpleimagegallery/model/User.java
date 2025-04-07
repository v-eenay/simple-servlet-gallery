package com.example.verysimpleimagegallery.model;

public class User {
    private int id;
    private String FullName;
    private String Email;
    private String Password;
    private int role = 1; // Default to regular user (1)

    public User() {
    }

    public User(String fullName, String email, String password) {
        FullName = fullName;
        Email = email;
        Password = password;
    }

    public User(int id, String fullName, String email, String password) {
        this.id = id;
        FullName = fullName;
        Email = email;
        Password = password;
    }
    
    public User(String fullName, String email, String password, int role) {
        FullName = fullName;
        Email = email;
        Password = password;
        this.role = role;
    }
    
    public User(int id, String fullName, String email, String password, int role) {
        this.id = id;
        FullName = fullName;
        Email = email;
        Password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    // Helper methods for role checks
    public boolean isSuperAdmin() {
        return role == 0;
    }
    
    public boolean isRegularUser() {
        return role == 1;
    }
    
    public boolean isAdmin() {
        return role == 2;
    }
    
    // Helper method to check if user has admin-level permissions
    public boolean hasAdminPermissions() {
        return role == 0 || role == 2; // Super admin or regular admin
    }
    
    // Helper method to check if user can upload images
    public boolean canUploadImages() {
        return role == 1 || role == 2; // Regular user or regular admin
    }
}
