package com.example.hotel.servlets;

import com.example.hotel.beans.UserBean;
import com.example.hotel.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/manageUsers")
public class UserManagementServlet extends HttpServlet {
    
    private AdminService adminService;
    
    @Override
    public void init() {
        adminService = new AdminService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");
        
        // 检查是否是管理员
        if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        String action = req.getParameter("action");
        
        if (action == null) {
            // 默认显示所有用户
            resp.sendRedirect("manage_users.jsp");
            return;
        }
        
        switch (action) {
            case "add":
                // 跳转到添加用户页面
                req.getRequestDispatcher("user_form.jsp").forward(req, resp);
                break;
            case "edit":
                // 编辑现有用户
                String userName = req.getParameter("userName");
                UserBean user = adminService.getUserByUsername(userName);
                
                if (user != null) {
                    req.setAttribute("user", user);
                    req.getRequestDispatcher("user_form.jsp").forward(req, resp);
                } else {
                    req.setAttribute("message", "Error: 未找到指定的用户");
                    req.getRequestDispatcher("manage_users.jsp").forward(req, resp);
                }
                break;
            case "delete":
                // 删除用户
                String deleteUserName = req.getParameter("userName");
                
                // 检查是否是管理员账户
                if ("admin".equals(deleteUserName)) {
                    req.setAttribute("message", "Error: 管理员账户不能被删除");
                    req.getRequestDispatcher("manage_users.jsp").forward(req, resp);
                    return;
                }
                
                boolean deleted = adminService.deleteUser(deleteUserName);
                
                if (deleted) {
                    req.setAttribute("message", "用户已成功删除");
                } else {
                    req.setAttribute("message", "Error: 无法删除用户，可能有关联的预订");
                }
                req.getRequestDispatcher("manage_users.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect("manage_users.jsp");
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");
        
        // 检查是否是管理员
        if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        req.setCharacterEncoding("UTF-8"); // 确保正确处理中文输入
        
        String action = req.getParameter("action");
        
        if ("createUser".equals(action) || "updateUser".equals(action)) {
            // 从表单收集用户信息
            UserBean user = new UserBean();
            user.setUserName(req.getParameter("userName"));
            user.setFullName(req.getParameter("fullName"));
            user.setEmail(req.getParameter("email"));
            user.setPhone(req.getParameter("phone"));
            user.setUserRole(req.getParameter("userRole"));
            
            String password = req.getParameter("password");
            boolean success = false;
            
            if ("createUser".equals(action)) {
                // 检查用户名是否已存在
                UserBean existingUser = adminService.getUserByUsername(user.getUserName());
                if (existingUser != null) {
                    req.setAttribute("message", "Error: 用户名已存在，请使用不同的用户名");
                    req.getRequestDispatcher("user_form.jsp").forward(req, resp);
                    return;
                }
                
                // 添加新用户时，密码是必需的
                if (password == null || password.isEmpty()) {
                    req.setAttribute("message", "Error: 创建用户时必须提供密码");
                    req.getRequestDispatcher("user_form.jsp").forward(req, resp);
                    return;
                }
                
                user.setPassword(password);
                // 添加新用户
                success = adminService.addUser(user);
                if (success) {
                    req.setAttribute("message", "用户已成功添加");
                } else {
                    req.setAttribute("message", "Error: 添加用户失败");
                }
            } else {
                // 更新现有用户
                
                // 检查是否是管理员账户
                boolean isAdminAccount = "admin".equals(user.getUserName());
                
                // 如果是管理员账户，确保用户角色保持为"admin"
                if (isAdminAccount) {
                    user.setUserRole("admin");
                }
                
                // 如果提供了新密码，则更新密码
                if (password != null && !password.isEmpty() && !isAdminAccount) {
                    success = adminService.updateUserPassword(user.getUserName(), password);
                    if (!success) {
                        req.setAttribute("message", "Error: 更新密码失败");
                        req.getRequestDispatcher("manage_users.jsp").forward(req, resp);
                        return;
                    }
                }
                
                // 更新用户其他信息
                success = adminService.updateUser(user);
                if (success) {
                    req.setAttribute("message", "用户已成功更新");
                } else {
                    req.setAttribute("message", "Error: 更新用户失败");
                }
            }
            
            req.getRequestDispatcher("manage_users.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("manage_users.jsp");
        }
    }
}
