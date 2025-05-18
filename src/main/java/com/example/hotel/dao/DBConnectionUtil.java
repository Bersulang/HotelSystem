package com.example.hotel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 提供获取数据库连接的方法。这是所有dao类与数据库通信的基础。*/
public class DBConnectionUtil {
    // 数据库连接参数
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotelUse?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "059296";

    // 注册JDBC驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver registered successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage()); //可能还有别的错误，只是用于调试
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 重载关闭PreparedStatement（常见情况）
     public static void closeConnection(Connection conn, java.sql.PreparedStatement pstmt, ResultSet rs) {
        closeConnection(conn, (Statement)pstmt, rs); // Cast and reuse
    }

    // 当没有 ResultSet 时重载关闭（例如，INSERT、UPDATE、DELETE）
    public static void closeConnection(Connection conn, Statement stmt) {
        closeConnection(conn, stmt, null);
    }
    
    public static void closeConnection(Connection conn, java.sql.PreparedStatement pstmt) {
        closeConnection(conn, (Statement)pstmt, null);
    }
} 