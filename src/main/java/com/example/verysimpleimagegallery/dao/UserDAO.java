package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static int createUser(User user) {
        String sql = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
        try(Connection conn = DbConnectionUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)
        ){
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected>0) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    /**
     * @deprecated This method uses plain text password comparison. 
     * Use AuthService.validateUser(email, password) which uses secure password hashing.
     */
    @Deprecated
    public static User validateUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try(Connection conn = DbConnectionUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        }
        return null;
    }
    public static int getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try(Connection conn = DbConnectionUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("id");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }
    public static User getUserByEmailOnly(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
