package com.example.verysimpleimagegallery.service;

import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.User;

import java.sql.SQLException;

public class AuthService {
    public static int createUser(String fullname, String email, String password) throws SQLException {
        if(UserDAO.getUserByEmail(email)!=-1){
            return -1;
        }
        User newUser = new User(fullname, email, password);
        return UserDAO.createUser(newUser);
    }
    public static User validateUser(String email, String password) throws SQLException {
       return UserDAO.validateUser(email, password);
    }
}
