package com.example.hotel.dao;

import com.example.hotel.beans.UserBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminUserDAO {
    private static final Logger logger = Logger.getLogger(AdminUserDAO.class.getName());
    
    // 获取所有用户
    public List<UserBean> getAllUsers() {
        List<UserBean> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {            conn = DBConnectionUtil.getConnection();
            String query = "SELECT * FROM users ORDER BY username";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                UserBean user = new UserBean();                user.setUserId(rs.getString("user_id"));
                user.setUserName(rs.getString("username"));
                // 不返回密码
                user.setUserRole(rs.getString("role"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                
                users.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "获取所有用户失败", e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return users;
    }
    
    // 根据用户名获取用户
    public UserBean getUserByUsername(String userName) {
        UserBean user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {            conn = DBConnectionUtil.getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new UserBean();                user.setUserId(rs.getString("user_id"));
                user.setUserName(rs.getString("username"));
                // 不返回密码
                user.setUserRole(rs.getString("role"));
                user.setFullName(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "根据用户名获取用户失败: " + userName, e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return user;
    }
    
    // 添加新用户
    public boolean addUser(UserBean user) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            
            // 检查用户名是否已存在
            if (getUserByUsername(user.getUserName()) != null) {
                return false;
            }
              String query = "INSERT INTO users (user_id, username, password, role, fullname, email, phone) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getUserRole());
            ps.setString(5, user.getFullName());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getPhone());
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "添加用户失败: " + user.getUserName(), e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
    
    // 更新用户信息（不更新密码）
    public boolean updateUser(UserBean user) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {            conn = DBConnectionUtil.getConnection();            String query = "UPDATE users SET fullname = ?, email = ?, phone = ?, role = ? " +
                    "WHERE username = ?";
            ps = conn.prepareStatement(query);
            
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getUserRole());
            ps.setString(5, user.getUserName());
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "更新用户信息失败: " + user.getUserName(), e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
    
    // 更新用户密码
    public boolean updateUserPassword(String userName, String newPassword) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String query = "UPDATE users SET password = ? WHERE username = ?";
            ps = conn.prepareStatement(query);
            
            ps.setString(1, newPassword);
            ps.setString(2, userName);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "更新用户密码失败: " + userName, e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
    
    // 删除用户
    public boolean deleteUser(String userName) {
        // 不允许删除管理员账号
        if ("admin".equals(userName)) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
              // 检查用户是否有关联的预订
            String checkQuery = "SELECT COUNT(*) FROM bookings WHERE guest_name = ?";
            ps = conn.prepareStatement(checkQuery);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // 如果有关联的预订，则不能删除
                return false;
            }
            
            ps.close();
            rs.close();
            
            String deleteQuery = "DELETE FROM users WHERE username = ?";
            ps = conn.prepareStatement(deleteQuery);
            ps.setString(1, userName);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "删除用户失败: " + userName, e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
}
