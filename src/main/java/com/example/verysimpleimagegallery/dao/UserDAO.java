package com.example.verysimpleimagegallery.dao;

import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.util.DbConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static int createUser(User user) {
        String sql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        try(Connection conn = DbConnectionUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)
        ){
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRole());
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
                user.setRole(rs.getInt("role"));
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
                user.setRole(rs.getInt("role"));
                return user;
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public static User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(""); // Don't send password to client
                user.setRole(rs.getInt("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return users;
    }
    
    public static List<User> getNonAdminUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 1 ORDER BY id"; // 1 = regular users
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(""); // Don't send password to client
                user.setRole(rs.getInt("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return users;
    }
    
    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    public static boolean updateUserRole(int userId, int role) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, role);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    public static int getUserCount() {
        String sql = "SELECT COUNT(*) as count FROM users";
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
    
    public static int getAdminCount() {
        String sql = "SELECT COUNT(*) as count FROM users WHERE role = 0 OR role = 2"; // Super admin (0) or regular admin (2)
        try(Connection conn = DbConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}
