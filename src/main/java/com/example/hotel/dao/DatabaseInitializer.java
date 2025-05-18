package com.example.hotel.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据库初始化工具，用于执行SQL脚本
 * 如果数据库中有数据则这个文件没什么卵用，留着对于迁移使用有好处
 */
public class DatabaseInitializer {
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    /**
     * 执行数据库初始化脚本
     */
    public static void initializeDatabase() {
        logger.info("开始初始化数据库...");
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DBConnectionUtil.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            // 从类路径加载SQL脚本
            InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("database_schema.sql");
            if (inputStream == null) {
                logger.severe("无法找到数据库初始化脚本");
                return;
            }

            // 读取并执行SQL语句
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder sqlStatement = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    // 忽略注释和空行
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("--")) {
                        continue;
                    }
                    
                    sqlStatement.append(line);
                    
                    // 如果当前行以分号结尾，则执行语句
                    if (line.endsWith(";")) {
                        String sql = sqlStatement.toString();
                        try {
                            stmt.executeUpdate(sql);
                            logger.fine("执行SQL: " + sql);
                        } catch (SQLException e) {
                            // 忽略"表已存在"或"重复键"错误
                            if (e.getErrorCode() == 1050 || e.getErrorCode() == 1062) {
                                logger.warning("忽略错误: " + e.getMessage());
                            } else {
                                throw e;
                            }
                        }
                        sqlStatement = new StringBuilder();
                    }
                }
            }
            
            conn.commit();
            logger.info("数据库初始化完成");
            
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "数据库初始化失败", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    logger.log(Level.SEVERE, "回滚事务失败", rollbackEx);
                }
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "关闭Statement失败", e);
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "关闭Connection失败", e);
                }
            }
        }
    }
}
