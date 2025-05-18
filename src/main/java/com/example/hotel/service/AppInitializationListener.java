package com.example.hotel.service;

import com.example.hotel.dao.DatabaseInitializer;
import com.example.hotel.init.UserDataInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

/**
 * 应用程序启动监听器，用于初始化数据库
 */
@WebListener
public class AppInitializationListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(AppInitializationListener.class.getName());    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("应用程序正在启动，初始化数据库...");
        DatabaseInitializer.initializeDatabase();
        
        // 初始化默认用户
        logger.info("初始化默认用户数据...");
        UserDataInitializer.initDefaultUsers();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("应用程序正在关闭...");
    }
}
