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
        
        // Set role: 0 for admin, 2 for super admin
        int role = isSuperAdmin ? 2 : 0;
        
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
        return user.isAdmin() || user.isSuperAdmin();
    }
    
    public static boolean hasSuperAdminAccess(User user) {
        if (user == null) return false;
        return user.isSuperAdmin();
    }
    
    public static boolean canDeleteUser(User currentUser, User targetUser) {
        if (currentUser == null || targetUser == null) return false;
        
        // Super admin can delete any user
        if (currentUser.isSuperAdmin()) return true;
        
        // Admin can only delete regular users
        if (currentUser.isAdmin() && targetUser.isRegularUser()) return true;
        
        return false;
    }
}
