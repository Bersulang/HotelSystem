package com.example.hotel.servlets;

import com.example.hotel.beans.UserBean;
import com.example.hotel.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty() || 
            password == null || password.isEmpty() || 
            confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("errorMessage", "所有字段均为必填项!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "两次输入的密码不匹配!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.usernameExists(username)) {
            request.setAttribute("errorMessage", "用户名 '" + username + "' 已存在!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        UserBean newUser = new UserBean();
        newUser.setUserId("U_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("user");

        boolean success = userDAO.createUser(newUser);

        if (success) {
            System.out.println("RegisterServlet: User '" + username + "' registered successfully.");
            request.setAttribute("successMessage", "注册成功! 请登录。");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "注册失败，请稍后再试。");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
} 