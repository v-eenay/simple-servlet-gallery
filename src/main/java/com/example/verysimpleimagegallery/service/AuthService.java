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
    
    public static User validateUser(String email, String password) throws SQLException {
        // Get the user by email (will need to modify UserDAO to get user by email only)
        User user = UserDAO.getUserByEmailOnly(email);
        
        // If user exists and password matches the stored hash
        if (user != null && PasswordHashUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }
}
