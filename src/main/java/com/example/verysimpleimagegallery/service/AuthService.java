package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.util.PasswordHashUtil;

import java.sql.SQLException;

public class AuthService {
    public static int createUser(String fullname, String email, String password) throws SQLException {
        if(UserDAO.getUserByEmail(email)!=-1){
            return -1;
        }
        
        // Hash the password before storing it
        String hashedPassword = PasswordHashUtil.hashPassword(password);
        
        User newUser = new User(fullname, email, hashedPassword);
        return UserDAO.createUser(newUser);
    }
    
    public static int createAdminUser(String fullname, String email, String password, boolean isSuperAdmin) throws SQLException {
        if(UserDAO.getUserByEmail(email)!=-1){
            return -1;
        }
        
        // Hash the password before storing it
        String hashedPassword = PasswordHashUtil.hashPassword(password);
        
        // Set role: 0 for super admin, 2 for regular admin
        int role = isSuperAdmin ? 0 : 2;
        
        User newUser = new User(fullname, email, hashedPassword, role);
        return UserDAO.createUser(newUser);
    }
    
    public static User validateUser(String email, String password) throws SQLException {
        // Get the user by email (will need to modify UserDAO to get user by email only)
        User user = UserDAO.getUserByEmailOnly(email);
        
        // If user exists and password matches the stored hash
        if (user != null && PasswordHashUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }
    
    public static boolean hasAdminAccess(User user) {
        if (user == null) return false;
        return user.hasAdminPermissions();
    }
    
    public static boolean hasSuperAdminAccess(User user) {
        if (user == null) return false;
        return user.isSuperAdmin();
    }
    
    public static boolean canDeleteUser(User currentUser, User targetUser) {
        if (currentUser == null || targetUser == null) return false;
        
        // Super admin can delete any user
        if (currentUser.isSuperAdmin()) return true;
        
        // Regular admin can only delete regular users
        if (currentUser.isAdmin() && targetUser.isRegularUser()) return true;
        
        return false;
    }
    
    public static boolean canUploadImages(User user) {
        if (user == null) return false;
        return user.canUploadImages();
    }
    
    public static boolean canDeleteImage(User currentUser, User imageOwner) {
        if (currentUser == null) return false;
        
        // Image owner can delete their own image
        if (currentUser.getId() == imageOwner.getId()) return true;
        
        // Admin users can delete any image
        if (currentUser.hasAdminPermissions()) return true;
        
        return false;
    }
    
    public static boolean canViewAllImages(User user) {
        if (user == null) return false;
        return user.hasAdminPermissions();
    }
}
