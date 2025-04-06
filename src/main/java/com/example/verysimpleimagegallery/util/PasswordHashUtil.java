package com.example.verysimpleimagegallery.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt
 */
public class PasswordHashUtil {
    
    // Define the log rounds for BCrypt (higher is more secure but slower)
    private static final int LOG_ROUNDS = 12;
    
    /**
     * Hash a password using BCrypt
     * 
     * @param plainTextPassword the plain text password to hash
     * @return the hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        // Generate a salt and hash the password
        String salt = BCrypt.gensalt(LOG_ROUNDS);
        return BCrypt.hashpw(plainTextPassword, salt);
    }
    
    /**
     * Verify a password against a hashed password
     * 
     * @param plainTextPassword the plain text password to check
     * @param hashedPassword the hashed password to check against
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty() || 
            hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        
        try {
            // Check if the password matches the hash
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // If the stored hash is invalid, return false
            return false;
        }
    }
} 