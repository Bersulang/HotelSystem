package com.example.hotel.servlets;

import com.example.hotel.beans.UserBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String referer = request.getHeader("Referer");
        String redirectUrl = request.getContextPath() + "/queryForm.jsp"; // 默认返回首页
        
        if (referer != null && !referer.isEmpty()) {
            redirectUrl = referer; // 返回来源页面
        }
        
        if (session != null) {
            UserBean currentUser = (UserBean) session.getAttribute("currentUser");
            if (currentUser != null) {
                System.out.println("LogoutServlet: User '" + currentUser.getUsername() + "' logging out.");
            }
            session.invalidate();
        }
        
        // 创建一个新会话来显示退出成功消息
        session = request.getSession(true);
        session.setAttribute("logoutMessage", "您已成功退出登录");
        
        response.sendRedirect(redirectUrl);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
