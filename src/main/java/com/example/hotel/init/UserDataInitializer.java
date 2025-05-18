package com.example.hotel.init;

import com.example.hotel.dao.DBConnectionUtil;
import com.example.hotel.dao.UserDAO;
import com.example.hotel.beans.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 初始化用户数据的工具类
 * 如果数据库中没有测试用户，则创建测试用户
 */
public class UserDataInitializer {

    private static final Logger logger = Logger.getLogger(UserDataInitializer.class.getName());

    public static void initDefaultUsers() {
        logger.info("初始化默认用户...");
        
        // 1. 检查admin用户是否存在
        if (!userExists("admin")) {
            createAdminUser();
        }
        
        // 2. 检查普通测试用户是否存在
        if (!userExists("user1")) {
            createNormalUser();
        }
        
        // 3. 确保User123用户存在
        if (!userExists("User123")) {
            createUser123();
        }
        
        logger.info("默认用户初始化完成");
    }
    
    private static boolean userExists(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String query = "SELECT 1 FROM users WHERE username = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            return rs.next(); // 如果有记录，则用户存在
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "检查用户是否存在时出错", e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
    }
    
    private static void createAdminUser() {
        try {
            UserBean adminUser = new UserBean();
            adminUser.setUserId("U_admin001");
            adminUser.setUserName("admin");
            adminUser.setPassword("admin123");
            adminUser.setUserRole("admin");
            adminUser.setFullName("系统管理员");
            adminUser.setEmail("admin@hotel.com");
            adminUser.setPhone("13800138000");
            
            new UserDAO().createUser(adminUser);
            logger.info("创建管理员用户成功");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "创建管理员用户失败", e);
        }
    }
    
    private static void createNormalUser() {
        try {
            UserBean normalUser = new UserBean();
            normalUser.setUserId("U_user001");
            normalUser.setUserName("user1");
            normalUser.setPassword("user123");
            normalUser.setUserRole("user");
            normalUser.setFullName("测试用户");
            normalUser.setEmail("user1@example.com");
            normalUser.setPhone("13900139000");
            
            new UserDAO().createUser(normalUser);
            logger.info("创建普通测试用户成功");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "创建普通测试用户失败", e);
        }
    }
    
    private static void createUser123() {
        try {
            UserBean user123 = new UserBean();
            user123.setUserId("U_user123");
            user123.setUserName("User123");
            user123.setPassword("pass123");
            user123.setUserRole("user");
            user123.setFullName("测试用户123");
            user123.setEmail("user123@example.com");
            user123.setPhone("13912312300");
            
            new UserDAO().createUser(user123);
            logger.info("创建User123用户成功");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "创建User123用户失败", e);
        }
    }
}
