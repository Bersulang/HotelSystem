package com.example.hotel.servlets;

import com.example.hotel.beans.UserBean;
import com.example.hotel.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "用户名和密码不能为空!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        UserBean user = userDAO.findUserByUsernameAndPassword(username, password);        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            System.out.println("LoginServlet: User '" + user.getUsername() + "' logged in with role '" + user.getRole() + "'.");
            
            // 检查是否有登录后需要重定向的页面
            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            
            if ("admin".equals(user.getRole())) {
                // 管理员重定向到管理控制台
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else if ("user".equals(user.getRole())) {
                if (redirectAfterLogin != null && !redirectAfterLogin.isEmpty()) {
                    // 清除重定向信息
                    session.removeAttribute("redirectAfterLogin");
                    // 重定向到用户之前尝试访问的页面
                    String reason = request.getParameter("reason");
                    if ("payment".equals(reason)) {
                        session.setAttribute("loginMessage", "登录成功！您现在可以继续完成支付。");
                    } else {
                        session.setAttribute("loginMessage", "登录成功！");
                    }
                    response.sendRedirect(redirectAfterLogin);
                } else {
                    // 默认重定向到查询页面
                    response.sendRedirect(request.getContextPath() + "/queryForm.jsp"); 
                }
            } else {
                // Should not happen if roles are 'admin' or 'user'
                request.setAttribute("errorMessage", "未知的用户角色!");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "用户名或密码无效!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
} 